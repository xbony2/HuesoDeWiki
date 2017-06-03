package xbony2.huesodewiki.category.categories;

import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import xbony2.huesodewiki.api.ICategory;
import xbony2.huesodewiki.api.infobox.IType;

public class FoodCategory implements ICategory {

	@Override
	public boolean canAdd(ItemStack itemstack){
		return itemstack.getItem() instanceof ItemFood; //Maybe this should be separated into vegan/vegetarian/whatnot, but that code would be a bit complex so we'll just go with this
	}

	@Override
	public String getCategoryName(ItemStack itemstack){
		return "Food";
	}
}
