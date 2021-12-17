package xbony2.huesodewiki.recipe;

import net.minecraft.world.item.ItemStack;
import xbony2.huesodewiki.api.HuesoDeWikiAPI;
import xbony2.huesodewiki.config.Config;
import xbony2.huesodewiki.recipe.recipes.CraftingRecipe;
import xbony2.huesodewiki.recipe.recipes.FurnaceRecipe;

public class RecipeCreator {
	public static void init(){
		HuesoDeWikiAPI.recipes.add(new CraftingRecipe());
		HuesoDeWikiAPI.recipes.add(new FurnaceRecipe());
	}
	
	public static String createRecipes(ItemStack itemstack){
		StringBuilder ret = new StringBuilder();
		
		HuesoDeWikiAPI.recipes.forEach((recipe) -> {
			String potentialRecipes = recipe.getRecipes(itemstack);
			
			if(potentialRecipes != null && !potentialRecipes.equals(""))
				ret.append(potentialRecipes);
		});
		
		if(ret.toString().length() < 1)
			return "";
		
		return (Config.use2SpaceStyle.get() ? "== Recipe ==" : "==Recipe==") + "\n" + ret.toString() + "\n";
	}
}
