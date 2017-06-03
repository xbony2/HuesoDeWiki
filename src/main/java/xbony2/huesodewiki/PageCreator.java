package xbony2.huesodewiki;

import static xbony2.huesodewiki.Utils.getModName;
import static xbony2.huesodewiki.Utils.getShapedLocation;
import static xbony2.huesodewiki.Utils.getShapelessLocation;
import static xbony2.huesodewiki.Utils.outputItem;
import static xbony2.huesodewiki.Utils.outputItemOutput;
import static xbony2.huesodewiki.Utils.outputOreDictionaryEntry;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import xbony2.huesodewiki.category.CategoryCreator;
import xbony2.huesodewiki.infobox.InfoboxCreator;

public class PageCreator {
	public static void createPage(ItemStack itemstack){
		String name = itemstack.getDisplayName();
		String modName = getModName(itemstack);
		
		String linkFix = HuesoDeWiki.linkCorrections.get(modName); //is null if there isn't a change required.
		String type = itemstack.getItem() instanceof ItemBlock ? "block" : "item";
		
		//And now for the magic
		String page = InfoboxCreator.createInfobox(itemstack);
		
		page += "\n";
		page += "The '''" + name + "''' is " + (type == "block" ? "a block" : "an item") + " added by [[" + (linkFix != null ? linkFix + "|" : "") + modName + "]]." + "\n";
		
		List<IRecipe> recipes = new ArrayList<IRecipe>();
		
		for(Iterator<IRecipe> iterator = CraftingManager.getInstance().getRecipeList().iterator(); iterator.hasNext();){
			IRecipe recipe = iterator.next();
			
			if(recipe.getRecipeOutput().isItemEqual(itemstack))
				recipes.add(recipe);
		}
		
		if(!recipes.isEmpty()){
			page += "\n";
			page += (HuesoDeWiki.use2SpaceStyle ? "== Recipe ==" : "==Recipe==") + "\n";
			
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
							
							if(component.isEmpty())
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
					
					int maxHeight = shapedrecipe.getHeight();
					int maxWidth = shapedrecipe.getWidth();
					
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
							
							if(component instanceof ItemStack && !((ItemStack)component).isEmpty())
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
						
						if(!component.isEmpty())
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
						
						if(object instanceof ItemStack && !((ItemStack)object).isEmpty())
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
		page += CategoryCreator.createCategories(itemstack) + "\n";
		page += "\n";
		page += "<languages />" + "\n";
		page += "\n";
		
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(page), null);
	}
}
