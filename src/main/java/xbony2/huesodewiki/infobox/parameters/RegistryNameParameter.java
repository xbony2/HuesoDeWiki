package xbony2.huesodewiki.infobox.parameters;

import net.minecraft.core.Registry;
import net.minecraft.world.item.ItemStack;
import xbony2.huesodewiki.api.infobox.IInfoboxParameter;

public class RegistryNameParameter implements IInfoboxParameter {

	@Override
	public boolean canAdd(ItemStack itemstack){
		return true; // everything has to be registered somewhere so this is not very necessary
	}

	@Override
	public String getParameterName(){
		return "registryname";
	}

	@Override
	public String getParameterText(ItemStack itemstack){
		// I don't know if this gives exactly what we want, and also it's deprecated, but this class isn't actually used anyway so meh -bony
		return Registry.ITEM.getKey(itemstack.getItem()).getPath();
	}
}
