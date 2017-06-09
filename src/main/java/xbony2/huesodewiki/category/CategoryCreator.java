package xbony2.huesodewiki.category;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Joiner;
import net.minecraft.item.ItemStack;
import xbony2.huesodewiki.HuesoDeWiki;
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
		List<String> categoryStrings = new ArrayList<String>();
		
		for(ICategory category : categories)
			if(category.canAdd(itemstack))
				categoryStrings.add("[[Category:" + category.getCategoryName(itemstack) + "]]");

		return Joiner.on(HuesoDeWiki.useStackedCategoryStyle ? "\n" : "").join(categoryStrings);
	}
}
