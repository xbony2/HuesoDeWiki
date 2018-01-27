package xbony2.huesodewiki.compat.baubles;

import baubles.api.cap.BaublesCapabilities;
import net.minecraft.item.ItemStack;
import xbony2.huesodewiki.api.infobox.type.IType;

public class BaubleType implements IType {
	@Override
	public int getPriority() {
		return 10;
	}

	@Override
	public String getName() {
		return "bauble";
	}

	@Override
	public boolean isApplicable(ItemStack itemstack) {
		return itemstack.hasCapability(BaublesCapabilities.CAPABILITY_ITEM_BAUBLE, null);
	}
}
