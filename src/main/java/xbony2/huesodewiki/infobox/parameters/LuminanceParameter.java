package xbony2.huesodewiki.infobox.parameters;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import xbony2.huesodewiki.Utils;
import xbony2.huesodewiki.api.infobox.IInfoboxParameter;

public class LuminanceParameter implements IInfoboxParameter {

	@Override
	public boolean canAdd(ItemStack itemstack){
		if(itemstack.getItem() instanceof BlockItem){
			Block block = ((BlockItem) itemstack.getItem()).getBlock();
			try {
				return block.getLightValue(Utils.stackToBlockState(itemstack), null, null) > 0;
			}catch(Exception e){
				return false;
			}
		}
		return false;
	}

	@Override
	public String getParameterName(){
		return "luminance";
	}

	@Override
	public String getParameterText(ItemStack itemstack){
		Block block = ((BlockItem) itemstack.getItem()).getBlock();
		return Integer.toString(block.getLightValue(Utils.stackToBlockState(itemstack), null, null));
	}
}
