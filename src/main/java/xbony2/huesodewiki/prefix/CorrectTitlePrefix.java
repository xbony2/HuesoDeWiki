package xbony2.huesodewiki.prefix;

import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.StringUtils;
import xbony2.huesodewiki.api.IPagePrefix;

public class CorrectTitlePrefix implements IPagePrefix {
	
	@Override
	public boolean canAdd(ItemStack itemstack){
		return StringUtils.containsAny(itemstack.getDisplayName(), {'#', '<', '>', '[', ']', '|', '{', '}'}); // see: https://en.wikipedia.org/wiki/Wikipedia:NCHASHTAG
	}

	@Override
	public String getText(ItemStack itemstack){
		return "{{Correct title|", itemstack.getDisplayName() + "}}");
	}
}
