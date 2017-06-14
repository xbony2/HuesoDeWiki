package xbony2.huesodewiki.category.categories;

import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import xbony2.huesodewiki.api.ICategory;

public class RangedWeaponsCategory implements ICategory {

	@Override
	public boolean canAdd(ItemStack itemstack){
		return itemstack.getItem() instanceof ItemBow;
	}

	@Override
	public String getCategoryName(ItemStack itemstack){
		return "Ranged weapons";
	}
}
