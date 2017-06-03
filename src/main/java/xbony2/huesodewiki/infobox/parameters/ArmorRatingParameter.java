package xbony2.huesodewiki.infobox.parameters;

import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import xbony2.huesodewiki.api.infobox.IInfoboxParameter;

public class ArmorRatingParameter implements IInfoboxParameter {

	@Override
	public boolean canAdd(ItemStack itemstack){
		return itemstack.getItem() instanceof ItemArmor;
	}

	@Override
	public String getParameterName(){
		return "armorrating";
	}

	@Override
	public String getParameterText(ItemStack itemstack){
		return Integer.toString(((ItemArmor)itemstack.getItem()).damageReduceAmount);
	}
}
