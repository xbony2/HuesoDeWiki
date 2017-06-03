package xbony2.huesodewiki.api;

import net.minecraft.item.ItemStack;

public interface ICategory {
	public boolean canAdd(ItemStack itemstack);
	
	public String getCategoryName(ItemStack itemstack);
}
