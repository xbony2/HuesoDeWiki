package xbony2.huesodewiki.command;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.forgespi.language.IModInfo;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryManager;
import net.minecraftforge.registries.tags.ITag;
import net.minecraftforge.registries.tags.ITagManager;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import xbony2.huesodewiki.HuesoDeWiki;

import static com.mojang.brigadier.arguments.StringArgumentType.getString;

public class TagDumpCommand {
	private static final SimpleCommandExceptionType WRITE_FAILED = new SimpleCommandExceptionType(Component.translatable("commands.huesodewiki.dumptags.write_failed"));

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher){
		dispatcher.register(Commands.literal("dumptags")
				.requires(s -> FMLEnvironment.dist.isClient() && s.getEntity() instanceof ServerPlayer) // TODO make it work on servers, but not with /execute
				.then(Commands.argument("modAbbrv", StringArgumentType.word())
						.then(Commands.argument("modid", StringArgumentType.word())
								.suggests((context, builder) -> SharedSuggestionProvider.suggest(ModList.get().getMods().stream().map(IModInfo::getModId), builder))
								.executes(ctx -> execute(ctx.getSource(), getString(ctx, "modAbbrv"), getString(ctx, "modid")))))
		);
	}

	@SuppressWarnings("resource")
	private static int execute(CommandSourceStack source, String modAbbrv, String modid) throws CommandSyntaxException {
		File output = new File(Minecraft.getInstance().gameDirectory, modAbbrv + ".txt");
		
		try(FileWriter writer = new FileWriter(output)){
			for(Item item : ForgeRegistries.ITEMS){
				ResourceLocation rl = ForgeRegistries.ITEMS.getKey(item);
				
				if(rl == null || !rl.getNamespace().equals(modid))
					continue;

				String displayName = item.getName(item.getDefaultInstance()).getString();

				System.out.println("rl: " + rl + ", dn: " + displayName);

				//var s = RegistryManager.ACTIVE.getRegistry(rl).tags();
				//if (s != null) s.getTagNames().forEach(System.out::println);

				// TODO: i just want this to compile rn lol -bony
				/*for(ResourceLocation tag : item.getTags())
					writer.append(tag.toString()).append("!").append(displayName).append("!").append(modAbbrv).append("!\n");*/
			}

			// i hate the boilerplate bullshit but IntelliJ is being a real drama queen about it
			for(ITag<Item> tag : ForgeRegistries.ITEMS.tags()) {
				//System.out.println(x.getKey().toString() + " - " + x);
				/*List<Item> matchingItems = tag
						.stream()
						.filter(item -> ForgeRegistries.ITEMS.getKey(item).getNamespace().equals(modid))
						.collect(Collectors.toList());*/

				/*List<Item> matchingItems2 = tag
						.stream()
						.map(item -> new ImmutablePair<>(ForgeRegistries.ITEMS.getKey(item), item))
						.filter(p -> p.right != null)
						.filter(p -> p.left.getNamespace().equals(modid))
						.map(rl -> rl.g)*/
				List<Item> matchingItems = tag
						.stream()
						.filter(item -> {
							ResourceLocation rl = ForgeRegistries.ITEMS.getKey(item);
							return rl != null && rl.getNamespace().equals(modid);
						})
						.collect(Collectors.toList());

				matchingItems.forEach(item -> {
					String displayName = item.getName(item.getDefaultInstance()).getString();
					System.out.println(displayName + " - " + tag.getKey().location());
				});

			}
		}catch(IOException e){
			HuesoDeWiki.LOGGER.error("Failed to write tag dump file {}", output.getName(), e);
			throw WRITE_FAILED.create();
		}

		source.sendSuccess(Component.translatable("commands.huesodewiki.dumptags.success", modid, modAbbrv), true);
		return 1;
	}
}
