package xbony2.huesodewiki;

import java.io.File;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xbony2.huesodewiki.compat.Compat;
import xbony2.huesodewiki.command.CommandDumpStructure;
import xbony2.huesodewiki.config.Config;
import xbony2.huesodewiki.recipe.RecipeCreator;

@Mod(modid = HuesoDeWiki.MODID, version = HuesoDeWiki.VERSION, clientSideOnly = true, guiFactory = "xbony2.huesodewiki.config.HuesoGuiConfigFactory")
public class HuesoDeWiki {
	public static final String MODID = "huesodewiki";
	public static final String VERSION = "@VERSION@";
	
	public static final Logger LOGGER = LogManager.getLogger(MODID);
	
	public static KeyBinding copyPageKey;
	private boolean isCopyPageKeyDown = false;
	
	public static KeyBinding copyNameKey;
	private boolean isCopyNameKeyDown = false;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event){
		copyPageKey = new KeyBinding("key.copybasepage", Keyboard.KEY_SEMICOLON, "key.categories.huesodewiki");
		ClientRegistry.registerKeyBinding(copyPageKey);
		copyNameKey = new KeyBinding("key.copyname", Keyboard.KEY_APOSTROPHE, "key.categories.huesodewiki");
		ClientRegistry.registerKeyBinding(copyNameKey);
		MinecraftForge.EVENT_BUS.register(new RenderTickEventEventHanlder());

		Config.initConfig(new File(event.getModConfigurationDirectory(), "HuesoDeWiki.cfg"));

		Compat.preInit();
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event){
		ClientCommandHandler.instance.registerCommand(new CommandDumpStructure());
	}
	
	private class RenderTickEventEventHanlder {
		@SubscribeEvent
		public void renderTickEvent(RenderTickEvent event){
			if(event.phase == Phase.START){
				if(Keyboard.isKeyDown(copyPageKey.getKeyCode())){
					if(!isCopyPageKeyDown){
						isCopyPageKeyDown = true;
						ItemStack itemstack = Utils.getHoveredItemStack();
						if(!itemstack.isEmpty()){
							if(GuiScreen.isCtrlKeyDown()){
								Utils.copyString(RecipeCreator.createRecipes(itemstack));
								Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentTranslation("msg.copiedrecipe", itemstack.getDisplayName()));
							}else{
								Utils.copyString(PageCreator.createPage(itemstack));
								Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentTranslation("msg.copiedpage", itemstack.getDisplayName()));
							}
						}else
							return;
					}
				}else
					isCopyPageKeyDown = false;
				
				if(Keyboard.isKeyDown(copyNameKey.getKeyCode())){
					if(!isCopyNameKeyDown){
						isCopyNameKeyDown = true;
						ItemStack itemstack = Utils.getHoveredItemStack();
						if(!itemstack.isEmpty())
							Utils.copyString(itemstack.getDisplayName());
					}
				}else
					isCopyNameKeyDown = false;
			}
		}
	}
}
