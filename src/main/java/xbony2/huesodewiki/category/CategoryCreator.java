package xbony2.huesodewiki.category;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Joiner;

import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemRecord;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import xbony2.huesodewiki.HuesoDeWiki;
import xbony2.huesodewiki.api.category.BasicInstanceOfCategory;
import xbony2.huesodewiki.api.category.ICategory;
import xbony2.huesodewiki.category.categories.ChestwearCategory;
import xbony2.huesodewiki.category.categories.FootwearCategory;
import xbony2.huesodewiki.category.categories.HeadwearCategory;
import xbony2.huesodewiki.category.categories.LegwearCategory;
import xbony2.huesodewiki.category.categories.ModCategory;

public class CategoryCreator {
	public static List<ICategory> categories = new ArrayList();
	
	static {
		categories.add(new ModCategory());
		categories.add(new BasicInstanceOfCategory("Food", ItemFood.class));
		categories.add(new BasicInstanceOfCategory("Music Discs", ItemRecord.class));
		categories.add(new BasicInstanceOfCategory("Farming", ItemHoe.class));
		categories.add(new BasicInstanceOfCategory("Shearing", ItemShears.class));
		categories.add(new FootwearCategory());
		categories.add(new LegwearCategory());
		categories.add(new ChestwearCategory());
		categories.add(new HeadwearCategory());
		categories.add(new BasicInstanceOfCategory("Felling", ItemAxe.class));
		categories.add(new BasicInstanceOfCategory("Fishing", ItemFishingRod.class));
		categories.add(new BasicInstanceOfCategory("Mining", ItemPickaxe.class, ItemSpade.class));
		categories.add(new BasicInstanceOfCategory("Melee weapons", ItemSword.class));
		categories.add(new BasicInstanceOfCategory("Ranged weapons", ItemBow.class));
	}
	
	public static String createCategories(ItemStack itemstack){
		List<String> categoryStrings = new ArrayList<String>();
		
		for(ICategory category : categories)
			if(category.canAdd(itemstack))
				categoryStrings.add("[[Category:" + category.getCategoryName(itemstack) + "]]");

		return Joiner.on(HuesoDeWiki.useStackedCategoryStyle ? "\n" : "").join(categoryStrings);
	}
}
