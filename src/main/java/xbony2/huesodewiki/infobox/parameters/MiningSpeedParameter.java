package xbony2.huesodewiki.infobox.parameters;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import xbony2.huesodewiki.Utils;
import xbony2.huesodewiki.api.infobox.IInfoboxParameter;

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
		Item.ToolMaterial material = ObfuscationReflectionHelper.getPrivateValue(ItemTool.class, (ItemTool) itemstack.getItem(), "field_77862_b"); //toolMaterial
		return Utils.floatToString(material.getEfficiency()); //toolMaterial
	}
}
