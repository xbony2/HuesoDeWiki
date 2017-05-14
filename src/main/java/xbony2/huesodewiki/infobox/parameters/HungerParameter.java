package xbony2.huesodewiki.infobox.parameters;

import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import xbony2.huesodewiki.api.infobox.IInfoboxParameter;

public class HungerParameter implements IInfoboxParameter {

	@Override
	public boolean canAdd(ItemStack itemstack){
		return itemstack.getItem() instanceof ItemFood;
	}

	@Override
	public String parameterName(){
		return "hunger";
	}

	@Override
	public String parameterText(ItemStack itemstack){
		ItemFood food = (ItemFood)itemstack.getItem();
		return "{{Shanks|" + Integer.toString(food.getHealAmount(itemstack)) + "|" + Float.toString(food.getSaturationModifier(itemstack)) + "}}";
	}
}
