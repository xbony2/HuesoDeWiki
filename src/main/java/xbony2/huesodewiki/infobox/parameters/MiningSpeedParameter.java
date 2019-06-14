package xbony2.huesodewiki.infobox.parameters;

import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import xbony2.huesodewiki.Utils;
import xbony2.huesodewiki.api.infobox.IInfoboxParameter;

public class MiningSpeedParameter implements IInfoboxParameter {
	
	@Override
	public boolean canAdd(ItemStack itemstack) {
		return itemstack.getItem() instanceof ToolItem;
	}

	@Override
	public String getParameterName() {
		return "miningspeed";
	}

	@Override
	public String getParameterText(ItemStack itemstack) {
		IItemTier tier = ((ToolItem) itemstack.getItem()).getTier();
		return Utils.floatToString(tier.getEfficiency());
	}
}
