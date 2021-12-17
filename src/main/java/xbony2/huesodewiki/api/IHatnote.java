package xbony2.huesodewiki.api;

import net.minecraft.world.item.ItemStack;

/**
 * This is used to declare conditional topmost hatnote to articles, like <a href="https://ftb.gamepedia.com/Template:Correct_title">Template:Correct title</a>. These come before the infobox.
 */
public interface IHatnote {
	String IMC_NAME = "hatnote";

	/**
	 * @param itemstack The ItemStack having its page constructed.
	 * @return Whether the hatnote should be generated and placed on the page.
	 */
	boolean canAdd(ItemStack itemstack);

	/**
	 * @param itemstack The ItemStack having its page constructed.
	 * @return The string to hatnote the page with.
	 */
	String getText(ItemStack itemstack);
}
