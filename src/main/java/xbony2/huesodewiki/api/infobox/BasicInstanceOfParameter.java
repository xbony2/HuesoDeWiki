package xbony2.huesodewiki.api.infobox;

import java.util.function.Function;

import net.minecraft.item.ItemStack;

@SuppressWarnings("rawtypes")
public class BasicInstanceOfParameter implements IInfoboxParameter {
	private final String name;
	private final Function<ItemStack, String> paramTextFunction;
	private final Class[] classes;
	
	public BasicInstanceOfParameter(String name, Function<ItemStack, String> paramText, Class... classes){
		this.name = name;
		this.paramTextFunction = paramText;
		this.classes = classes;
	}

	@Override
	public boolean canAdd(ItemStack itemstack){
		for(Class clazz : classes)
			if(clazz.isInstance(itemstack.getItem()))
				return true;
		
		return false;
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
