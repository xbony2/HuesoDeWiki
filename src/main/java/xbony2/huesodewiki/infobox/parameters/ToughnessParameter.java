package xbony2.huesodewiki.infobox.parameters;

import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import xbony2.huesodewiki.api.infobox.IInfoboxParameter;

public class ToughnessParameter implements IInfoboxParameter {

	@Override
	public boolean canAdd(ItemStack itemstack){
		return itemstack.getItem() instanceof ItemArmor && ((ItemArmor)itemstack.getItem()).toughness > 0.0;
	}

	@Override
	public String getParameterName(){
		return "toughness";
	}

	@Override
	public String getParameterText(ItemStack itemstack){
		return Float.toString(((ItemArmor)itemstack.getItem()).toughness);
	}
}
