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
import xbony2.huesodewiki.HuesoDeWiki;
import xbony2.huesodewiki.api.category.BasicCategory;
import xbony2.huesodewiki.api.category.BasicInstanceOfCategory;
import xbony2.huesodewiki.api.category.ICategory;

public class CategoryCreator {
	public static List<ICategory> categories = new ArrayList();
	
	static {
		categories.add(new ModCategory());
		categories.add(new BasicInstanceOfCategory("Food", ItemFood.class));
		categories.add(new BasicInstanceOfCategory("Music Discs", ItemRecord.class));
		categories.add(new BasicInstanceOfCategory("Farming", ItemHoe.class));
		categories.add(new BasicInstanceOfCategory("Shearing", ItemShears.class));
		categories.add(new BasicCategory("Footwear", (itemstack) -> itemstack.getItem() instanceof ItemArmor && ((ItemArmor)itemstack.getItem()).armorType == EntityEquipmentSlot.FEET));
		categories.add(new BasicCategory("Legwear", (itemstack) -> itemstack.getItem() instanceof ItemArmor && ((ItemArmor)itemstack.getItem()).armorType == EntityEquipmentSlot.LEGS));
		categories.add(new BasicCategory("Chestwear", (itemstack) -> itemstack.getItem() instanceof ItemArmor && ((ItemArmor)itemstack.getItem()).armorType == EntityEquipmentSlot.CHEST));
		categories.add(new BasicCategory("Headwear", (itemstack) -> itemstack.getItem() instanceof ItemArmor && ((ItemArmor)itemstack.getItem()).armorType == EntityEquipmentSlot.HEAD));
		categories.add(new BasicInstanceOfCategory("Felling", ItemAxe.class));
		categories.add(new BasicInstanceOfCategory("Fishing", ItemFishingRod.class));
		categories.add(new BasicInstanceOfCategory("Mining", ItemPickaxe.class));
		categories.add(new BasicInstanceOfCategory("Digging", ItemSpade.class));
		categories.add(new BasicInstanceOfCategory("Melee weapons", ItemSword.class));
		categories.add(new BasicInstanceOfCategory("Ranged weapons", ItemBow.class));
	}
	
	public static String createCategories(ItemStack itemstack){
		List<String> categoryStrings = new ArrayList<>();
		
		categories.stream().filter((category) -> category.canAdd(itemstack)).forEach((category) -> categoryStrings.add("[[Category:" + category.getCategoryName(itemstack) + "]]"));

		return Joiner.on(HuesoDeWiki.useStackedCategoryStyle ? "\n" : "").join(categoryStrings);
	}
}
