package xbony2.huesodewiki.infobox.parameters;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ToolType;
import xbony2.huesodewiki.api.infobox.IInfoboxParameter;

public class MiningLevelParameter implements IInfoboxParameter {
	
	@Override
	public boolean canAdd(ItemStack itemstack){
		return itemstack.getItem().getHarvestLevel(itemstack, ToolType.PICKAXE, null, null) >= 0;
	}

	@Override
	public String getParameterName(){
		return "mininglevel";
	}

	@Override
	public String getParameterText(ItemStack itemstack){
		return Integer.toString(itemstack.getItem().getHarvestLevel(itemstack, ToolType.PICKAXE, null, null));
	}
}
