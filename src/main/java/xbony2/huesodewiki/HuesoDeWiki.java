package xbony2.huesodewiki;

import org.lwjgl.glfw.GLFW;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xbony2.huesodewiki.category.CategoryCreator;
import xbony2.huesodewiki.config.Config;
import xbony2.huesodewiki.infobox.InfoboxCreator;
import xbony2.huesodewiki.prefix.PrefixCreator;
import xbony2.huesodewiki.recipe.RecipeCreator;

@Mod(HuesoDeWiki.MODID)
public class HuesoDeWiki {
	public static final String MODID = "huesodewiki";
	public static final String VERSION = "@VERSION@";

	public static final Logger LOGGER = LogManager.getLogger(MODID);

	public static KeyBinding copyPageKey;
	public static KeyBinding copyNameKey;

	public HuesoDeWiki(){
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		bus.addListener(this::clientInit);

		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_SPEC);
		bus.addListener(Config::onConfigLoad);
		bus.addListener(Config::onConfigReload);
	}

	private void clientInit(FMLClientSetupEvent event){
		copyPageKey = new KeyBinding("key.copybasepage", GLFW.GLFW_KEY_SEMICOLON, "key.categories.huesodewiki");
		ClientRegistry.registerKeyBinding(copyPageKey);
		copyNameKey = new KeyBinding("key.copyname", GLFW.GLFW_KEY_APOSTROPHE, "key.categories.huesodewiki");
		ClientRegistry.registerKeyBinding(copyNameKey);

		InfoboxCreator.init();
		RecipeCreator.init();
		PrefixCreator.init();
		CategoryCreator.init();
		
//		Compat.preInit();
	}
	/*
	@EventHandler TODO commands, because client commands dont exist for now and also brigadier
	public void postInit(FMLPostInitializationEvent event){
		ClientCommandHandler.instance.registerCommand(new CommandDumpStructure());
	}
	*/
}
