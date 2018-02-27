package xbony2.huesodewiki.prefix;

import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.StringUtils;
import xbony2.huesodewiki.api.IPagePrefix;

public class CorrectTitlePrefix implements IPagePrefix {
	/**
	 * As defined at <a href="https://en.wikipedia.org/wiki/Wikipedia:Naming_conventions_(technical_restrictions)#Forbidden_characters">Wikipedia:Naming conventions (technical restrictions)#Forbidden characters</a>.
	 */
	private static final char[] INVALID_CHARS = { '#', '<', '>', '[', ']', '|', '{', '}' };

	@Override
	public boolean canAdd(ItemStack itemstack){
		return StringUtils.containsAny(itemstack.getDisplayName(), INVALID_CHARS);
	}

	@Override
	public String getText(ItemStack itemstack){
		return String.format("{{Correct title|%s}}", itemstack.getDisplayName());
	}
}