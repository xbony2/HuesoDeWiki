package xbony2.huesodewiki.category.categories;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import xbony2.huesodewiki.api.ICategory;

public class MeleeWeaponsCategory implements ICategory {

	@Override
	public boolean canAdd(ItemStack itemstack){
		return itemstack.getItem() instanceof ItemSword;
	}

	@Override
	public String getCategoryName(ItemStack itemstack){
		return "Melee weapons";
	}
}
