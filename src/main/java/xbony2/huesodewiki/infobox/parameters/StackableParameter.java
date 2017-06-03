package xbony2.huesodewiki.infobox.parameters;

import net.minecraft.item.ItemStack;
import xbony2.huesodewiki.api.infobox.IInfoboxParameter;

public class StackableParameter implements IInfoboxParameter {

	@Override
	public boolean canAdd(ItemStack itemstack){
		return itemstack.getMaxStackSize() != 64;
	}

	@Override
	public String getParameterName(){
		return "stackable";
	}

	@Override
	public String getParameterText(ItemStack itemstack){
		int stacksize = itemstack.getMaxStackSize();
		return stacksize == 1 ? "No" : "Yes (" + stacksize + ")";
	}
}
