package xbony2.huesodewiki.recipe.recipes;

import static xbony2.huesodewiki.Utils.outputItem;
import static xbony2.huesodewiki.Utils.outputIngredient;
import static xbony2.huesodewiki.Utils.outputItemOutput;
import static xbony2.huesodewiki.Utils.outputOreDictionaryEntry;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.OreIngredient;
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
		List<IRecipe> recipes = new ArrayList<>();
		
		CraftingManager.REGISTRY.forEach((recipe) -> {
			if(recipe.getRecipeOutput().isItemEqual(itemstack))
				recipes.add(recipe);
		});
		
		if(!recipes.isEmpty())
			for(Iterator<IRecipe> iterator = recipes.iterator(); iterator.hasNext();){
				IRecipe recipe = iterator.next();
				
				if(recipe instanceof ShapedRecipes){
					ShapedRecipes shapedrecipe = (ShapedRecipes)recipe;
					ret.append("{{Cg/Crafting Table\n");
					
					int maxHeight = shapedrecipe.recipeHeight;
					int maxWidth = shapedrecipe.recipeWidth;
					
					for(int h = 1; h <= maxHeight; h++){
						for(int w = 1; w <= maxWidth; w++){
							Ingredient component = Ingredient.EMPTY;
							
							switch(h){
							case 1:
								component = shapedrecipe.recipeItems.get(w - 1);
								break;
							case 2:
								component = shapedrecipe.recipeItems.get(maxWidth + (w - 1));
								break;
							case 3:
								component = shapedrecipe.recipeItems.get((maxWidth * 2) + (w - 1));
								break;
							}
							
							if(component != Ingredient.EMPTY)
								if(!(component instanceof OreIngredient)) //Forge injects Vanilla
									ret.append('|').append(getShapedLocation(h, w)).append('=').append(outputIngredient(component)).append('\n');
								else{
									String entry = outputOreDictionaryEntry(component.getMatchingStacks());
								
									if(entry != null)
										ret.append('|').append(getShapedLocation(h, w)).append('=').append(entry).append('\n');
							}
						}
					}
					
					ret.append("|O=").append(outputItemOutput(shapedrecipe.getRecipeOutput())).append('\n');
					ret.append("}}").append('\n');
					
					if(iterator.hasNext())
						ret.append('\n');
				}else if(recipe instanceof ShapedOreRecipe){
					ShapedOreRecipe shapedrecipe = (ShapedOreRecipe)recipe;
					ret.append("{{Cg/Crafting Table\n");
					
					int maxHeight = shapedrecipe.getRecipeHeight();
					int maxWidth = shapedrecipe.getRecipeWidth();
					
					for(int h = 1; h <= maxHeight; h++){
						for(int w = 1; w <= maxWidth; w++){
							Ingredient component = Ingredient.EMPTY;
							
							switch(h){
							case 1:
								component = shapedrecipe.getIngredients().get(w - 1);
								
								break;
							case 2:
								component = shapedrecipe.getIngredients().get(maxWidth + (w - 1));
								break;
							case 3:
								component = shapedrecipe.getIngredients().get((maxWidth * 2) + (w - 1));
								break;
							}
							
							if(component != Ingredient.EMPTY)
								if(!(component instanceof OreIngredient))
									ret.append('|').append(getShapedLocation(h, w)).append('=').append(outputIngredient(component)).append('\n');
								else{
									String entry = outputOreDictionaryEntry(component.getMatchingStacks());
								
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
					
					List<Ingredient> recipeItems = shapelessrecipe.recipeItems;
					
					for(int i = 0; i < recipeItems.size(); i++){
						Ingredient component = recipeItems.get(i);
						
						if(component != Ingredient.EMPTY)
							if(!(component instanceof OreIngredient))
								ret.append('|').append(getShapelessLocation(i, recipeItems.size())).append('=').append(outputIngredient(component)).append('\n');
							else{
								String entry = outputOreDictionaryEntry(component.getMatchingStacks());
								
								if(entry != null)
									ret.append('|').append(getShapelessLocation(i, recipeItems.size())).append('=').append(entry).append('\n');
							}
					}
					
					ret.append("|O=").append(outputItemOutput(shapelessrecipe.getRecipeOutput())).append('\n');
					ret.append("|shapeless=true\n");
					ret.append("}}\n");
					
					if(iterator.hasNext())
						ret.append('\n');
				}else if(recipe instanceof ShapelessOreRecipe){
					ShapelessOreRecipe shapelessrecipe = (ShapelessOreRecipe)recipe;
					ret.append("{{Cg/Crafting Table\n");
					
					List<Ingredient> recipeItems = shapelessrecipe.getIngredients();
					
					for(int i = 0; i < recipeItems.size(); i++){
						Ingredient component = recipeItems.get(i);
						
						if(component != Ingredient.EMPTY)
							if(!(component instanceof OreIngredient))
								ret.append('|').append(getShapelessLocation(i, recipeItems.size())).append('=').append(outputIngredient(component)).append('\n');
							else{
								String entry = outputOreDictionaryEntry(component.getMatchingStacks());
							
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
		return ret.toString();
	}
}
