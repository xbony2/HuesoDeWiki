/*package xbony2.huesodewiki.compat.ic2;

import ic2.api.item.IElectricItem;
import xbony2.huesodewiki.Utils;
import xbony2.huesodewiki.api.category.BasicCategory;
import xbony2.huesodewiki.api.infobox.BasicInstanceOfParameter;
import xbony2.huesodewiki.category.CategoryCreator;
import xbony2.huesodewiki.infobox.InfoboxCreator;

public class IC2Compat {
	public static void preInit(){
		CategoryCreator.categories.add(new BasicCategory("EU Power", (itemstack) -> {
			return itemstack.getItem() instanceof IElectricItem;
		}));
		
		InfoboxCreator.parameters.add(new EUStorageParameter());
	}
}
*/