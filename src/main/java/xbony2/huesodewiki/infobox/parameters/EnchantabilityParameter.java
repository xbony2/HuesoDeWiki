package xbony2.huesodewiki.infobox.parameters;

import net.minecraft.item.ItemStack;
import xbony2.huesodewiki.api.infobox.IInfoboxParameter;

public class EnchantabilityParameter implements IInfoboxParameter {

	@Override
	public boolean canAdd(ItemStack itemstack) {
		return itemstack.getItem().getItemEnchantability(itemstack) > 0;
	}
	
	@Override
	public String getParameterName() {
		return "enchantability";
	}

	@Override
	public String getParameterText(ItemStack itemstack) {
		return Integer.toString(itemstack.getItem().getItemEnchantability(itemstack));
	}
}
