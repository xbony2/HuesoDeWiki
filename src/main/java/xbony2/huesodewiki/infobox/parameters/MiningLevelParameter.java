package xbony2.huesodewiki.infobox.parameters;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;
import xbony2.huesodewiki.api.infobox.IInfoboxParameter;

public class MiningLevelParameter implements IInfoboxParameter {
	
	@Override
	public boolean canAdd(ItemStack itemstack){
		return itemstack.getItem() instanceof PickaxeItem;
	}

	@Override
	public String getParameterName(){
		return "mininglevel";
	}

	@Override
	public String getParameterText(ItemStack itemstack){
		Tier tier = ((PickaxeItem) itemstack.getItem()).getTier();
		// TODO: Forge says "FORGE: Use TierSortingRegistry to define which tiers are better than others"
		// This does exactly what we want it to do though, so will ignore Forge's advice until something breaks.
		return Integer.toString(tier.getLevel());
	}
}
