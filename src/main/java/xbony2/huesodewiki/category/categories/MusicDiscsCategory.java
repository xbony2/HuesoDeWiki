package xbony2.huesodewiki.category.categories;

import net.minecraft.item.ItemRecord;
import net.minecraft.item.ItemStack;
import xbony2.huesodewiki.api.ICategory;

public class MusicDiscsCategory implements ICategory {

	@Override
	public boolean canAdd(ItemStack itemstack){
		return itemstack.getItem() instanceof ItemRecord;
	}

	@Override
	public String getCategoryName(ItemStack itemstack){
		return "Music Discs";
	}
}
