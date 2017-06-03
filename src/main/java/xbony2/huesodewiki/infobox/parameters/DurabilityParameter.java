package xbony2.huesodewiki.infobox.parameters;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import xbony2.huesodewiki.Utils;
import xbony2.huesodewiki.api.infobox.IInfoboxParameter;

public class DurabilityParameter implements IInfoboxParameter {

	@Override
	public boolean canAdd(ItemStack itemstack){
		return itemstack.getItem() instanceof ItemTool;
	}

	@Override
	public String getParameterName(){
		return "durability";
	}

	@Override
	public String getParameterText(ItemStack itemstack){
		return Utils.floatToString(((ItemTool)itemstack.getItem()).getMaxDamage(itemstack) + 1);
	}
}
