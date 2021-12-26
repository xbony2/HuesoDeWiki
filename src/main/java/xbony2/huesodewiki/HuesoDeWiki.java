package xbony2.huesodewiki;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

import net.minecraftforge.client.ClientRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import xbony2.huesodewiki.api.HuesoDeWikiAPI;
import xbony2.huesodewiki.api.IHatnote;
import xbony2.huesodewiki.api.IWikiRecipe;
import xbony2.huesodewiki.api.category.ICategory;
import xbony2.huesodewiki.api.infobox.IInfoboxParameter;
import xbony2.huesodewiki.api.infobox.type.IType;
import xbony2.huesodewiki.category.CategoryCreator;
import xbony2.huesodewiki.command.StructureCommand;
import xbony2.huesodewiki.command.TagDumpCommand;
import xbony2.huesodewiki.config.Config;
import xbony2.huesodewiki.hatnote.HatnoteCreator;
import xbony2.huesodewiki.infobox.InfoboxCreator;
import xbony2.huesodewiki.recipe.RecipeCreator;

@Mod(HuesoDeWiki.MODID)
public class HuesoDeWiki {
	public static final String MODID = "huesodewiki";

	public static final Logger LOGGER = LogManager.getLogger(MODID);

	public static KeyMapping copyPageKey;
	public static KeyMapping copyNameKey;

	public HuesoDeWiki(){
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		bus.addListener(this::clientInit);
		bus.addListener(this::processIMC);

		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_SPEC);
		bus.addListener(Config::onConfigLoad);
		bus.addListener(Config::onConfigReload);

		MinecraftForge.EVENT_BUS.addListener(this::registerCommands);
	}

	private void clientInit(FMLClientSetupEvent event){
		copyPageKey = new KeyMapping("key.huesodewiki.copybasepage", GLFW.GLFW_KEY_SEMICOLON, "key.categories.huesodewiki");
		ClientRegistry.registerKeyBinding(copyPageKey);
		copyNameKey = new KeyMapping("key.huesodewiki.copyname", GLFW.GLFW_KEY_APOSTROPHE, "key.categories.huesodewiki");
		ClientRegistry.registerKeyBinding(copyNameKey);

		InfoboxCreator.init();
		RecipeCreator.init();
		HatnoteCreator.init();
		CategoryCreator.init();
	}

	private void processIMC(InterModProcessEvent event){
		processIMCStream(event.getIMCStream(IWikiRecipe.IMC_NAME::equals), IWikiRecipe.class, HuesoDeWikiAPI.recipes);
		processIMCStream(event.getIMCStream(IHatnote.IMC_NAME::equals), IHatnote.class, HuesoDeWikiAPI.hatnotes);
		processIMCStream(event.getIMCStream(IInfoboxParameter.IMC_NAME::equals), IInfoboxParameter.class, HuesoDeWikiAPI.parameters);
		processIMCStream(event.getIMCStream(ICategory.IMC_NAME::equals), ICategory.class, HuesoDeWikiAPI.categories);
		processIMCStream(event.getIMCStream(IType.IMC_NAME::equals), IType.class, HuesoDeWikiAPI.types);
	}

	@SuppressWarnings("unchecked")
	private <T> void processIMCStream(Stream<InterModComms.IMCMessage> imcs, Class<T> validClass, List<T> targetList){
		imcs.map(InterModComms.IMCMessage::messageSupplier).map(Supplier::get).filter(validClass::isInstance).forEach(t -> targetList.add((T) t));
	}

	private void registerCommands(RegisterCommandsEvent event){
		StructureCommand.register(event.getDispatcher());
		TagDumpCommand.register(event.getDispatcher());
	}
}
