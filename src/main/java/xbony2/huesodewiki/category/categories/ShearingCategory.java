package xbony2.huesodewiki.category.categories;

import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import xbony2.huesodewiki.api.ICategory;

public class ShearingCategory implements ICategory {

	@Override
	public boolean canAdd(ItemStack itemstack){
		return itemstack.getItem() instanceof ItemShears;
	}

	@Override
	public String getCategoryName(ItemStack itemstack){
		return "Shearing";
	}
}
