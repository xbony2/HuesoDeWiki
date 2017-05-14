package xbony2.huesodewiki.infobox.parameters;

import static xbony2.huesodewiki.Utils.getModName;

import net.minecraft.item.ItemStack;
import xbony2.huesodewiki.api.infobox.IInfoboxParameter;

public class ModParameter implements IInfoboxParameter {

	@Override
	public boolean canAdd(ItemStack itemstack){
		return true;
	}

	@Override
	public String parameterName(){
		return "mod";
	}

	@Override
	public String parameterText(ItemStack itemstack){
		return getModName(itemstack);
	}
}
