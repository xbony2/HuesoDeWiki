package xbony2.huesodewiki.api.infobox;

import net.minecraft.item.ItemStack;

public class BasicInstanceOfParameter implements IInfoboxParameter {
	private final String NAME;
	private final IGetParameterText GET_PARAMETER_TEXT;
	private final Class[] CLASSES;
	
	public BasicInstanceOfParameter(String name, IGetParameterText getParameterText, Class... classes){
		NAME = name;
		GET_PARAMETER_TEXT = getParameterText;
		CLASSES = classes;
	}

	@Override
	public boolean canAdd(ItemStack itemstack){
		for(Class clazz : CLASSES)
			if(clazz.isInstance(itemstack.getItem()))
				return true;
		
		return false;
	}

	@Override
	public String getParameterName(){
		return NAME;
	}

	@Override
	public String getParameterText(ItemStack itemstack){
		return GET_PARAMETER_TEXT.getParameterText(itemstack);
	}
}
