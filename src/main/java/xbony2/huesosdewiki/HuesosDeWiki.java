package xbony2.huesosdewiki;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;

@Mod(modid = HuesosDeWiki.MODID, version = HuesosDeWiki.VERSION)
public class HuesosDeWiki {
	public static final String MODID = "huesosdewiki";
	public static final String VERSION = "1.0.0a";
	
	public static KeyBinding key;
	private boolean isKeyDown = false;
	
	public boolean use2SpaceStyle;
	
	public Map<String, String> nameCorrections = new HashMap<String, String>();
	public Map<String, String> linkCorrections = new HashMap<String, String>();
	
	public static final String[] DEFAULT_NAME_CORRECTIONS = new String[]{"Iron Chest", "Iron Chests"};
	public static final String[] DEFAULT_LINK_CORRECTIONS = new String[]{"Roots", "Roots (Mod)", "Esteemed Innovation", "Esteemed Innovation (Mod)"};

	@EventHandler
	public void preInit(FMLPreInitializationEvent event){
		key = new KeyBinding("key.copybasepage", Keyboard.KEY_SEMICOLON, "key.categories.huesosdewiki");
		ClientRegistry.registerKeyBinding(key);
		MinecraftForge.EVENT_BUS.register(new RenderTickEventEventHanlder());
		
		Configuration config = new Configuration(new File(event.getModConfigurationDirectory(), "HuesosDeWiki.cfg"));
		config.load();
		use2SpaceStyle = config.getBoolean("Use2SpaceStyle", "Main", false, "Use \"2spacestyle\"- put an extra space in headers (like \"== Recipe ==\", as vs \"==Recipe==\").");
		String[] nameCorrections = config.getStringList("NameCorrections", "Main", DEFAULT_NAME_CORRECTIONS, "Name fixes. Is a map- first entry is the mod's internal name, second is the FTB Wiki's name.");
		String[] linkCorrections = config.getStringList("LinkCorrections", "Main", DEFAULT_LINK_CORRECTIONS, "Link fixes. Is a map- first entry is the mod's name, second is the FTB Wiki's page.");
		
		for(int i = 0; i < nameCorrections.length - 1; i += 2)
			this.nameCorrections.put(nameCorrections[i], nameCorrections[i + 1]);
		
		for(int i = 0; i < linkCorrections.length - 1; i += 2)
			this.nameCorrections.put(linkCorrections[i], linkCorrections[i + 1]);
		
		config.save();
	}
	
	private class RenderTickEventEventHanlder {
		@SubscribeEvent
		public void renderTickEvent(RenderTickEvent event){
			if(event.phase == Phase.START)
				if(Keyboard.isKeyDown(key.getKeyCode())){
					if(!isKeyDown){
						isKeyDown = true;
						Minecraft mc = Minecraft.getMinecraft();
						Slot hovered = null;
						GuiScreen currentScreen = mc.currentScreen;
						if(currentScreen instanceof GuiContainer)
							hovered = ((GuiContainer)currentScreen).getSlotUnderMouse();
						
						if(hovered != null){
							ItemStack itemstack = hovered.getStack();
							if(itemstack != null){
								String name = itemstack.getDisplayName();
								String modName;
								
								ModContainer container = Loader.instance().getIndexedModList().get(Item.REGISTRY.getNameForObject(hovered.getStack().getItem()).getResourceDomain());
								
								if(container == null)
									modName = "Vanilla";
								else{
									modName = container.getName();
									
									if(nameCorrections.get(modName) != null)
										modName = nameCorrections.get(modName);
								}
								
								String linkFix = linkCorrections.get(modName); //is null if there isn't a change required.
								String blockOrItem = itemstack.getItem() instanceof ItemBlock ? "block" : "item";
								
								//And now for the magic
								String page = "{{Infobox" + "\n";
								page += "|name=" + name + "\n";
								page += "|imageicon={{Gc|mod={{subst:#invoke:Mods|getAbbrv|" + modName + "}}" + "\n";
								page += "|mod=" + modName + "\n";
								page += "|type=" + blockOrItem + "\n";
								page += "}}" + "\n";
								page += "\n";
								page += "The '''" + name + "''' is a " + blockOrItem + " added by [[" + (linkFix != null ? linkFix + "|" : "") + modName + "]]." + "\n";
								
								List<IRecipe> recipes = new ArrayList<IRecipe>();
								
								for(Iterator<IRecipe> interator = CraftingManager.getInstance().getRecipeList().iterator(); interator.hasNext();){
									IRecipe recipe = interator.next();
									
									if(recipe.getRecipeOutput().isItemEqual(itemstack))
										recipes.add(recipe);
								}
								
								if(!recipes.isEmpty()){
									page += "\n";
									page += (use2SpaceStyle ? "== Recipes ==" : "Recipe") + "\n";
								}
							}
						}
					}
				}else
					isKeyDown = false;
		}
	}
}
