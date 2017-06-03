package xbony2.huesodewiki.category;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import xbony2.huesodewiki.api.ICategory;
import xbony2.huesodewiki.category.categories.FoodCategory;
import xbony2.huesodewiki.category.categories.ModCategory;

public class CategoryCreator {
	public static List<ICategory> categories = new ArrayList();
	
	static {
		categories.add(new ModCategory());
		categories.add(new FoodCategory());
	}
	
	public static String createCategories(ItemStack itemstack){
		String ret = "";
		
		for(ICategory category : categories)
			if(category.canAdd(itemstack))
				ret += "[[Category:" + category.getCategoryName(itemstack) + "]]"; //TODO: support "stacked" categories via config.
		
		return ret;
	}
}
