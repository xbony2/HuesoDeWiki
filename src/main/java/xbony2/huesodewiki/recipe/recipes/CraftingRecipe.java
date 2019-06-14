package xbony2.huesodewiki.recipe.recipes;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.IShapedRecipe;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import xbony2.huesodewiki.HuesoDeWiki;
import xbony2.huesodewiki.Utils;
import xbony2.huesodewiki.api.IWikiRecipe;

public class CraftingRecipe implements IWikiRecipe {

	// RecipeManager#getRecipes(IRecipeType), TODO check if forge ATs this away later, or possibly AT this on our own
	static final Method getRecipes = ObfuscationReflectionHelper.findMethod(RecipeManager.class, "func_215366_a", IRecipeType.class);

	@Override
	public String getRecipes(ItemStack itemstack){
		StringBuilder ret = new StringBuilder();
		List<IRecipe<?>> recipes = gatherRecipes(itemstack);

		if(recipes.isEmpty())
			return "";

		for(Iterator<IRecipe<?>> iterator = recipes.iterator(); iterator.hasNext(); ){
			IRecipe recipe = iterator.next();
			ret.append(outputRecipe(recipe));
			if(iterator.hasNext())
				ret.append('\n');
		}

		return ret.toString();
	}

	@SuppressWarnings("unchecked")
	public List<IRecipe<?>> gatherRecipes(ItemStack itemstack){
		List<IRecipe<?>> recipes = new ArrayList<>();
		Map<ResourceLocation, IRecipe<?>> recipeMap;
		try {
			recipeMap = (Map<ResourceLocation, IRecipe<?>>) CraftingRecipe.getRecipes.invoke(Minecraft.getInstance().world.getRecipeManager(), IRecipeType.CRAFTING);
		}catch(IllegalAccessException | InvocationTargetException e){
			HuesoDeWiki.LOGGER.error("Exception getting crafting recipe map", e);
			return Collections.emptyList();
		}

		recipeMap.forEach((rl, recipe) -> {
			if(recipe.getRecipeOutput().isItemEqual(itemstack))
				recipes.add(recipe);
		});
		return recipes;
	}

	public String outputRecipe(IRecipe<?> recipe){
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

//			if(ingredient instanceof OreIngredient)
//				ret.append(Utils.outputOreDictionaryEntry(ingredient.getMatchingStacks()));
//			else
			ret.append(Utils.outputIngredient(ingredient));

			ret.append('\n');
		}

		ret.append("|O=").append(Utils.outputItemOutput(recipe.getRecipeOutput())).append('\n');

		if(shapeless)
			ret.append("|shapeless=true\n");

		return ret.append("}}\n").toString();
	}

	protected int getWidth(IRecipe recipe){
		if(recipe instanceof IShapedRecipe)
			return ((IShapedRecipe) recipe).getRecipeWidth();

		return recipe.getIngredients().size() > 6 ? 3 : 2;
	}
}
