package xbony2.huesodewiki.category.categories;

import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import xbony2.huesodewiki.api.ICategory;

public class MiningCategory implements ICategory {

	@Override
	public boolean canAdd(ItemStack itemstack){
		return itemstack.getItem() instanceof ItemPickaxe || itemstack.getItem() instanceof ItemSpade;
	}

	@Override
	public String getCategoryName(ItemStack itemstack){
		return "Mining";
	}
}
