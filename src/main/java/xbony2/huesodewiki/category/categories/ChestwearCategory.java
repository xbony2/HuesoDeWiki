package xbony2.huesodewiki.category.categories;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import xbony2.huesodewiki.api.ICategory;

public class ChestwearCategory implements ICategory {

	@Override
	public boolean canAdd(ItemStack itemstack){
		return itemstack.getItem() instanceof ItemArmor && ((ItemArmor)itemstack.getItem()).armorType == EntityEquipmentSlot.CHEST;
	}

	@Override
	public String getCategoryName(ItemStack itemstack){
		return "Chestwear";
	}
}
