package xbony2.huesodewiki.category.categories;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import xbony2.huesodewiki.api.category.ICategory;

public class LegwearCategory implements ICategory {

	@Override
	public boolean canAdd(ItemStack itemstack){
		return itemstack.getItem() instanceof ItemArmor && ((ItemArmor)itemstack.getItem()).armorType == EntityEquipmentSlot.LEGS;
	}

	@Override
	public String getCategoryName(ItemStack itemstack){
		return "Legwear";
	}
}
