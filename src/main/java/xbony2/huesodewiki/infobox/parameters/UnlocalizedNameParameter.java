package xbony2.huesodewiki.infobox.parameters;

import net.minecraft.item.ItemStack;
import xbony2.huesodewiki.api.infobox.IInfoboxParameter;

public class UnlocalizedNameParameter implements IInfoboxParameter {

	@Override
	public boolean canAdd(ItemStack itemstack){
		return true;
	}

	@Override
	public String getParameterName(){
		return "unlocalizedname";
	}

	@Override
	public String getParameterText(ItemStack itemstack){
		return itemstack.getItem().getTranslationKey(itemstack);
	}
}
