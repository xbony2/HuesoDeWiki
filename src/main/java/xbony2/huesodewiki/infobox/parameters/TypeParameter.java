package xbony2.huesodewiki.infobox.parameters;

import net.minecraft.item.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BowItem;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolItem;
import xbony2.huesodewiki.api.HuesoDeWikiAPI;
import xbony2.huesodewiki.api.infobox.IInfoboxParameter;
import xbony2.huesodewiki.api.infobox.type.BasicConditionType;
import xbony2.huesodewiki.api.infobox.type.BasicInstanceOfType;
import xbony2.huesodewiki.api.infobox.type.IType;
import xbony2.huesodewiki.infobox.parameters.types.*;

public class TypeParameter implements IInfoboxParameter {

	private static final IType fallback = new BasicConditionType(0, "item", itemstack -> true);
	
	static{
		HuesoDeWikiAPI.types.add(new BasicConditionType(10, "food", ItemStack::isFood));
		HuesoDeWikiAPI.types.add(new BasicInstanceOfType(10, "armor", ArmorItem.class));
		HuesoDeWikiAPI.types.add(new BasicInstanceOfType(10, "tool", ToolItem.class));
		HuesoDeWikiAPI.types.add(new BasicInstanceOfType(10, "weapon", SwordItem.class, BowItem.class)); //Not really comprehensive but hey
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
