package xbony2.huesodewiki.api;

import net.minecraft.world.item.ItemStack;

public interface IWikiRecipe {
	String IMC_NAME = "recipe";

	/**
	 * @return null or "" if there are no recipes to add.
	 */
	String getRecipes(ItemStack itemstack);
}
