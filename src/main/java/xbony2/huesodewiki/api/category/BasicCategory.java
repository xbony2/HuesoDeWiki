package xbony2.huesodewiki.api.category;

import net.minecraft.item.ItemStack;

public class BasicCategory implements ICategory {
	private final String NAME;
	private final ICanAdd CAN_ADD;
	
	public BasicCategory(String name, ICanAdd canAdd){
		NAME = name;
		CAN_ADD = canAdd;
	}

	@Override
	public boolean canAdd(ItemStack itemstack){
		return CAN_ADD.canAdd(itemstack);
	}

	@Override
	public String getCategoryName(ItemStack itemstack){
		return NAME;
	}
}
