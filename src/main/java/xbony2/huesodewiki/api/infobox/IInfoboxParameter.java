package xbony2.huesodewiki.api.infobox;

import net.minecraft.item.ItemStack;

public interface IInfoboxParameter {
	public boolean canAdd(ItemStack itemstack);
	
	public String parameterName();
	
	public String parameterText(ItemStack itemstack);
}
