package xbony2.huesodewiki.recipe.recipes;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.IShapedRecipe;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import xbony2.huesodewiki.HuesoDeWiki;
import xbony2.huesodewiki.Utils;
import xbony2.huesodewiki.api.IWikiRecipe;

public class CraftingRecipe implements IWikiRecipe {

	// RecipeManager#getRecipes(IRecipeType), TODO check if forge ATs this away later, or possibly AT this on our own
	static final Method getRecipes = ObfuscationReflectionHelper.findMethod(RecipeManager.class, "byType", RecipeType.class);

	@SuppressWarnings("rawtypes")
	@Override
	public String getRecipes(ItemStack itemstack){
		StringBuilder ret = new StringBuilder();
		List<Recipe<?>> recipes = gatherRecipes(itemstack);

		if(recipes.isEmpty())
			return "";

		for(Iterator<Recipe<?>> iterator = recipes.iterator(); iterator.hasNext(); ){
			Recipe recipe = iterator.next();
			ret.append(outputRecipe(recipe));
			if(iterator.hasNext())
				ret.append('\n');
		}

		return ret.toString();
	}

	@SuppressWarnings({"unchecked", "resource"})
	public List<Recipe<?>> gatherRecipes(ItemStack itemstack){
		List<Recipe<?>> recipes = new ArrayList<>();
		Map<ResourceLocation, Recipe<?>> recipeMap;
		try {
			recipeMap = (Map<ResourceLocation, Recipe<?>>) getRecipes.invoke(Minecraft.getInstance().level.getRecipeManager(), RecipeType.CRAFTING);
		}catch(IllegalAccessException | InvocationTargetException e){
			HuesoDeWiki.LOGGER.error("Exception getting crafting recipe map", e);
			return Collections.emptyList();
		}

		recipeMap.forEach((rl, recipe) -> {
			if(recipe.getResultItem().sameItem(itemstack))
				recipes.add(recipe);
		});
		return recipes;
	}

	public String outputRecipe(Recipe<?> recipe){
		StringBuilder ret = new StringBuilder("{{Cg/Crafting Table\n");
		NonNullList<Ingredient> ingredients = recipe.getIngredients();

		boolean shapeless = false;
		int width = getWidth(recipe);

		if(!(recipe instanceof IShapedRecipe))
			shapeless = true;

		for(int i = 0; i < ingredients.size(); i++){
			Ingredient ingredient = ingredients.get(i);
			if(ingredient == Ingredient.EMPTY || ingredient.getItems().length == 0)
				continue;

			ret.append('|').append(Utils.getAlphabetLetter(i % width + 1)).append(i / width + 1).append('=');
			ret.append(Utils.outputIngredient(ingredient));

			ret.append('\n');
		}

		ret.append("|O=").append(Utils.outputItemOutput(recipe.getResultItem())).append('\n');

		if(shapeless)
			ret.append("|shapeless=true\n");

		return ret.append("}}\n").toString();
	}

	@SuppressWarnings("rawtypes")
	protected int getWidth(Recipe recipe){
		if(recipe instanceof IShapedRecipe shapedRecipe)
			return shapedRecipe.getRecipeWidth();

		return recipe.getIngredients().size() > 6 ? 3 : 2;
	}
}
