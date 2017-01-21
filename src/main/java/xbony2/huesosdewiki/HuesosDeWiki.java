package xbony2.huesosdewiki;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
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
import net.minecraft.item.crafting.ShapedRecipes;
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
	
	public static boolean use2SpaceStyle;
	
	public static Map<String, String> nameCorrections = new HashMap<String, String>();
	public static Map<String, String> linkCorrections = new HashMap<String, String>();
	
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
							if(!itemstack.isEmpty()){
								String name = itemstack.getDisplayName();
								String modName = Utils.getModName(itemstack);
								
								String linkFix = linkCorrections.get(modName); //is null if there isn't a change required.
								String blockOrItem = itemstack.getItem() instanceof ItemBlock ? "block" : "item";
								
								//And now for the magic
								String page = "{{Infobox" + "\n";
								page += "|name=" + name + "\n";
								page += "|imageicon={{Gc|mod=" + Utils.getModAbbrevation(modName) + "|link=none|" + name + "}}" + "\n";
								page += "|mod=" + modName + "\n";
								page += "|type=" + blockOrItem + "\n";
								page += "}}" + "\n";
								page += "\n";
								page += "The '''" + name + "''' is " + (blockOrItem == "block" ? "a block" : "an item") + " added by [[" + (linkFix != null ? linkFix + "|" : "") + modName + "]]." + "\n";
								
								List<IRecipe> recipes = new ArrayList<IRecipe>();
								
								for(Iterator<IRecipe> iterator = CraftingManager.getInstance().getRecipeList().iterator(); iterator.hasNext();){
									IRecipe recipe = iterator.next();
									
									if(recipe.getRecipeOutput().isItemEqual(itemstack))
										recipes.add(recipe);
								}
								
								if(!recipes.isEmpty()){
									page += "\n";
									page += (use2SpaceStyle ? "== Recipes ==" : "==Recipe==") + "\n";
									
									for(Iterator<IRecipe> iterator = recipes.iterator(); iterator.hasNext();){
										IRecipe recipe = iterator.next();
										
										if(recipe instanceof ShapedRecipes){
											ShapedRecipes shapedrecipe = (ShapedRecipes)recipe;
											page += "{{Cg/Crafting Table" + "\n";
											
											for(int h = 1; h <= shapedrecipe.recipeHeight; h++){
												for(int w = 1; w <= shapedrecipe.recipeWidth; w++){
													ItemStack itemstack2 = shapedrecipe.recipeItems[(h == 1 ? 0 : (h == 2 ? 3 : 6)) + w - 1]; //XXX: only supports 3x3 recipes (also still not sure if the position is right)
													
													if(itemstack2.isEmpty())
														continue;
													
													page += "|" + ((char)(w + 64)) + h + "={{Gc|mod=" + Utils.getModAbbrevation(itemstack2) + "|dis=false|" + itemstack2.getDisplayName() + "}}" + "\n";
												}
											}
											
											ItemStack output = shapedrecipe.getRecipeOutput();
											
											page += "|O={{Gc|mod=" + Utils.getModAbbrevation(output) + "|link=none|" + name + (output.getCount() != 1 ? "|" + output.getCount() : "") + "}}" + "\n";
											page += "}}" + "\n";
										}//TODO: shapeless and oredict
									}
								}
								
								page += "\n";
								page += "\n";
								page += "{{Navbox " + modName + "}}" + "\n";
								page += "\n";
								page += "[[Category:" + modName + "]]" + "\n";
								page += "\n";
								page += "<languages />" + "\n";
								page += "\n";
								
								Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(page), null);
							}
						}
					}
				}else
					isKeyDown = false;
		}
	}
}
