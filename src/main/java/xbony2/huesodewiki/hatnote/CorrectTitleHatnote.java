package xbony2.huesodewiki.hatnote;

import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.StringUtils;
import xbony2.huesodewiki.api.IHatnote;

public class CorrectTitleHatnote implements IHatnote {
	
	@Override
	public boolean canAdd(ItemStack itemstack){
		// see: https://en.wikipedia.org/wiki/Wikipedia:NCHASHTAG
		return StringUtils.containsAny(itemstack.getDisplayName().getString(), '#', '<', '>', '[', ']', '|', '{', '}');
	}

	@Override
	public String getText(ItemStack itemstack){
		return "{{Correct title|" + itemstack.getDisplayName().getString() + "}}";
	}
}
