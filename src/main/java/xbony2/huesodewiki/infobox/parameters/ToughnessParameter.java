package xbony2.huesodewiki.infobox.parameters;

import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import xbony2.huesodewiki.Utils;
import xbony2.huesodewiki.api.infobox.IInfoboxParameter;

public class ToughnessParameter implements IInfoboxParameter {

	@Override
	public boolean canAdd(ItemStack itemstack){
		return itemstack.getItem() instanceof ItemArmor && ((ItemArmor)itemstack.getItem()).getToughness() > 0.0;
	}

	@Override
	public String getParameterName(){
		return "toughness";
	}

	@Override
	public String getParameterText(ItemStack itemstack){
		return Utils.floatToString(((ItemArmor)itemstack.getItem()).getToughness());
	}
}
