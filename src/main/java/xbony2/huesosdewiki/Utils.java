package xbony2.huesosdewiki;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

public class Utils {
	public static String getModName(ItemStack itemstack){
		ModContainer container = Loader.instance().getIndexedModList().get(Item.REGISTRY.getNameForObject(itemstack.getItem()).getResourceDomain());
		
		if(container == null)
			return "Vanilla";
		else{
			String modName = container.getName();
			return HuesosDeWiki.nameCorrections.get(modName) != null ? HuesosDeWiki.nameCorrections.get(modName) : modName;
		}
	}
	
	public static String getModAbbrevation(String modName){
		return "{{subst:#invoke:Mods|getAbbrv|" + modName + "}}";
	}
	
	public static String getModAbbrevation(ItemStack itemstack){
		return getModAbbrevation(getModName(itemstack));
	}
}
