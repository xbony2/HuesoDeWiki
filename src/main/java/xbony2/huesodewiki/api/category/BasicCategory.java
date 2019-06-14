package xbony2.huesodewiki.api.category;

import java.util.function.Predicate;

import net.minecraft.item.ItemStack;

public class BasicCategory implements ICategory {
	private final String name;
	private final Predicate<ItemStack> canAdd;
	
	public BasicCategory(String name, Predicate<ItemStack> canAdd){
		this.name = name;
		this.canAdd = canAdd;
	}

	@Override
	public boolean canAdd(ItemStack itemstack){
		return canAdd.test(itemstack);
	}

	@Override
	public String getCategoryName(ItemStack itemstack){
		return name;
	}
}
