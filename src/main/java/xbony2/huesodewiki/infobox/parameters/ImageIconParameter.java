package xbony2.huesodewiki.infobox.parameters;

import static xbony2.huesodewiki.Utils.outputItemOutput;

import net.minecraft.item.ItemStack;
import xbony2.huesodewiki.api.infobox.IInfoboxParameter;

public class ImageIconParameter implements IInfoboxParameter {

	@Override
	public boolean canAdd(ItemStack itemstack){
		return true;
	}

	@Override
	public String parameterName(){
		return "imageicon";
	}

	@Override
	public String parameterText(ItemStack itemstack){
		return outputItemOutput(itemstack);
	}
}
