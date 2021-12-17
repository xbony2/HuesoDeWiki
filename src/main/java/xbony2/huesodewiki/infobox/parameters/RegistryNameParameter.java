package xbony2.huesodewiki.infobox.parameters;

import net.minecraft.world.item.ItemStack;
import xbony2.huesodewiki.api.infobox.IInfoboxParameter;

public class RegistryNameParameter implements IInfoboxParameter {

	@Override
	public boolean canAdd(ItemStack itemstack){
		return itemstack.getItem().getRegistryName() != null;
	}

	@Override
	public String getParameterName(){
		return "registryname";
	}

	@Override
	public String getParameterText(ItemStack itemstack){
		return itemstack.getItem().getRegistryName().toString();
	}
}
