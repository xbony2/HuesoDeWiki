package xbony2.huesodewiki.category;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Joiner;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.AxeItem;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ShearsItem;
import net.minecraft.item.ShootableItem;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.SwordItem;
import xbony2.huesodewiki.api.HuesoDeWikiAPI;
import xbony2.huesodewiki.api.category.BasicCategory;
import xbony2.huesodewiki.api.category.BasicInstanceOfCategory;
import xbony2.huesodewiki.config.Config;

public class CategoryCreator {

	public static void init(){
		HuesoDeWikiAPI.categories.add(new ModCategory());
		HuesoDeWikiAPI.categories.add(new BasicCategory("Food", ItemStack::isFood));
		HuesoDeWikiAPI.categories.add(new BasicInstanceOfCategory("Music Discs", MusicDiscItem.class));
		HuesoDeWikiAPI.categories.add(new BasicInstanceOfCategory("Farming", HoeItem.class));
		HuesoDeWikiAPI.categories.add(new BasicInstanceOfCategory("Shearing", ShearsItem.class));
		HuesoDeWikiAPI.categories.add(new BasicCategory("Footwear", (itemstack) -> itemstack.getItem() instanceof ArmorItem && ((ArmorItem) itemstack.getItem()).getEquipmentSlot() == EquipmentSlotType.FEET));
		HuesoDeWikiAPI.categories.add(new BasicCategory("Legwear", (itemstack) -> itemstack.getItem() instanceof ArmorItem && ((ArmorItem) itemstack.getItem()).getEquipmentSlot() == EquipmentSlotType.LEGS));
		HuesoDeWikiAPI.categories.add(new BasicCategory("Chestwear", (itemstack) -> itemstack.getItem() instanceof ArmorItem && ((ArmorItem) itemstack.getItem()).getEquipmentSlot() == EquipmentSlotType.CHEST));
		HuesoDeWikiAPI.categories.add(new BasicCategory("Headwear", (itemstack) -> itemstack.getItem() instanceof ArmorItem && ((ArmorItem) itemstack.getItem()).getEquipmentSlot() == EquipmentSlotType.HEAD));
		HuesoDeWikiAPI.categories.add(new BasicInstanceOfCategory("Felling", AxeItem.class));
		HuesoDeWikiAPI.categories.add(new BasicInstanceOfCategory("Fishing", FishingRodItem.class));
		HuesoDeWikiAPI.categories.add(new BasicInstanceOfCategory("Mining", PickaxeItem.class));
		HuesoDeWikiAPI.categories.add(new BasicInstanceOfCategory("Digging", ShovelItem.class));
		HuesoDeWikiAPI.categories.add(new BasicInstanceOfCategory("Melee weapons", SwordItem.class));
		HuesoDeWikiAPI.categories.add(new BasicInstanceOfCategory("Ranged weapons", ShootableItem.class));
	}

	public static String createCategories(ItemStack itemstack){
		List<String> categoryStrings = new ArrayList<>();
		
		HuesoDeWikiAPI.categories.stream().filter((category) -> category.canAdd(itemstack)).forEach((category) -> categoryStrings.add("[[Category:" + category.getCategoryName(itemstack) + "]]"));

		return Joiner.on(Config.useStackedCategoryStyle.get() ? "\n" : "").join(categoryStrings);
	}
}
