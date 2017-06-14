package xbony2.huesodewiki.api.category;

import net.minecraft.item.ItemStack;

public interface ICategory {
	boolean canAdd(ItemStack itemstack);
	
	String getCategoryName(ItemStack itemstack);
}
