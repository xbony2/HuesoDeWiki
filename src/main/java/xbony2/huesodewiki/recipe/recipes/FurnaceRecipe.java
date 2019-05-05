package xbony2.huesodewiki.recipe.recipes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.crafting.VanillaRecipeTypes;
import xbony2.huesodewiki.Utils;
import xbony2.huesodewiki.api.IWikiRecipe;

public class FurnaceRecipe implements IWikiRecipe {
	@Override
	public String getRecipes(ItemStack itemstack){
		List<ItemStack> inputs = new ArrayList<>();

		Minecraft.getInstance().world.getRecipeManager().getRecipes(VanillaRecipeTypes.SMELTING).forEach((recipe) -> {
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
