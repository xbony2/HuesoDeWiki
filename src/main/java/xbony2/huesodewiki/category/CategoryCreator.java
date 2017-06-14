package xbony2.huesodewiki.category;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Joiner;

import net.minecraft.item.ItemStack;
import xbony2.huesodewiki.HuesoDeWiki;
import xbony2.huesodewiki.api.ICategory;
import xbony2.huesodewiki.category.categories.*;

public class CategoryCreator {
	public static List<ICategory> categories = new ArrayList();
	
	static {
		categories.add(new ModCategory());
		categories.add(new FoodCategory());
		categories.add(new MusicDiscsCategory());
		categories.add(new FarmingCategory());
		categories.add(new ShearingCategory());
		categories.add(new FootwearCategory());
		categories.add(new LegwearCategory());
		categories.add(new ChestwearCategory());
		categories.add(new HeadwearCategory());
		categories.add(new FellingCategory());
		categories.add(new FishingCategory());
		categories.add(new MiningCategory());
		categories.add(new MeleeWeaponsCategory());
		categories.add(new RangedWeaponsCategory());
	}
	
	public static String createCategories(ItemStack itemstack){
		List<String> categoryStrings = new ArrayList<String>();
		
		for(ICategory category : categories)
			if(category.canAdd(itemstack))
				categoryStrings.add("[[Category:" + category.getCategoryName(itemstack) + "]]");

		return Joiner.on(HuesoDeWiki.useStackedCategoryStyle ? "\n" : "").join(categoryStrings);
	}
}
