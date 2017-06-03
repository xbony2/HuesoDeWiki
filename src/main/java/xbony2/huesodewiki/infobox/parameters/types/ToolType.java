package xbony2.huesodewiki.infobox.parameters.types;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import xbony2.huesodewiki.api.infobox.IType;

public class ToolType implements IType {

	@Override
	public int getPriority(){
		return 10;
	}

	@Override
	public String getName(){
		return "tool";
	}

	@Override
	public boolean isApplicable(ItemStack itemstack){
		return itemstack.getItem() instanceof ItemTool;
	}
}
