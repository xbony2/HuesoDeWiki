package xbony2.huesodewiki.category.categories;

import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import xbony2.huesodewiki.api.ICategory;

public class FellingCategory implements ICategory {

	@Override
	public boolean canAdd(ItemStack itemstack){
		return itemstack.getItem() instanceof ItemAxe;
	}

	@Override
	public String getCategoryName(ItemStack itemstack){
		return "Felling";
	}
}
