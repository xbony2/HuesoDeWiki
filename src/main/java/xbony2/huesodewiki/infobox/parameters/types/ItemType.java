package xbony2.huesodewiki.infobox.parameters.types;

import net.minecraft.item.ItemStack;
import xbony2.huesodewiki.api.infobox.type.IType;

public class ItemType implements IType {

	@Override
	public int getPriority(){
		return 0;
	}

	@Override
	public String getName(){
		return "item";
	}

	@Override
	public boolean isApplicable(ItemStack itemstack){
		return true;
	}
}
