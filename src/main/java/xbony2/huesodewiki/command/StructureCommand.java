package xbony2.huesodewiki.command;

import java.util.*;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fmllegacy.ForgeI18n;
import xbony2.huesodewiki.Utils;

import static com.mojang.brigadier.arguments.BoolArgumentType.getBool;
import static net.minecraft.commands.arguments.coordinates.BlockPosArgument.getLoadedBlockPos;

public class StructureCommand {
	private static final String START_POS = "startPos";
	private static final String END_POS = "endPos";
	private static final String SHOULD_COMPACT = "shouldCompact";
	private static final String SHOULD_WRAP_IN_TABLE = "shouldWrapInTable";
	private static final String PADDING_MODE = "paddingMode";

	private static final SimpleCommandExceptionType STRUCTURE_TOO_LARGE = new SimpleCommandExceptionType(new TranslatableComponent("commands.huesodewiki.dumpstructure.tooLarge"));

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher){
		dispatcher.register(Commands.literal("dumpstructure")
				.requires(s -> FMLEnvironment.dist.isClient() && s.getEntity() instanceof ServerPlayer) //TODO make it work on servers, but not with /execute
				.then(Commands.argument(START_POS, BlockPosArgument.blockPos())
						.then(Commands.argument(END_POS, BlockPosArgument.blockPos())
								.executes(
										ctx -> execute(ctx.getSource(), getLoadedBlockPos(ctx, START_POS),
												getLoadedBlockPos(ctx, END_POS), Padding.CENTER,
												false, false))
								.then(Commands.argument(SHOULD_COMPACT, BoolArgumentType.bool())
										.executes(
												ctx -> execute(ctx.getSource(), getLoadedBlockPos(ctx, START_POS),
														getLoadedBlockPos(ctx, END_POS), Padding.CENTER,
														getBool(ctx, SHOULD_COMPACT), false))
										.then(Commands.argument(SHOULD_WRAP_IN_TABLE, BoolArgumentType.bool())
												.executes(
														ctx -> execute(ctx.getSource(), getLoadedBlockPos(ctx, START_POS),
																getLoadedBlockPos(ctx, END_POS), Padding.CENTER,
																getBool(ctx, SHOULD_COMPACT), getBool(ctx, SHOULD_WRAP_IN_TABLE)))
												.then(Commands.argument(PADDING_MODE, StringArgumentType.word())
														.suggests((context, builder) -> SharedSuggestionProvider.suggest(Arrays.stream(Padding.values())
																.map(Enum::name).map(s -> s.toLowerCase(Locale.ROOT)), builder))
														.executes(
																ctx -> execute(ctx.getSource(), getLoadedBlockPos(ctx, START_POS),
																		getLoadedBlockPos(ctx, END_POS), Padding.parse(ctx, PADDING_MODE),
																		getBool(ctx, SHOULD_COMPACT), getBool(ctx, SHOULD_WRAP_IN_TABLE))))
										))))
		);
	}

	private static int execute(CommandSourceStack source, BlockPos start, BlockPos end, Padding mode, boolean shouldCompact, boolean shouldWrapInTable) throws CommandSyntaxException{

		Level world = source.getLevel();
		ServerPlayer player = source.getPlayerOrException();

		int sizeX = Math.abs(start.getX() - end.getX());
		int sizeZ = Math.abs(start.getZ() - end.getZ());

		if(sizeX >= 26 || sizeZ >= 26) //template supports up to 26x26 layers
			throw STRUCTURE_TOO_LARGE.create();

		if(start.getY() < end.getY()){
			int y = end.getY() - start.getY();
			start = start.above(y);
			end = end.below(y);
		}

		List<MultiblockPiece> structure = new ArrayList<>();

		boolean reverse = Integer.signum(start.getX() - end.getX()) * Integer.signum(start.getZ() - end.getZ()) == 1;
		int paddingSizeEnd = Math.abs(sizeX - sizeZ);
		int paddingSizeStart = 0;

		BlockPos startPadded = start, endPadded = end;

		if(mode == Padding.BACK){
			paddingSizeStart = paddingSizeEnd;
			paddingSizeEnd = 0;
		}else if(mode == Padding.CENTER){
			paddingSizeStart = paddingSizeEnd / 2 + paddingSizeEnd % 2;
			paddingSizeEnd = paddingSizeEnd / 2;
		}

		if(sizeX > sizeZ){
			endPadded = end.offset(0, 0, start.getZ() > end.getZ() ? -paddingSizeEnd : paddingSizeEnd);
			startPadded = start.offset(0, 0, start.getZ() > end.getZ() ? paddingSizeStart : -paddingSizeStart);
		}else if(sizeZ > sizeX){
			endPadded = end.offset(start.getX() > end.getX() ? -paddingSizeEnd : paddingSizeEnd, 0, 0);
			startPadded = start.offset(start.getX() > end.getX() ? paddingSizeStart : -paddingSizeStart, 0, 0);
		}

		int minX = Math.min(start.getX(), end.getX());
		int minZ = Math.min(start.getZ(), end.getZ());
		int maxX = Math.max(start.getX(), end.getX());
		int maxZ = Math.max(start.getZ(), end.getZ());

		BlockPos startPoint = startPadded;
		int amount = 0;
		for(BlockPos pos : BlockPos.betweenClosed(startPadded, endPadded)){
			int x = startPoint.getX() - pos.getX();
			int y = startPoint.getY() - pos.getY();
			int z = startPoint.getZ() - pos.getZ();

			if(pos.getX() >= minX && pos.getX() <= maxX && pos.getZ() >= minZ && pos.getZ() <= maxZ){
				BlockState state = world.getBlockState(pos);

				HitResult ray = new BlockHitResult(new Vec3(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5), Direction.UP, pos, true);
				ItemStack stack = state.getBlock().getPickBlock(state, ray, world, pos, player);

				if(!stack.isEmpty())
					amount++;
				else{
					FluidState fluid = world.getFluidState(pos);
					
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

		builder.append("{{Cg/Multiblock\n");

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
		source.sendSuccess(new TranslatableComponent("commands.huesodewiki.dumpstructure.success", amount), true);
		return structure.size();
	}

	private enum Padding {
		CENTER, BACK, FRONT;

		private static final DynamicCommandExceptionType INVALID_ENUM = new DynamicCommandExceptionType(
				obj -> new TranslatableComponent("commands.huesodewiki.argument.enum.invalid", obj));

		static Padding parse(CommandContext<CommandSourceStack> context, String argName) throws CommandSyntaxException{
			String argument = context.getArgument(argName, String.class);
			try {
				return valueOf(argument.toUpperCase(Locale.ROOT));
			}catch(IllegalArgumentException e){
				throw INVALID_ENUM.create(argument);
			}
		}
	}

	private static class MultiblockPiece {
		final int x, y, z;
		ItemStack stack;
		FluidState fluid;
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

		MultiblockPiece(int x, int y, int z, FluidState fluid, boolean reverse){
			this(x, y, z, reverse);
			this.fluid = fluid;
		}

		static final Comparator<MultiblockPiece> XZ_COMPARE = Comparator.comparingInt((MultiblockPiece p) -> p.y).thenComparingInt((MultiblockPiece p) -> p.x).thenComparingInt((MultiblockPiece p) -> p.z);

		static final Comparator<MultiblockPiece> ZX_COMPARE = Comparator.comparingInt((MultiblockPiece p) -> p.y).thenComparingInt((MultiblockPiece p) -> p.z).thenComparingInt((MultiblockPiece p) -> p.x);


		boolean isEmpty(){
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

	private static String outputFluid(FluidState fluid){
		Block block = fluid.createLegacyBlock().getBlock();
		return "{{Gc|mod=" + Utils.getModAbbrevation(block) + "|dis=false|" + ForgeI18n.parseMessage(block.getDescriptionId()) + "}}";
	}
}
