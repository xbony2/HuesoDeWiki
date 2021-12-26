package xbony2.huesodewiki.category;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Joiner;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SwordItem;
import xbony2.huesodewiki.api.HuesoDeWikiAPI;
import xbony2.huesodewiki.api.category.BasicCategory;
import xbony2.huesodewiki.api.category.BasicInstanceOfCategory;
import xbony2.huesodewiki.config.Config;

public class CategoryCreator {

	public static void init(){
		HuesoDeWikiAPI.categories.add(new ModCategory());
		HuesoDeWikiAPI.categories.add(new BasicCategory("Food", ItemStack::isEdible));
		HuesoDeWikiAPI.categories.add(new BasicInstanceOfCategory("Music Discs", RecordItem.class));
		HuesoDeWikiAPI.categories.add(new BasicInstanceOfCategory("Farming", HoeItem.class));
		HuesoDeWikiAPI.categories.add(new BasicInstanceOfCategory("Shearing", ShearsItem.class));
		HuesoDeWikiAPI.categories.add(new BasicCategory("Footwear", (itemstack) -> itemstack.getItem() instanceof ArmorItem item && item.getSlot() == EquipmentSlot.FEET));
		HuesoDeWikiAPI.categories.add(new BasicCategory("Legwear", (itemstack) -> itemstack.getItem() instanceof ArmorItem item && item.getSlot() == EquipmentSlot.LEGS));
		HuesoDeWikiAPI.categories.add(new BasicCategory("Chestwear", (itemstack) -> itemstack.getItem() instanceof ArmorItem item && item.getSlot() == EquipmentSlot.CHEST));
		HuesoDeWikiAPI.categories.add(new BasicCategory("Headwear", (itemstack) -> itemstack.getItem() instanceof ArmorItem item && item.getSlot() == EquipmentSlot.HEAD));
		HuesoDeWikiAPI.categories.add(new BasicInstanceOfCategory("Felling", AxeItem.class));
		HuesoDeWikiAPI.categories.add(new BasicInstanceOfCategory("Fishing", FishingRodItem.class));
		HuesoDeWikiAPI.categories.add(new BasicInstanceOfCategory("Mining", PickaxeItem.class));
		HuesoDeWikiAPI.categories.add(new BasicInstanceOfCategory("Digging", ShovelItem.class));
		HuesoDeWikiAPI.categories.add(new BasicInstanceOfCategory("Melee weapons", SwordItem.class));
		HuesoDeWikiAPI.categories.add(new BasicInstanceOfCategory("Ranged weapons", ProjectileWeaponItem.class));
	}

	public static String createCategories(ItemStack itemstack){
		List<String> categoryStrings = new ArrayList<>();
		
		HuesoDeWikiAPI.categories.stream().filter((category) -> category.canAdd(itemstack)).forEach((category) -> categoryStrings.add("[[Category:" + category.getCategoryName(itemstack) + "]]"));

		return Joiner.on(Config.useStackedCategoryStyle.get() ? "\n" : "").join(categoryStrings);
	}
}
