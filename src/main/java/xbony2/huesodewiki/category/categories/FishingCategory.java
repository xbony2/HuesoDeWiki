package xbony2.huesodewiki.category.categories;

import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemStack;
import xbony2.huesodewiki.api.ICategory;

public class FishingCategory implements ICategory {

	@Override
	public boolean canAdd(ItemStack itemstack){
		return itemstack.getItem() instanceof ItemFishingRod;
	}

	@Override
	public String getCategoryName(ItemStack itemstack){
		return "Fishing";
	}
}
