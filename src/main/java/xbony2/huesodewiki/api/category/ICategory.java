package xbony2.huesodewiki.api.category;

import net.minecraft.world.item.ItemStack;

public interface ICategory {
	String IMC_NAME = "category";

	boolean canAdd(ItemStack itemstack);

	String getCategoryName(ItemStack itemstack);
}
