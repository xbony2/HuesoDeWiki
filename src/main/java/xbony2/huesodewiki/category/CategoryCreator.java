package xbony2.huesodewiki.category;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Joiner;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
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
import xbony2.huesodewiki.api.HuesoDeWikiAPI;
import xbony2.huesodewiki.api.category.BasicCategory;
import xbony2.huesodewiki.api.category.BasicInstanceOfCategory;
import xbony2.huesodewiki.config.Config;

public class CategoryCreator {

	public static void init() {
		HuesoDeWikiAPI.categories.add(new ModCategory());
		HuesoDeWikiAPI.categories.add(new BasicInstanceOfCategory("Food", ItemFood.class));
		HuesoDeWikiAPI.categories.add(new BasicInstanceOfCategory("Music Discs", ItemRecord.class));
		HuesoDeWikiAPI.categories.add(new BasicInstanceOfCategory("Farming", ItemHoe.class));
		HuesoDeWikiAPI.categories.add(new BasicInstanceOfCategory("Shearing", ItemShears.class));
		HuesoDeWikiAPI.categories.add(new BasicCategory("Footwear", (itemstack) -> itemstack.getItem() instanceof ItemArmor && ((ItemArmor)itemstack.getItem()).getEquipmentSlot() == EntityEquipmentSlot.FEET));
		HuesoDeWikiAPI.categories.add(new BasicCategory("Legwear", (itemstack) -> itemstack.getItem() instanceof ItemArmor && ((ItemArmor)itemstack.getItem()).getEquipmentSlot() == EntityEquipmentSlot.LEGS));
		HuesoDeWikiAPI.categories.add(new BasicCategory("Chestwear", (itemstack) -> itemstack.getItem() instanceof ItemArmor && ((ItemArmor)itemstack.getItem()).getEquipmentSlot() == EntityEquipmentSlot.CHEST));
		HuesoDeWikiAPI.categories.add(new BasicCategory("Headwear", (itemstack) -> itemstack.getItem() instanceof ItemArmor && ((ItemArmor)itemstack.getItem()).getEquipmentSlot() == EntityEquipmentSlot.HEAD));
		HuesoDeWikiAPI.categories.add(new BasicInstanceOfCategory("Felling", ItemAxe.class));
		HuesoDeWikiAPI.categories.add(new BasicInstanceOfCategory("Fishing", ItemFishingRod.class));
		HuesoDeWikiAPI.categories.add(new BasicInstanceOfCategory("Mining", ItemPickaxe.class));
		HuesoDeWikiAPI.categories.add(new BasicInstanceOfCategory("Digging", ItemSpade.class));
		HuesoDeWikiAPI.categories.add(new BasicInstanceOfCategory("Melee weapons", ItemSword.class));
		HuesoDeWikiAPI.categories.add(new BasicInstanceOfCategory("Ranged weapons", ItemBow.class));
	}
	
	public static String createCategories(ItemStack itemstack){
		List<String> categoryStrings = new ArrayList<>();
		
		HuesoDeWikiAPI.categories.stream().filter((category) -> category.canAdd(itemstack)).forEach((category) -> categoryStrings.add("[[Category:" + category.getCategoryName(itemstack) + "]]"));

		return Joiner.on(Config.useStackedCategoryStyle.get() ? "\n" : "").join(categoryStrings);
	}
}
