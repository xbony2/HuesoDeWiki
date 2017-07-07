package xbony2.huesodewiki.compat.tesla;

import net.darkhax.tesla.capability.TeslaCapabilities;
import xbony2.huesodewiki.api.category.BasicCategory;
import xbony2.huesodewiki.category.CategoryCreator;

public class TeslaCompat {
	public static void preInit(){
		//I don't know if this works or not, but there's not any mods to test without messing with hacky kotlin stuff so I'll pass on it for now.
		CategoryCreator.categories.add(new BasicCategory("Tesla Power", (itemstack) -> itemstack.hasCapability(TeslaCapabilities.CAPABILITY_CONSUMER, null) || 
				itemstack.hasCapability(TeslaCapabilities.CAPABILITY_HOLDER, null) || itemstack.hasCapability(TeslaCapabilities.CAPABILITY_PRODUCER, null)));
	}
}
