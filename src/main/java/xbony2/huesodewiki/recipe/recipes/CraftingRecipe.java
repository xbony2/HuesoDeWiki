package xbony2.huesodewiki.recipe.recipes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.crafting.IShapedRecipe;
import net.minecraftforge.oredict.OreIngredient;
import xbony2.huesodewiki.Utils;
import xbony2.huesodewiki.api.IWikiRecipe;

public class CraftingRecipe implements IWikiRecipe {

	@Override
	public String getRecipes(ItemStack itemstack){
		StringBuilder ret = new StringBuilder();
		List<IRecipe> recipes = gatherRecipes(itemstack);

		if(recipes.isEmpty())
			return "";

		for(Iterator<IRecipe> iterator = recipes.iterator(); iterator.hasNext(); ){
			IRecipe recipe = iterator.next();
			ret.append(outputRecipe(recipe));
			if(iterator.hasNext())
				ret.append('\n');
		}

		return ret.toString();
	}

	public List<IRecipe> gatherRecipes(ItemStack itemstack){
		List<IRecipe> recipes = new ArrayList<>();

		CraftingManager.REGISTRY.forEach((recipe) -> {
			if(recipe.getRecipeOutput().isItemEqual(itemstack))
				recipes.add(recipe);
		});
		return recipes;
	}

	public String outputRecipe(IRecipe recipe){
		StringBuilder ret = new StringBuilder("{{Cg/Crafting Table\n");
		NonNullList<Ingredient> ingredients = recipe.getIngredients();

		boolean shapeless = false;
		int width = getWidth(recipe);

		if(!(recipe instanceof IShapedRecipe))
			shapeless = true;

		for(int i = 0; i < ingredients.size(); i++){
			Ingredient ingredient = ingredients.get(i);
			if(ingredient == Ingredient.EMPTY || ingredient.getMatchingStacks().length == 0)
				continue;

			ret.append('|').append(Utils.getAlphabetLetter(i % width + 1)).append(i / width + 1).append('=');

			if(ingredient instanceof OreIngredient)
				ret.append(Utils.outputOreDictionaryEntry(ingredient.getMatchingStacks()));
			else
				ret.append(Utils.outputIngredient(ingredient));

			ret.append('\n');
		}

		ret.append("|O=").append(Utils.outputItemOutput(recipe.getRecipeOutput())).append('\n');

		if(shapeless){
			ret.append("|shapeless=true\n");
		}

		return ret.append("}}\n").toString();
	}

	protected int getWidth(IRecipe recipe){
		if(recipe instanceof IShapedRecipe)
			return ((IShapedRecipe) recipe).getRecipeWidth();

		return recipe.getIngredients().size() > 6 ? 3 : 2;
	}
}
