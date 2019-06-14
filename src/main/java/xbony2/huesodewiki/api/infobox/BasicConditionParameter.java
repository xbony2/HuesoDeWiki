package xbony2.huesodewiki.api.infobox;

import java.util.function.Function;
import java.util.function.Predicate;

import net.minecraft.item.ItemStack;

public class BasicConditionParameter implements IInfoboxParameter {
	private final String name;
	private final Function<ItemStack, String> paramTextFunction;
	private final Predicate<ItemStack> condition;

	public BasicConditionParameter(String name, Function<ItemStack, String> paramTextFunction, Predicate<ItemStack> condition){
		this.name = name;
		this.condition = condition;
		this.paramTextFunction = paramTextFunction;
	}

	@Override
	public boolean canAdd(ItemStack itemstack){
		return condition.test(itemstack);
	}

	@Override
	public String getParameterName(){
		return name;
	}

	@Override
	public String getParameterText(ItemStack itemstack){
		return paramTextFunction.apply(itemstack);
	}
}
