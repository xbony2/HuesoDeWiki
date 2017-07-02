package xbony2.huesodewiki.compact;

import net.minecraftforge.fml.common.Loader;
import xbony2.huesodewiki.compact.tesla.TeslaCompact;

public class Compact {
	public static void preInit(){
		if(Loader.isModLoaded("tesla"))
			TeslaCompact.preInit();
	}
}
