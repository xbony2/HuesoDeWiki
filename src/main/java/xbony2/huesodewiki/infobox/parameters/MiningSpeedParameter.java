package xbony2.huesodewiki.infobox.parameters;

import net.minecraft.world.item.Tier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.DiggerItem;
import xbony2.huesodewiki.Utils;
import xbony2.huesodewiki.api.infobox.IInfoboxParameter;

public class MiningSpeedParameter implements IInfoboxParameter {
	
	@Override
	public boolean canAdd(ItemStack itemstack){
		return itemstack.getItem() instanceof DiggerItem;
	}

	@Override
	public String getParameterName(){
		return "miningspeed";
	}

	@Override
	public String getParameterText(ItemStack itemstack){
		Tier tier = ((DiggerItem) itemstack.getItem()).getTier();
		return Utils.floatToString(tier.getSpeed());
	}
}
