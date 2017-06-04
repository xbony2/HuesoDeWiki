package xbony2.huesodewiki.api;

import net.minecraft.item.ItemStack;

public interface IWikiRecipe {
	/**
	 * 
	 * @param itemstack
	 * @return null or "" if there are no recipes to add.
	 */
	public String getRecipes(ItemStack itemstack);
}
