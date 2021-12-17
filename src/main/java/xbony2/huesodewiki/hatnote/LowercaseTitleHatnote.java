package xbony2.huesodewiki.hatnote;

import net.minecraft.world.item.ItemStack;
import xbony2.huesodewiki.api.IHatnote;

public class LowercaseTitleHatnote implements IHatnote {
	@Override
	public boolean canAdd(ItemStack itemstack){
		return Character.isLowerCase(itemstack.getHoverName().getString().charAt(0));
	}

	@Override
	public String getText(ItemStack itemstack){
		return "{{Lowercase title}}";
	}
}
