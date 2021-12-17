package xbony2.huesodewiki.category;

import net.minecraft.world.item.ItemStack;
import xbony2.huesodewiki.Utils;
import xbony2.huesodewiki.api.category.ICategory;

public class ModCategory implements ICategory {

	@Override
	public boolean canAdd(ItemStack itemstack){
		return true;
	}

	@Override
	public String getCategoryName(ItemStack itemstack){
		return Utils.getModName(itemstack);
	}
}
