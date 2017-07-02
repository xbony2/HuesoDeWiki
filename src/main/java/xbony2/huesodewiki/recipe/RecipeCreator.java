package xbony2.huesodewiki.recipe;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import xbony2.huesodewiki.HuesoDeWiki;
import xbony2.huesodewiki.api.IWikiRecipe;
import xbony2.huesodewiki.recipe.recipes.*;

public class RecipeCreator {
	public static List<IWikiRecipe> recipes = new ArrayList();
	
	static {
		recipes.add(new CraftingRecipe());
		recipes.add(new FurnaceRecipe());
	}
	
	public static String createRecipes(ItemStack itemstack){
		StringBuilder ret = new StringBuilder();
		for(IWikiRecipe recipe : recipes){
			String potentialRecipes = recipe.getRecipes(itemstack);
			if(potentialRecipes != null && potentialRecipes != "")
				ret.append(potentialRecipes);
		}
		
		if(ret.toString().length() < 1) //Sometimes there's a new line. I dunno, it's weird
			return "";
		return (HuesoDeWiki.use2SpaceStyle ? "== Recipe ==" : "==Recipe==") + "\n" + ret.toString();
	}
}
