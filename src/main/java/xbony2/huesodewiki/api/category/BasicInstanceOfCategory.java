package xbony2.huesodewiki.api.category;

import net.minecraft.item.ItemStack;

public class BasicInstanceOfCategory implements ICategory {
	private final String NAME;
	private final Class[] CLASSES;
	
	/**
	 * 
	 * @param name the name of the category to potentially be added.
	 * @param classes parent class(es) that the item's class must extend in order for the category to be added.
	 */
	public BasicInstanceOfCategory(String name, Class... classes){
		NAME = name;
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
	public String getCategoryName(ItemStack itemstack){
		return NAME;
	}
}
