package xbony2.huesodewiki.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;
import xbony2.huesodewiki.Utils;

public class CommandDumpStructure extends CommandBase {
	@Override
	public String getName(){
		return "dumpstructure";
	}

	@Override
	public String getUsage(ICommandSender sender){
		return "commands.dumpstructure.usage";
	}

	@Override
	public List<String> getAliases(){
		return Collections.emptyList();
	}

	private int amount;
	
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if(args.length < 6)
			throw new WrongUsageException("commands.dumpstructure.usage");

		BlockPos start = parseBlockPos(sender, args, 0, false);
		BlockPos end = parseBlockPos(sender, args, 3, false);
		World world = sender.getEntityWorld();
		
		if(!world.isAreaLoaded(start, end, false))
			throw new CommandException("commands.dumpstructure.outOfWorld");

		int sizeX = Math.abs(start.getX() - end.getX());
		int sizeZ = Math.abs(start.getZ() - end.getZ());
		
		if(sizeX >= 26 || sizeZ >= 26) //template supports up to 26x26 layers
			throw new CommandException("commands.dumpstructure.tooLarge");

		if(start.getY() < end.getY()){
			int y = end.getY() - start.getY();
			start = start.up(y);
			end = end.down(y);
		}

		List<MultiblockPiece> structure = new ArrayList<>();

		boolean reverse = Integer.signum(start.getX() - end.getX()) * Integer.signum(start.getZ() - end.getZ()) == 1;
		int paddingSizeEnd = Math.abs(sizeX - sizeZ);
		int paddingSizeStart = 0;

		BlockPos startPadded = start, endPadded = end;
		
		if(args.length > 6){
			String paddingMode = args[6];
			
			if(paddingMode.equals("back")){
				paddingSizeStart = paddingSizeEnd;
				paddingSizeEnd = 0;
			}else if(paddingMode.equals("center")){
				paddingSizeStart = paddingSizeEnd / 2 + paddingSizeEnd % 2;
				paddingSizeEnd = paddingSizeEnd / 2;
			}
		}

		if(sizeX > sizeZ){
			endPadded = end.add(0, 0, start.getZ() > end.getZ() ? -paddingSizeEnd : paddingSizeEnd);
			startPadded = start.add(0, 0, start.getZ() > end.getZ() ? paddingSizeStart : -paddingSizeStart);
		}else if(sizeZ > sizeX){
			endPadded = end.add(start.getX() > end.getX() ? -paddingSizeEnd : paddingSizeEnd, 0, 0);
			startPadded = start.add(start.getX() > end.getX() ? paddingSizeStart : -paddingSizeStart, 0, 0);
		}

		int minX = Math.min(start.getX(), end.getX());
		int minZ = Math.min(start.getZ(), end.getZ());
		int maxX = Math.max(start.getX(), end.getX());
		int maxZ = Math.max(start.getZ(), end.getZ());

		BlockPos startPoint = startPadded;
		amount = 0;
		BlockPos.getAllInBoxMutable(startPadded, endPadded).forEach(pos -> {
			int x = startPoint.getX() - pos.getX();
			int y = startPoint.getY() - pos.getY();
			int z = startPoint.getZ() - pos.getZ();
			
			if(pos.getX() >= minX && pos.getX() <= maxX && pos.getZ() >= minZ && pos.getZ() <= maxZ){
				IBlockState state = world.getBlockState(pos);
				
				if(state.getBlock() instanceof BlockLiquid || state.getBlock() instanceof IFluidBlock){
					Fluid fluid = FluidRegistry.lookupFluidForBlock(state.getBlock());
					
					if(fluid != null){
						structure.add(new MultiblockPiece(x, y, z, new FluidStack(fluid, 1000), reverse));
						return;
					}
				}
				
				RayTraceResult ray = new RayTraceResult(new Vec3d(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5), EnumFacing.UP, pos);
				ItemStack stack = state.getBlock().getPickBlock(state, ray, world, pos, (EntityPlayer) sender);
				
				if(!stack.isEmpty())
					amount++;
				
				structure.add(new MultiblockPiece(x, y, z, stack, reverse));
			}else
				structure.add(new MultiblockPiece(x, y, z, reverse));
		});
		
		structure.sort(reverse ? MultiblockPiece.ZX_COMPARE : MultiblockPiece.XZ_COMPARE);

		StringBuilder builder = new StringBuilder("{{Cg/Multiblock/Alt\n");
		
		if(sizeX >= 5 || sizeZ >= 5)
			builder.append("|oversize=").append(Math.max(sizeX, sizeZ) + 1).append('\n');

		boolean shouldCompact = args.length > 7 && "true".equals(args[7]);
		boolean previousEmpty = false;
		for(MultiblockPiece piece : structure){
			if(shouldCompact){
				if(previousEmpty && !piece.isEmpty())
					builder.append('\n');
				
				previousEmpty = piece.isEmpty();
			}
			
			builder.append(piece.toString());
			
			if(!shouldCompact || !piece.isEmpty())
				builder.append('\n');
			
		}
		
		if(shouldCompact && previousEmpty)
			builder.append('\n');
		
		builder.append("}}");

		Utils.copyString(builder.toString());
		sender.sendMessage(new TextComponentTranslation("commands.dumpstructure.success", amount));
	}

	private static class MultiblockPiece {
		final int x, y, z;
		ItemStack stack;
		FluidStack fluidStack;
		final boolean reverse;

		MultiblockPiece(int x, int y, int z, boolean reverse){
			this.x = Math.abs(x) + 1;
			this.y = Math.abs(y) + 1;
			this.z = Math.abs(z) + 1;
			this.reverse = reverse;
			stack = ItemStack.EMPTY;
		}

		MultiblockPiece(int x, int y, int z, ItemStack stack, boolean reverse){
			this(x, y, z, reverse);
			this.stack = stack;
			this.fluidStack = null;
		}

		MultiblockPiece(int x, int y, int z, FluidStack stack, boolean reverse){
			this(x, y, z, reverse);
			this.fluidStack = stack;
		}

		static final Comparator<MultiblockPiece> XZ_COMPARE = Comparator.comparingInt((MultiblockPiece p) -> p.y).thenComparingInt((MultiblockPiece p) -> p.x).thenComparingInt((MultiblockPiece p) -> p.z);

		static final Comparator<MultiblockPiece> ZX_COMPARE = Comparator.comparingInt((MultiblockPiece p) -> p.y).thenComparingInt((MultiblockPiece p) -> p.z).thenComparingInt((MultiblockPiece p) -> p.x);

		public boolean isEmpty(){
			return stack.isEmpty() && fluidStack == null;
		}

		@Override
		public String toString(){
			StringBuilder builder = new StringBuilder("|").append(getAlphabetLetter(y));
			
			if(reverse)
				builder.append(getAlphabetLetter(z)).append(x);
			else
				builder.append(getAlphabetLetter(x)).append(z);
			
			builder.append('=');

			if(!stack.isEmpty())
				builder.append(Utils.outputItem(stack));
			else if(fluidStack != null)
				builder.append(outputFluid(fluidStack));
			
			return builder.toString();
		}
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender){
		return true;
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos){
		//Clientside commands get the player's position in targetPos, not the pointed block
		RayTraceResult raytrace = Minecraft.getMinecraft().objectMouseOver;
		BlockPos pos = null;
		
		if(raytrace.typeOfHit == RayTraceResult.Type.BLOCK)
			pos = raytrace.getBlockPos();

		if(args.length > 0 && args.length <= 3)
			return getTabCompletionCoordinate(args, 0, pos);
		else if(args.length > 3 && args.length <= 6)
			return getTabCompletionCoordinate(args, 3, pos);
		else if(args.length == 7)
			return getListOfStringsMatchingLastWord(args, "center", "back", "front");
		else if(args.length == 8)
			return getListOfStringsMatchingLastWord(args, "true", "false");
		
		return Collections.emptyList();
	}

	private static char getAlphabetLetter(int index){
		return (char)(index + 'A' - 1);
	}

	private static String outputFluid(FluidStack fluidstack){
		return "{{Gc|mod=" + Utils.getModAbbrevation(Utils.getModName(FluidRegistry.getModId(fluidstack))) + "|dis=false|" + fluidstack.getLocalizedName() + "}}";
	}
}
