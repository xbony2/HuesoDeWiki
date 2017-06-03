package xbony2.huesodewiki.infobox.parameters;

import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import xbony2.huesodewiki.api.infobox.IInfoboxParameter;

public class FoodPointsParameter implements IInfoboxParameter {

	@Override
	public boolean canAdd(ItemStack itemstack){
		return itemstack.getItem() instanceof ItemFood;
	}

	@Override
	public String getParameterName(){
		return "foodpoints";
	}

	@Override
	public String getParameterText(ItemStack itemstack){
		return Integer.toString(((ItemFood)itemstack.getItem()).getHealAmount(itemstack));
	}
}
