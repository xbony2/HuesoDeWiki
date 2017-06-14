package xbony2.huesodewiki.infobox;

import java.util.ArrayList;
import java.util.List;

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
		parameters.add(new HungerParameter());
		parameters.add(new ArmorRatingParameter());
		parameters.add(new ToughnessParameter());
		parameters.add(new DamageParameter()); //Consider it WIP, it's dumb.
		parameters.add(new DurabilityParameter());
		parameters.add(new StackableParameter());
		parameters.add(new FlammableParameter());
	}
	
	public static String createInfobox(ItemStack itemstack){
		StringBuilder ret = new StringBuilder("{{Infobox\n");
		
		for(IInfoboxParameter parameter : parameters)
			if(parameter.canAdd(itemstack))
				ret.append('|').append(parameter.getParameterName()).append('=').append(parameter.getParameterText(itemstack)).append('\n');
		
		ret.append("}}\n");
		return ret.toString();
	}
}
