package xbony2.huesodewiki.infobox.parameters;

import static xbony2.huesodewiki.Utils.outputItemOutput;

import net.minecraft.world.item.ItemStack;
import xbony2.huesodewiki.api.infobox.IInfoboxParameter;

public class ImageIconParameter implements IInfoboxParameter {

	@Override
	public boolean canAdd(ItemStack itemstack){
		return true;
	}

	@Override
	public String getParameterName(){
		return "imageicon";
	}

	@Override
	public String getParameterText(ItemStack itemstack){
		ItemStack copy = itemstack.copy();
		copy.setCount(1);
		return outputItemOutput(copy);
	}
}
