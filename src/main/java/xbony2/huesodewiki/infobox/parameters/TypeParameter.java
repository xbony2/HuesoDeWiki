package xbony2.huesodewiki.infobox.parameters;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.DiggerItem;
import xbony2.huesodewiki.api.HuesoDeWikiAPI;
import xbony2.huesodewiki.api.infobox.IInfoboxParameter;
import xbony2.huesodewiki.api.infobox.type.BasicConditionType;
import xbony2.huesodewiki.api.infobox.type.BasicInstanceOfType;
import xbony2.huesodewiki.api.infobox.type.IType;
import xbony2.huesodewiki.infobox.parameters.types.TEntityType;

public class TypeParameter implements IInfoboxParameter {

	private static final IType fallback = new BasicConditionType(0, "item", itemstack -> true);
	
	static {
		HuesoDeWikiAPI.types.add(new BasicConditionType(10, "food", ItemStack::isEdible));
		HuesoDeWikiAPI.types.add(new BasicInstanceOfType(10, "armor", ArmorItem.class));
		HuesoDeWikiAPI.types.add(new BasicInstanceOfType(10, "tool", DiggerItem.class));
		HuesoDeWikiAPI.types.add(new BasicInstanceOfType(10, "weapon", SwordItem.class, ProjectileWeaponItem.class)); // Not comprehensive, of course
		HuesoDeWikiAPI.types.add(new TEntityType());
		HuesoDeWikiAPI.types.add(new BasicInstanceOfType(5, "block", BlockItem.class));
	}

	@Override
	public boolean canAdd(ItemStack itemstack){
		return true;
	}

	@Override
	public String getParameterName(){
		return "type";
	}

	@Override
	public String getParameterText(ItemStack itemstack){
		return HuesoDeWikiAPI.types.stream().filter((type) -> type.isApplicable(itemstack))
				.findFirst().orElse(fallback).getName();
	}
}
