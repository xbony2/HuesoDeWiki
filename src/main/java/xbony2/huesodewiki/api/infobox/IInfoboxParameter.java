package xbony2.huesodewiki.api.infobox;

import net.minecraft.item.ItemStack;

public interface IInfoboxParameter {
	public boolean canAdd(ItemStack itemstack);
	
	public String getParameterName();
	
	public String getParameterText(ItemStack itemstack);
}
