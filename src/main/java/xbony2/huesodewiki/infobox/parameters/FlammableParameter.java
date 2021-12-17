package xbony2.huesodewiki.infobox.parameters;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import xbony2.huesodewiki.api.infobox.IInfoboxParameter;

public class FlammableParameter implements IInfoboxParameter {

	@Override
	public boolean canAdd(ItemStack itemstack){
		if(itemstack.getItem() instanceof BlockItem){
			Block block = ((BlockItem) itemstack.getItem()).getBlock();
			
			try {
				return block.isFlammable(block.defaultBlockState(), null, null, null);
			}catch(Exception e){
				return false;
			}
		}
		return false;
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
