package xbony2.huesodewiki.infobox.parameters;

import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import xbony2.huesodewiki.Utils;
import xbony2.huesodewiki.api.infobox.IInfoboxParameter;

public class HungerParameter implements IInfoboxParameter {

	@Override
	public boolean canAdd(ItemStack itemstack){
		return itemstack.getItem() instanceof ItemFood;
	}

	@Override
	public String getParameterName(){
		return "hunger";
	}

	@Override
	public String getParameterText(ItemStack itemstack){
		ItemFood food = (ItemFood)itemstack.getItem();
		return "{{Shanks|" + Integer.toString(food.getHealAmount(itemstack)) + "|" + Utils.floatToString(food.getSaturationModifier(itemstack)) + "}}";
	}
}
