package xbony2.huesodewiki.infobox.parameters;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import xbony2.huesodewiki.api.infobox.IInfoboxParameter;

public class FlammableParameter implements IInfoboxParameter {

	@Override
	public boolean canAdd(ItemStack itemstack){
		try {
			return itemstack.getItem() instanceof ItemBlock && ((ItemBlock)itemstack.getItem()).getBlock().isFlammable(null, null, null);
		}catch(Exception e){
			return false;
		}
	}

	@Override
	public String getParameterName(){
		return "flammable";
	}

	@Override
	public String getParameterText(ItemStack itemstack){
		return "Yes";
	}
}
