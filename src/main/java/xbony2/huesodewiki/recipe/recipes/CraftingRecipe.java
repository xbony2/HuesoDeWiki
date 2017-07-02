package xbony2.huesodewiki.recipe.recipes;

import static xbony2.huesodewiki.Utils.outputItem;
import static xbony2.huesodewiki.Utils.outputItemOutput;
import static xbony2.huesodewiki.Utils.outputOreDictionaryEntry;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import xbony2.huesodewiki.api.IWikiRecipe;

public class CraftingRecipe implements IWikiRecipe {
	public static String getShapedLocation(int height, int width){
		return ((char)(width + 64)) + "" + height;
	}
	
	public static String getShapelessLocation(int index, int max){
		if(max > 6)
			switch(index){
			case 0:
				return "A1";
			case 1:
				return "B1";
			case 2:
				return "C1";
			case 3:
				return "A2";
			case 4:
				return "B2";
			case 5:
				return "C2";
			case 6:
				return "A3";
			case 7:
				return "B3";
			case 8:
				return "C3";
			}
		else
			switch(index){
			case 0:
				return "A1";
			case 1:
				return "B1";
			case 2:
				return "A2";
			case 3:
				return "B2";
			case 4:
				return "A3";
			case 5:
				return "B3";
			}
		return null;
	}

	@Override
	public String getRecipes(ItemStack itemstack){
		StringBuilder ret = new StringBuilder();
		List<IRecipe> recipes = new ArrayList<IRecipe>();
		
		for(Iterator<IRecipe> iterator = CraftingManager.REGISTRY.iterator(); iterator.hasNext();){
			
			IRecipe recipe = iterator.next();
			
			if(recipe.getRecipeOutput().isItemEqual(itemstack))
				recipes.add(recipe);
		}
		
		if(!recipes.isEmpty()){
			for(Iterator<IRecipe> iterator = recipes.iterator(); iterator.hasNext();){
				IRecipe recipe = iterator.next();
				
				if(recipe instanceof ShapedRecipes){
					ShapedRecipes shapedrecipe = (ShapedRecipes)recipe;
					ret.append("{{Cg/Crafting Table\n");
					
					int maxHeight = shapedrecipe.recipeHeight;
					int maxWidth = shapedrecipe.recipeWidth;
					
					for(int h = 1; h <= maxHeight; h++){
						for(int w = 1; w <= maxWidth; w++){
							ItemStack component = null;
							
							switch(h){
							case 1:
								component = shapedrecipe.recipeItems.get(w - 1).getMatchingStacks()[0];
								break;
							case 2:
								component = shapedrecipe.recipeItems.get(maxWidth + (w - 1)).getMatchingStacks()[0];
								break;
							case 3:
								component = shapedrecipe.recipeItems.get((maxWidth * 2) + (w - 1)).getMatchingStacks()[0];
								break;
							}
							
							if(component.isEmpty())
								continue;
							
							ret.append('|').append(getShapedLocation(h, w)).append('=').append(outputItem(component)).append('\n');
						}
					}
					
					ret.append("|O=").append(outputItemOutput(shapedrecipe.getRecipeOutput())).append('\n');
					ret.append("}}").append('\n');
					
					if(iterator.hasNext())
						ret.append('\n');
				}else if(recipe instanceof ShapedOreRecipe){
					ShapedOreRecipe shapedrecipe = (ShapedOreRecipe)recipe;
					ret.append("{{Cg/Crafting Table\n");
					
					int maxHeight = shapedrecipe.getHeight();
					int maxWidth = shapedrecipe.getWidth();
					
					for(int h = 1; h <= maxHeight; h++){
						for(int w = 1; w <= maxWidth; w++){
							Object component = null;
							
							switch(h){
							case 1:
								component = shapedrecipe.getIngredients().get(w - 1).getMatchingStacks()[0];
								
								break;
							case 2:
								component = shapedrecipe.getIngredients().get(maxWidth + (w - 1)).getMatchingStacks()[0];
								break;
							case 3:
								component = shapedrecipe.getIngredients().get((maxWidth * 2) + (w - 1)).getMatchingStacks()[0];
								break;
							}
							
							if(component == null)
								continue;
							
							if(component instanceof ItemStack && !((ItemStack)component).isEmpty())
								ret.append('|').append(getShapedLocation(h, w)).append('=').append(outputItem((ItemStack)component)).append('\n');
							else if(component instanceof List && !((List)component).isEmpty()){ //For recipes that contain ore dictionary entries that aren't registered, this won't work. But I don't care enough to fix it...
								String entry = outputOreDictionaryEntry((List)component);
								
								if(entry != null)
									ret.append('|').append(getShapedLocation(h, w)).append('=').append(entry).append('\n');
							}
						}
					}
					
					ret.append("|O=").append(outputItemOutput(shapedrecipe.getRecipeOutput())).append('\n');
					ret.append("}}\n");
					
					if(iterator.hasNext())
						ret.append('\n');
				}else if(recipe instanceof ShapelessRecipes){
					ShapelessRecipes shapelessrecipe = (ShapelessRecipes)recipe;
					ret.append("{{Cg/Crafting Table\n");
					
					List<ItemStack> recipeItems = shapelessrecipe.recipeItems;
					
					for(int i = 0; i < recipeItems.size(); i++){
						ItemStack component = recipeItems.get(i);
						
						if(!component.isEmpty())
							ret.append('|').append(getShapelessLocation(i, recipeItems.size())).append('=').append(outputItem(component)).append('\n');
					}
					
					ret.append("|O=").append(outputItemOutput(shapelessrecipe.getRecipeOutput())).append('\n');
					ret.append("|shapeless=true\n");
					ret.append("}}\n");
					
					if(iterator.hasNext())
						ret.append('\n');
				}else if(recipe instanceof ShapelessOreRecipe){
					ShapelessOreRecipe shapelessrecipe = (ShapelessOreRecipe)recipe;
					ret.append("{{Cg/Crafting Table\n");
					
					List<Object> recipeItems = shapelessrecipe.getInput();
					
					for(int i = 0; i < recipeItems.size(); i++){
						Object object = recipeItems.get(i);
						
						if(object == null)
							continue;
						
						if(object instanceof ItemStack && !((ItemStack)object).isEmpty())
							ret.append('|').append(getShapelessLocation(i, recipeItems.size())).append('=').append(outputItem((ItemStack)object)).append('\n');
						else if(object instanceof List && !((List)object).isEmpty()){
							String entry = outputOreDictionaryEntry((List)object);
							
							if(entry != null)
								ret.append('|').append(getShapelessLocation(i, recipeItems.size())).append('=').append(entry).append('\n');
						}
					}
					
					ret.append("|O=").append(outputItemOutput(shapelessrecipe.getRecipeOutput())).append('\n');
					ret.append("|shapeless=true\n");
					ret.append("}}\n");
					
					if(iterator.hasNext())
						ret.append('\n');
				}
			}
		}
		return ret.toString();
	}
}
