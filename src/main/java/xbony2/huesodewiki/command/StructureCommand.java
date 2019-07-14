package xbony2.huesodewiki.command;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.BlockPosArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.ForgeI18n;
import net.minecraftforge.fml.loading.FMLEnvironment;
import xbony2.huesodewiki.Utils;

import static com.mojang.brigadier.arguments.BoolArgumentType.getBool;
import static net.minecraft.command.arguments.BlockPosArgument.getLoadedBlockPos;

public class StructureCommand {
	private static final String START_POS = "startPos";
	private static final String END_POS = "endPos";
	private static final String SHOULD_COMPACT = "shouldCompact";
	private static final String SHOULD_WRAP_IN_TABLE = "shouldWrapInTable";

	private static final SimpleCommandExceptionType STRUCTURE_TOO_LARGE = new SimpleCommandExceptionType(new TranslationTextComponent("commands.dumpstructure.tooLarge"));

	public static void register(CommandDispatcher<CommandSource> dispatcher){
		dispatcher.register(Commands.literal("dumpstructure")
				.requires(s -> FMLEnvironment.dist.isClient() && s.getEntity() instanceof ServerPlayerEntity) //TODO make it work on servers, but not with /execute
				.then(Commands.argument(START_POS, BlockPosArgument.blockPos())
						.then(Commands.argument(END_POS, BlockPosArgument.blockPos())
								.executes(
										ctx -> execute(ctx.getSource(), getLoadedBlockPos(ctx, START_POS),
												getLoadedBlockPos(ctx, END_POS), "center",
												false, false))
								.then(Commands.argument(SHOULD_COMPACT, BoolArgumentType.bool())
										.executes(
												ctx -> execute(ctx.getSource(), getLoadedBlockPos(ctx, START_POS),
														getLoadedBlockPos(ctx, END_POS), "center",
														getBool(ctx, SHOULD_COMPACT), false))
										.then(Commands.argument(SHOULD_WRAP_IN_TABLE, BoolArgumentType.bool())
												.executes(
														ctx -> execute(ctx.getSource(), getLoadedBlockPos(ctx, START_POS),
																getLoadedBlockPos(ctx, END_POS), "center",
																getBool(ctx, SHOULD_COMPACT), getBool(ctx, SHOULD_WRAP_IN_TABLE)))
												.then(Commands.literal("center")
														.executes(
																ctx -> execute(ctx.getSource(), getLoadedBlockPos(ctx, START_POS),
																		getLoadedBlockPos(ctx, END_POS), "center",
																		getBool(ctx, SHOULD_COMPACT), getBool(ctx, SHOULD_WRAP_IN_TABLE))))
												.then(Commands.literal("back")
														.executes(
																ctx -> execute(ctx.getSource(), getLoadedBlockPos(ctx, START_POS),
																		getLoadedBlockPos(ctx, END_POS), "back",
																		getBool(ctx, SHOULD_COMPACT), getBool(ctx, SHOULD_WRAP_IN_TABLE))))
												.then(Commands.literal("front")
														.executes(
																ctx -> execute(ctx.getSource(), getLoadedBlockPos(ctx, START_POS),
																		getLoadedBlockPos(ctx, END_POS), "front",
																		getBool(ctx, SHOULD_COMPACT), getBool(ctx, SHOULD_WRAP_IN_TABLE)))
												)))))
		);
	}

	private static int execute(CommandSource source, BlockPos start, BlockPos end, String paddingMode, boolean shouldCompact, boolean shouldWrapInTable) throws CommandSyntaxException {

		World world = source.getWorld();
		ServerPlayerEntity player = source.asPlayer();

		int sizeX = Math.abs(start.getX() - end.getX());
		int sizeZ = Math.abs(start.getZ() - end.getZ());

		if(sizeX >= 26 || sizeZ >= 26) //template supports up to 26x26 layers
			throw STRUCTURE_TOO_LARGE.create();

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

		if(paddingMode.equals("back")){
			paddingSizeStart = paddingSizeEnd;
			paddingSizeEnd = 0;
		}else if(paddingMode.equals("center")){
			paddingSizeStart = paddingSizeEnd / 2 + paddingSizeEnd % 2;
			paddingSizeEnd = paddingSizeEnd / 2;
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
		int amount = 0;
		for(BlockPos pos : BlockPos.getAllInBoxMutable(startPadded, endPadded)){
			int x = startPoint.getX() - pos.getX();
			int y = startPoint.getY() - pos.getY();
			int z = startPoint.getZ() - pos.getZ();

			if(pos.getX() >= minX && pos.getX() <= maxX && pos.getZ() >= minZ && pos.getZ() <= maxZ){
				BlockState state = world.getBlockState(pos);
				
				RayTraceResult ray = new BlockRayTraceResult(new Vec3d(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5), Direction.UP, pos, true);
				ItemStack stack = state.getBlock().getPickBlock(state, ray, world, pos, player);

				if(!stack.isEmpty())
					amount++;
				else{
					IFluidState fluid = world.getFluidState(pos);
					if(!fluid.isEmpty()){
						structure.add(new MultiblockPiece(x, y, z, fluid, reverse));
						amount++;
						continue;
					}
				}

				structure.add(new MultiblockPiece(x, y, z, stack, reverse));
			}else
				structure.add(new MultiblockPiece(x, y, z, reverse));
		}

		structure.sort(reverse ? MultiblockPiece.ZX_COMPARE : MultiblockPiece.XZ_COMPARE);

		StringBuilder builder = new StringBuilder();

		if(shouldWrapInTable)
			builder.append("{| class=\"wikitable mw-collapsible mw-collapsed\"\n|-\n! Structure\n|-\n| ");

		builder.append("{{Cg/Multiblock/Alt\n");

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

		if(shouldWrapInTable)
			builder.append("\n|}");

		Utils.copyString(builder.toString());
		source.sendFeedback(new TranslationTextComponent("commands.dumpstructure.success", amount), true);
		return structure.size();
	}

	private static class MultiblockPiece {
		final int x, y, z;
		ItemStack stack;
		IFluidState fluid;
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
			this.fluid = null;
		}

		MultiblockPiece(int x, int y, int z, IFluidState fluid, boolean reverse){
			this(x, y, z, reverse);
			this.fluid = fluid;
		}

		static final Comparator<MultiblockPiece> XZ_COMPARE = Comparator.comparingInt((MultiblockPiece p) -> p.y).thenComparingInt((MultiblockPiece p) -> p.x).thenComparingInt((MultiblockPiece p) -> p.z);

		static final Comparator<MultiblockPiece> ZX_COMPARE = Comparator.comparingInt((MultiblockPiece p) -> p.y).thenComparingInt((MultiblockPiece p) -> p.z).thenComparingInt((MultiblockPiece p) -> p.x);


		public boolean isEmpty(){
			return stack.isEmpty() && fluid == null;
		}

		@Override
		public String toString(){
			StringBuilder builder = new StringBuilder("|").append(Utils.getAlphabetLetter(y));

			if(reverse)
				builder.append(Utils.getAlphabetLetter(z)).append(x);
			else
				builder.append(Utils.getAlphabetLetter(x)).append(z);

			builder.append('=');

			if(!stack.isEmpty())
				builder.append(Utils.outputItem(stack));
			else if(fluid != null)
				builder.append(outputFluid(fluid));

			return builder.toString();
		}
	}

	private static String outputFluid(IFluidState fluid){
		Block block = fluid.getBlockState().getBlock();
		return "{{Gc|mod=" + Utils.getModAbbrevation(block) + "|dis=false|" + ForgeI18n.parseMessage(block.getTranslationKey()) + "}}";
	}
}
