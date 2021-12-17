package xbony2.huesodewiki.api.infobox;

import net.minecraft.world.item.ItemStack;

public interface IInfoboxParameter {
	String IMC_NAME = "infobox_param";

	boolean canAdd(ItemStack itemstack);

	String getParameterName();

	String getParameterText(ItemStack itemstack);
}
