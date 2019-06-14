package xbony2.huesodewiki.api.infobox.type;

import java.util.function.Predicate;

import net.minecraft.item.ItemStack;

public class BasicConditionType implements IType {
	private final int priority;
	private final String name;
	private final Predicate<ItemStack> condition;

	public BasicConditionType(int priority, String name, Predicate<ItemStack> condition){
		this.priority = priority;
		this.name = name;
		this.condition = condition;
	}
	
	@Override
	public int getPriority(){
		return priority;
	}

	@Override
	public String getName(){
		return name;
	}

	@Override
	public boolean isApplicable(ItemStack itemstack){
		return condition.test(itemstack);
	}
}
