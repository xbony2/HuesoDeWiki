package xbony2.huesodewiki.api.category;

import net.minecraft.item.ItemStack;

public class BasicInstanceOfCategory implements ICategory {
	String name;
	Class[] classes;
	
	/**
	 * 
	 * @param name the name of the category to potentially be added.
	 * @param classes parent class(es) that the item's class must extend in order for the category to be added.
	 */
	public BasicInstanceOfCategory(String name, Class... classes){
		this.name = name;
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
	public String getCategoryName(ItemStack itemstack){
		return name;
	}
}
