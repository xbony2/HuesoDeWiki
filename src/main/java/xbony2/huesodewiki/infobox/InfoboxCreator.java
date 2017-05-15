package xbony2.huesodewiki.infobox;

import static xbony2.huesodewiki.Utils.getModName;
import static xbony2.huesodewiki.Utils.outputItemOutput;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import xbony2.huesodewiki.api.infobox.IInfoboxParameter;
import xbony2.huesodewiki.infobox.parameters.*;

public class InfoboxCreator {
	public static List<IInfoboxParameter> parameters = new ArrayList();
	
	static {
		parameters.add(new NameParameter());
		parameters.add(new ImageIconParameter());
		parameters.add(new ModParameter());
		parameters.add(new TypeParameter());
		parameters.add(new BlastResistanceParameter());
		parameters.add(new HardnessParameter());
		parameters.add(new FoodPointsParameter());
		parameters.add(new SaturationParameter());
		parameters.add(new ArmorRatingParameter());
		parameters.add(new ToughnessParameter());
	}
	
	public static String createInfobox(ItemStack itemstack){
		
		String page = "{{Infobox" + "\n";
		
		for(IInfoboxParameter parameter : parameters)
			if(parameter.canAdd(itemstack))
				page += "|" + parameter.parameterName() + "=" + parameter.parameterText(itemstack) + "\n";
		
		page += "}}" + "\n";
		return page;
	}
}
