package xbony2.huesodewiki.compat.ic2;

import ic2.api.item.IElectricItem;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import xbony2.huesodewiki.Utils;
import xbony2.huesodewiki.api.infobox.IInfoboxParameter;

public class EUStorageParameter implements IInfoboxParameter {

	@Override
	public boolean canAdd(ItemStack itemstack){
		if(itemstack.getItem() instanceof IElectricItem)
			return true;
		/*if(itemstack.getItem() instanceof ItemBlock && ((ItemBlock)itemstack.getItem()).getBlock())
			return true;*/
		return false;
	}

	@Override
	public String getParameterName(){
		return "eustorage";
	}

	@Override
	public String getParameterText(ItemStack itemstack){
		if(itemstack.getItem() instanceof IElectricItem)
			return Utils.doubleToString(((IElectricItem)itemstack.getItem()).getMaxCharge(itemstack));
		return "?";
	}
}
