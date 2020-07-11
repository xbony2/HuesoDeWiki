package xbony2.huesodewiki.infobox.parameters;

import net.minecraft.item.ItemStack;
import xbony2.huesodewiki.Utils;
import xbony2.huesodewiki.api.infobox.IInfoboxParameter;

public class ModParameter implements IInfoboxParameter {

	@Override
	public boolean canAdd(ItemStack itemstack){
		return true;
	}

	@Override
	public String getParameterName(){
		return "mod";
	}

	@Override
	public String getParameterText(ItemStack itemstack){
		return Utils.getModName(itemstack);
	}
}
