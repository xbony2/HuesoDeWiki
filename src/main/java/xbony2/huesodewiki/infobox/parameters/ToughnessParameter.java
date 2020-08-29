package xbony2.huesodewiki.infobox.parameters;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import xbony2.huesodewiki.Utils;
import xbony2.huesodewiki.api.infobox.IInfoboxParameter;

public class ToughnessParameter implements IInfoboxParameter {

	@Override
	public boolean canAdd(ItemStack itemstack){
		return itemstack.getItem() instanceof ArmorItem && ((ArmorItem) itemstack.getItem()).getArmorMaterial().getToughness() > 0.0;
	}

	@Override
	public String getParameterName(){
		return "toughness";
	}

	@Override
	public String getParameterText(ItemStack itemstack){
		return Utils.floatToString(((ArmorItem) itemstack.getItem()).getArmorMaterial().getToughness());
	}
}
