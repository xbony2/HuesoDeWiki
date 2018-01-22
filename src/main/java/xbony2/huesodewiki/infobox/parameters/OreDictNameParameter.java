package xbony2.huesodewiki.infobox.parameters;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import xbony2.huesodewiki.Utils;
import xbony2.huesodewiki.api.infobox.IInfoboxParameter;

import java.util.ArrayList;
import java.util.List;

public class OreDictNameParameter implements IInfoboxParameter {
	@Override
	public boolean canAdd(ItemStack itemstack){
		return OreDictionary.getOreIDs(itemstack).length > 0;
	}

	@Override
	public String getParameterName(){
		return "oredictname";
	}

	@Override
	public String getParameterText(ItemStack itemstack){
		int[] ids = OreDictionary.getOreIDs(itemstack);
		List<String> oreDictNames = new ArrayList<>();
		for (int id : ids) {
			oreDictNames.add(OreDictionary.getOreName(id));
		}
		return Utils.formatInfoboxList(oreDictNames);
	}
}
