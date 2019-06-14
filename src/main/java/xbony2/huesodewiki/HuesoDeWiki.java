package xbony2.huesodewiki;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;
import xbony2.huesodewiki.api.HuesoDeWikiAPI;
import xbony2.huesodewiki.api.IPagePrefix;
import xbony2.huesodewiki.api.IWikiRecipe;
import xbony2.huesodewiki.api.category.ICategory;
import xbony2.huesodewiki.api.infobox.IInfoboxParameter;
import xbony2.huesodewiki.api.infobox.type.IType;
import xbony2.huesodewiki.category.CategoryCreator;
import xbony2.huesodewiki.config.Config;
import xbony2.huesodewiki.infobox.InfoboxCreator;
import xbony2.huesodewiki.prefix.PrefixCreator;
import xbony2.huesodewiki.recipe.RecipeCreator;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

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
		bus.addListener(this::processIMC);

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

	private void processIMC(InterModProcessEvent event){
		processIMCStream(event.getIMCStream(IWikiRecipe.IMC_NAME::equals), IWikiRecipe.class, HuesoDeWikiAPI.recipes);
		processIMCStream(event.getIMCStream(IPagePrefix.IMC_NAME::equals), IPagePrefix.class, HuesoDeWikiAPI.prefixes);
		processIMCStream(event.getIMCStream(IInfoboxParameter.IMC_NAME::equals), IInfoboxParameter.class, HuesoDeWikiAPI.parameters);
		processIMCStream(event.getIMCStream(ICategory.IMC_NAME::equals), ICategory.class, HuesoDeWikiAPI.categories);
		processIMCStream(event.getIMCStream(IType.IMC_NAME::equals), IType.class, HuesoDeWikiAPI.types);
	}

	@SuppressWarnings("unchecked")
	private <T> void processIMCStream(Stream<InterModComms.IMCMessage> imcs, Class<T> validClass, List<T> targetList){
		imcs.map(InterModComms.IMCMessage::getMessageSupplier).map(Supplier::get).filter(validClass::isInstance).forEach(t -> targetList.add((T) t));
	}

	/*
	@EventHandler TODO commands, because client commands dont exist for now and also brigadier
	public void postInit(FMLPostInitializationEvent event){
		ClientCommandHandler.instance.registerCommand(new CommandDumpStructure());
	}
	*/
}
