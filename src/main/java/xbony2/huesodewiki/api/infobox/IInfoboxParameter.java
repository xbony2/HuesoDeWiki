package xbony2.huesodewiki.api.infobox;

import net.minecraft.item.ItemStack;

public interface IInfoboxParameter {
	boolean canAdd(ItemStack itemstack);
	
	String getParameterName();
	
	String getParameterText(ItemStack itemstack);
}
