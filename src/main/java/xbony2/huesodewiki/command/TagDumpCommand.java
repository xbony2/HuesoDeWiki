package xbony2.huesodewiki.command;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.ForgeRegistries;
import xbony2.huesodewiki.HuesoDeWiki;

import static com.mojang.brigadier.arguments.StringArgumentType.getString;

public class TagDumpCommand {
	private static final SimpleCommandExceptionType WRITE_FAILED = new SimpleCommandExceptionType(new TranslationTextComponent("commands.huesodewiki.dumptags.write_failed"));

	public static void register(CommandDispatcher<CommandSource> dispatcher){
		dispatcher.register(Commands.literal("dumptags")
				.requires(s -> FMLEnvironment.dist.isClient() && s.getEntity() instanceof ServerPlayerEntity) //TODO make it work on servers, but not with /execute
				.then(Commands.argument("modAbbrv", StringArgumentType.word())
						.then(Commands.argument("modid", StringArgumentType.word())
								.executes(ctx -> execute(ctx.getSource(), getString(ctx, "modAbbrv"), getString(ctx, "modid")))))
		);
	}

	private static int execute(CommandSource source, String modAbbrv, String modid) throws CommandSyntaxException{
		File output = new File(Minecraft.getInstance().gameDir, modAbbrv + ".txt");
		try(FileWriter writer = new FileWriter(output)){

			for(Item item : ForgeRegistries.ITEMS){
				ResourceLocation rl = item.getRegistryName();
				if(rl == null || !rl.getNamespace().equals(modid))
					continue;

				String displayName = item.getDisplayName(item.getDefaultInstance()).getString();
				for(ResourceLocation tag : item.getTags())
					writer.append(tag.toString()).append("!").append(displayName).append("!").append(modAbbrv).append("!\n");
			}

		}catch(IOException e){
			HuesoDeWiki.LOGGER.error("Failed to write tag dump file {}", output.getName(), e);
			throw WRITE_FAILED.create();
		}

		source.sendFeedback(new TranslationTextComponent("commands.huesodewiki.dumptags.success", modid, modAbbrv), true);
		return 1;
	}


}
