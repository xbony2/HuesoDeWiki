package xbony2.huesodewiki.api.infobox.type;

import net.minecraft.item.ItemStack;

public class BasicInstanceOfType implements IType {
	private final int PRIORITY;
	private final String NAME;
	private final Class[] CLASSES;
	
	public BasicInstanceOfType(int priority, String name, Class... classes){
		PRIORITY = priority;
		NAME = name;
		CLASSES = classes;
	}

	@Override
	public int getPriority(){
		return PRIORITY;
	}

	@Override
	public String getName(){
		return NAME;
	}

	@Override
	public boolean isApplicable(ItemStack itemstack){
		for(Class clazz : CLASSES)
			if(clazz.isInstance(itemstack.getItem()))
				return true;
		
		return false;
	}
}
