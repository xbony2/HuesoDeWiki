package xbony2.huesodewiki.api.infobox.type;

import net.minecraft.item.ItemStack;

public interface IType {
	/**
	 * 
	 * @return the priority of this type. More specialized types should have a greater priority.
	 * For example, the "item" type has a priority of 0, but the "food" type has a priority of 10 so it will always be choosen over "item,"
	 * even though both would be applicable.
	 */
	int getPriority();
	
	String getName();
	
	boolean isApplicable(ItemStack itemstack);
}
