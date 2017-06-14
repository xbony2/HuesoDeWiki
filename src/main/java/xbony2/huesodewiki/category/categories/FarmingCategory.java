package xbony2.huesodewiki.category.categories;

import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import xbony2.huesodewiki.api.ICategory;

/**
 * Just supports Hoes.
 *
 */
public class FarmingCategory implements ICategory {

	@Override
	public boolean canAdd(ItemStack itemstack){
		return itemstack.getItem() instanceof ItemHoe;
	}

	@Override
	public String getCategoryName(ItemStack itemstack){
		return "Farming";
	}
}
