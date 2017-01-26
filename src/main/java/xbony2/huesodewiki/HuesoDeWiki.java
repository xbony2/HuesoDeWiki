package xbony2.huesodewiki;

import static xbony2.huesodewiki.Utils.*;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.lang.reflect.Field;
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
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

@Mod(modid = HuesoDeWiki.MODID, version = HuesoDeWiki.VERSION)
public class HuesoDeWiki {
	public static final String MODID = "huesodewiki";
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
		key = new KeyBinding("key.copybasepage", Keyboard.KEY_SEMICOLON, "key.categories.huesodewiki");
		ClientRegistry.registerKeyBinding(key);
		MinecraftForge.EVENT_BUS.register(new RenderTickEventEventHanlder());
		
		Configuration config = new Configuration(new File(event.getModConfigurationDirectory(), "HuesoDeWiki.cfg"));
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
						GuiScreen currentScreen = mc.currentScreen;
						
						if(currentScreen instanceof GuiContainer){
							Slot hovered = ((GuiContainer)currentScreen).getSlotUnderMouse();
							
							if(hovered == null)
								return;
							
							ItemStack itemstack = hovered.getStack();
							
							if(itemstack != null){
								String name = itemstack.getDisplayName();
								String modName = getModName(itemstack);
								
								String linkFix = linkCorrections.get(modName); //is null if there isn't a change required.
								String type = itemstack.getItem() instanceof ItemBlock ? "block" : "item";
								
								//And now for the magic
								String page = "{{Infobox" + "\n";
								page += "|name=" + name + "\n";
								page += "|imageicon=" + outputItemOutput(itemstack) + "\n";
								page += "|mod=" + modName + "\n";
								page += "|type=" + type + "\n";
								page += "}}" + "\n";
								page += "\n";
								page += "The '''" + name + "''' is " + (type == "block" ? "a block" : "an item") + " added by [[" + (linkFix != null ? linkFix + "|" : "") + modName + "]]." + "\n";
								
								List<IRecipe> recipes = new ArrayList<IRecipe>();
								
								for(Iterator<IRecipe> iterator = CraftingManager.getInstance().getRecipeList().iterator(); iterator.hasNext();){
									IRecipe recipe = iterator.next();
									
									if(recipe.getRecipeOutput() != null && recipe.getRecipeOutput().isItemEqual(itemstack))
										recipes.add(recipe);
								}
								
								if(!recipes.isEmpty()){
									page += "\n";
									page += (use2SpaceStyle ? "== Recipe ==" : "==Recipe==") + "\n";
									
									for(Iterator<IRecipe> iterator = recipes.iterator(); iterator.hasNext();){
										IRecipe recipe = iterator.next();
										
										if(recipe instanceof ShapedRecipes){
											ShapedRecipes shapedrecipe = (ShapedRecipes)recipe;
											page += "{{Cg/Crafting Table" + "\n";
											
											int maxHeight = shapedrecipe.recipeHeight;
											int maxWidth = shapedrecipe.recipeWidth;
											
											for(int h = 1; h <= maxHeight; h++){
												for(int w = 1; w <= maxWidth; w++){
													ItemStack component = null;
													
													switch(h){
													case 1:
														component = shapedrecipe.recipeItems[w - 1];
														break;
													case 2:
														component = shapedrecipe.recipeItems[maxWidth + (w - 1)];
														break;
													case 3:
														component = shapedrecipe.recipeItems[(maxWidth * 2) + (w - 1)];
														break;
													}
													
													if(component == null)
														continue;
													
													page += "|" + getShapedLocation(h, w) + "=" + outputItem(component) + "\n";
												}
											}
											
											page += "|O=" + outputItemOutput(shapedrecipe.getRecipeOutput()) + "\n";
											page += "}}" + "\n";
											
											if(iterator.hasNext())
												page += "\n";
										}else if(recipe instanceof ShapedOreRecipe){
											ShapedOreRecipe shapedrecipe = (ShapedOreRecipe)recipe;
											page += "{{Cg/Crafting Table" + "\n";
											
											int maxHeight = 0;
											int maxWidth = 0;
											
											try{
												Field heightField = ShapedOreRecipe.class.getDeclaredField("height");
												Field widthField = ShapedOreRecipe.class.getDeclaredField("width");
												
												heightField.setAccessible(true);
												widthField.setAccessible(true);
												
												maxHeight = heightField.getInt(shapedrecipe);
												maxWidth = widthField.getInt(shapedrecipe);
											}catch(Exception e){ //¯\_(ツ)_/¯
												e.printStackTrace();
											}
											
											for(int h = 1; h <= maxHeight; h++){
												for(int w = 1; w <= maxWidth; w++){
													Object component = null;
													
													switch(h){
													case 1:
														component = shapedrecipe.getInput()[w - 1];
														break;
													case 2:
														component = shapedrecipe.getInput()[maxWidth + (w - 1)];
														break;
													case 3:
														component = shapedrecipe.getInput()[(maxWidth * 2) + (w - 1)];
														break;
													}
													
													if(component == null)
														continue;
													
													if(component instanceof ItemStack && ((ItemStack)component) != null)
														page += "|" + getShapedLocation(h, w) + "=" + outputItem((ItemStack)component) + "\n";
													else if(component instanceof List && !((List)component).isEmpty()){ //For recipes that contain ore dictionary entries that aren't registered, this won't work. But I don't care enough to fix it...
														String entry = outputOreDictionaryEntry((List)component);
														
														if(entry != null)
															page += "|" + getShapedLocation(h, w) + "=" + entry + "\n";
													}
												}
											}
											
											page += "|O=" + outputItemOutput(shapedrecipe.getRecipeOutput()) + "\n";
											page += "}}" + "\n";
											
											if(iterator.hasNext())
												page += "\n";
										}else if(recipe instanceof ShapelessRecipes){
											ShapelessRecipes shapelessrecipe = (ShapelessRecipes)recipe;
											page += "{{Cg/Crafting Table" + "\n";
											
											List<ItemStack> recipeItems = shapelessrecipe.recipeItems;
											
											for(int i = 0; i < recipeItems.size(); i++){
												ItemStack component = recipeItems.get(i);
												
												if(component != null)
													page += "|" + getShapelessLocation(i, recipeItems.size()) + "=" + outputItem(component) + "\n";
											}
											
											page += "|O=" + outputItemOutput(shapelessrecipe.getRecipeOutput()) + "\n";
											page += "|shapeless=true" + "\n";
											page += "}}" + "\n";
											
											if(iterator.hasNext())
												page += "\n";
										}else if(recipe instanceof ShapelessOreRecipe){
											ShapelessOreRecipe shapelessrecipe = (ShapelessOreRecipe)recipe;
											page += "{{Cg/Crafting Table" + "\n";
											
											List<Object> recipeItems = shapelessrecipe.getInput();
											
											for(int i = 0; i < recipeItems.size(); i++){
												Object object = recipeItems.get(i);
												
												if(object == null)
													continue;
												
												if(object instanceof ItemStack && ((ItemStack)object) != null)
													page += "|" + getShapelessLocation(i, recipeItems.size()) + "=" + outputItem((ItemStack)object) + "\n";
												else if(object instanceof List && !((List)object).isEmpty()){
													String entry = outputOreDictionaryEntry((List)object);
													
													if(entry != null)
														page += "|" + getShapelessLocation(i, recipeItems.size()) + "=" + entry + "\n";
												}
											}
											
											page += "|O=" + outputItemOutput(shapelessrecipe.getRecipeOutput()) + "\n";
											page += "|shapeless=true" + "\n";
											page += "}}" + "\n";
											
											if(iterator.hasNext())
												page += "\n";
										}
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
