package xbony2.huesodewiki.recipe.recipes;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import xbony2.huesodewiki.Utils;
import xbony2.huesodewiki.api.IWikiRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FurnaceRecipe implements IWikiRecipe {
	@Override
	public String getRecipes(ItemStack itemstack){
		List<ItemStack> inputs = new ArrayList<ItemStack>();
		for(Map.Entry<ItemStack, ItemStack> entry : FurnaceRecipes.instance().getSmeltingList().entrySet()){
			ItemStack output = entry.getValue();
			if(output.isItemEqual(itemstack))
				inputs.add(entry.getKey());
		}
		if(inputs.isEmpty())
			return null;

		StringBuilder recipeString = new StringBuilder("{{Cg/Furnace\n|I=");

		for(ItemStack input : inputs){
			recipeString.append(Utils.outputItem(input));
		}
		recipeString.append("\n");

		recipeString.append("|O=");
		recipeString.append(Utils.outputItemOutput(itemstack));
		recipeString.append("\n}}\n");
		return recipeString.toString();
	}
}
