package xbony2.huesodewiki.prefix;

import net.minecraft.item.ItemStack;
import xbony2.huesodewiki.api.IPagePrefix;

public class LowercaseTitlePrefix implements IPagePrefix {
	@Override
	public boolean canAdd(ItemStack itemstack){
		return Character.isLowerCase(itemstack.getDisplayName().getUnformattedComponentText().charAt(0));
	}

	@Override
	public String getText(ItemStack itemstack){
		return "{{Lowercase title}}";
	}
}
