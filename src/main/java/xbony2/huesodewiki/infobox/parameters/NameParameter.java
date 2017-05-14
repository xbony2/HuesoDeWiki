package xbony2.huesodewiki.infobox.parameters;

import net.minecraft.item.ItemStack;
import xbony2.huesodewiki.api.infobox.IInfoboxParameter;

public class NameParameter implements IInfoboxParameter {

	@Override
	public boolean canAdd(ItemStack itemstack){
		return true;
	}

	@Override
	public String parameterName(){
		return "name";
	}

	@Override
	public String parameterText(ItemStack itemstack){
		return itemstack.getDisplayName();
	}
}
