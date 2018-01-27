package xbony2.huesodewiki.compat.baubles;


import baubles.api.cap.BaublesCapabilities;
import xbony2.huesodewiki.api.category.BasicCategory;
import xbony2.huesodewiki.category.CategoryCreator;
import xbony2.huesodewiki.infobox.parameters.TypeParameter;

public class BaublesCompat {
	
	public static void preInit(){
		CategoryCreator.categories.add(new BasicCategory("Baubles", 
				(itemstack) -> itemstack.hasCapability(BaublesCapabilities.CAPABILITY_ITEM_BAUBLE, null)));

		TypeParameter.types.add(new BaubleType());
	}
	
}
