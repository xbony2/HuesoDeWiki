package xbony2.huesodewiki.api.infobox.type;

import net.minecraft.item.ItemStack;

public class BasicInstanceOfType implements IType {
	private final int priority;
	private final String name;
	private final Class[] classes;
	
	public BasicInstanceOfType(int priority, String name, Class... classes){
		this.priority = priority;
		this.name = name;
		this.classes = classes;
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
		for(Class clazz : classes)
			if(clazz.isInstance(itemstack.getItem()))
				return true;
		
		return false;
	}
}
