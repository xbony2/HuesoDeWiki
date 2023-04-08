package xbony2.huesodewiki.recipe.recipes;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.resources.ResourceLocation;
import xbony2.huesodewiki.HuesoDeWiki;
import xbony2.huesodewiki.Utils;
import xbony2.huesodewiki.api.IWikiRecipe;

public class FurnaceRecipe implements IWikiRecipe {

	@SuppressWarnings({"unchecked", "resource"})
	@Override
	public String getRecipes(ItemStack itemstack){
		List<ItemStack> inputs = new ArrayList<>();
		Map<ResourceLocation, Recipe<?>> recipes;
		try {
			recipes = (Map<ResourceLocation, Recipe<?>>) CraftingRecipe.getRecipes.invoke(Minecraft.getInstance().level.getRecipeManager(), RecipeType.SMELTING);
		}catch(IllegalAccessException | InvocationTargetException e){
			HuesoDeWiki.LOGGER.error("Exception getting furnace recipe map", e);
			return "<!--Furnace recipes errored, see console log for details-->";
		}

		recipes.forEach((rl, recipe) -> {
			ItemStack output = recipe.getResultItem(Minecraft.getInstance().level.registryAccess());
			
			if(output.sameItem(itemstack))
				Collections.addAll(inputs, recipe.getIngredients().get(0).getItems());
		});
		
		if(inputs.isEmpty())
			return null;

		StringBuilder ret = new StringBuilder("{{Cg/Furnace\n");
		ret.append("|I=");

		inputs.forEach((input) -> ret.append(Utils.outputItem(input)));
		
		ret.append('\n');

		ret.append("|O=").append(Utils.outputItemOutput(itemstack)).append('\n');
		ret.append("}}\n");
		return ret.toString();
	}
}
