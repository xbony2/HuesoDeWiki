package xbony2.huesodewiki.api;

import net.minecraft.item.ItemStack;

/**
 * This is used to declare conditional prefixes to pages. These come before the infobox.
 */
public interface IPagePrefix {
	String IMC_NAME = "page_prefix";

	/**
	 * @param itemstack The ItemStack having its page constructed.
	 * @return Whether the prefix should be generated and placed on the page.
	 */
	boolean canAdd(ItemStack itemstack);

	/**
	 * @param itemstack The ItemStack having its page constructed.
	 * @return The string to prefix the page with.
	 */
	String getText(ItemStack itemstack);
}
