package xbony2.huesodewiki.prefix;

import net.minecraft.item.ItemStack;
import xbony2.huesodewiki.api.IPagePrefix;

public class LowercaseTitlePrefix implements IPagePrefix {
	@Override
	public boolean canAdd(ItemStack itemstack){
		String firstLetter = itemstack.getDisplayName().substring(0, 1);
		return firstLetter.equals(firstLetter.toLowerCase());
	}

	@Override
	public String getText(ItemStack itemstack){
		return "{{Lowercase title}}";
	}
}
