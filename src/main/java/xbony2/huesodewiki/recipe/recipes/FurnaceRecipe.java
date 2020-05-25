package xbony2.huesodewiki.recipe.recipes;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import xbony2.huesodewiki.HuesoDeWiki;
import xbony2.huesodewiki.Utils;
import xbony2.huesodewiki.api.IWikiRecipe;

public class FurnaceRecipe implements IWikiRecipe {

	@SuppressWarnings({"unchecked", "resource"})
	@Override
	public String getRecipes(ItemStack itemstack){
		List<ItemStack> inputs = new ArrayList<>();
		Map<ResourceLocation, IRecipe<?>> recipes;
		try {
			recipes = (Map<ResourceLocation, IRecipe<?>>) CraftingRecipe.getRecipes.invoke(Minecraft.getInstance().world.getRecipeManager(), IRecipeType.SMELTING);
		}catch(IllegalAccessException | InvocationTargetException e){
			HuesoDeWiki.LOGGER.error("Exception getting furnace recipe map", e);
			return "<!--Furnace recipes errored, see console log for details-->";
		}

		recipes.forEach((rl, recipe) -> {
			ItemStack output = recipe.getRecipeOutput();
			
			if(output.isItemEqual(itemstack))
				Collections.addAll(inputs, recipe.getIngredients().get(0).getMatchingStacks());
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
