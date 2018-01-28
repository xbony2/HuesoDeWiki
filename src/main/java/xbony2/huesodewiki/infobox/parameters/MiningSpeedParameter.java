package xbony2.huesodewiki.infobox.parameters;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import xbony2.huesodewiki.Utils;
import xbony2.huesodewiki.api.infobox.IInfoboxParameter;

import java.lang.reflect.Field;

public class MiningSpeedParameter implements IInfoboxParameter {
	@Override
	public boolean canAdd(ItemStack itemstack) {
		return itemstack.getItem() instanceof ItemTool;
	}

	@Override
	public String getParameterName() {
		return "miningspeed";
	}

	@Override
	public String getParameterText(ItemStack itemstack) {
		try {
			Field field = Utils.getField(ItemTool.class, "toolMaterial", "field_77862_b");
			if(field != null){
				field.setAccessible(true);
				return Utils.floatToString(((Item.ToolMaterial) field.get(itemstack.getItem())).getEfficiency());
			}
		} catch(IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return "?";
	}
}
