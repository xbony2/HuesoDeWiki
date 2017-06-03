package xbony2.huesodewiki.infobox.parameters.types;

import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import xbony2.huesodewiki.api.infobox.IType;

public class FoodType implements IType {

	@Override
	public int getPriority(){
		return 10;
	}

	@Override
	public String getName(){
		return "food";
	}

	@Override
	public boolean isApplicable(ItemStack itemstack){
		return itemstack.getItem() instanceof ItemFood;
	}
}
