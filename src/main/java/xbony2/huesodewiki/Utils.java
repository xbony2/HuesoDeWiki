package xbony2.huesodewiki;

import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.oredict.OreDictionary;

public class Utils {
	public static String getModName(ItemStack itemstack){
		ModContainer container = Loader.instance().getIndexedModList().get(Item.REGISTRY.getNameForObject(itemstack.getItem()).getResourceDomain());
		
		if(container == null)
			return "Vanilla";
		else{
			String modName = container.getName();
			return HuesoDeWiki.nameCorrections.get(modName) != null ? HuesoDeWiki.nameCorrections.get(modName) : modName;
		}
	}
	
	public static String getModAbbrevation(String modName){
		return "{{subst:#invoke:Mods|getAbbrv|" + modName + "}}";
	}
	
	public static String getModAbbrevation(ItemStack itemstack){
		return getModAbbrevation(getModName(itemstack));
	}
	
	public static String outputItem(ItemStack itemstack){
		return "{{Gc|mod=" + getModAbbrevation(itemstack) + "|dis=false|" + itemstack.getDisplayName() + "}}";
	}
	
	public static String outputItemOutput(ItemStack itemstack){
		return "{{Gc|mod=" + getModAbbrevation(itemstack) + "|link=none|" + itemstack.getDisplayName() + (itemstack.getCount() != 1 ? "|" + itemstack.getCount() : "") + "}}";
	}
	
	/**
	 * @return null if nothing can be found.
	 */
	public static String outputOreDictionaryEntry(List<ItemStack> list){
		String ret = null;
		ItemStack stack = list.iterator().next();
		
		if(stack != null){
			int[] ids = OreDictionary.getOreIDs(stack);
			
			for(int i = 0; i < ids.length; i++){
				String potentialEntry = OreDictionary.getOreName(ids[i]);
				List<ItemStack> potentialCognate = OreDictionary.getOres(potentialEntry);
				
				boolean isEqual = potentialCognate.size() == list.size();
				
				if(isEqual) //so far, that is
					for(int j = 0; j < list.size(); j++)
						if(potentialCognate.get(j).getItem() != list.get(j).getItem() && potentialCognate.get(j).getItemDamage() != list.get(j).getItemDamage())
							isEqual = false;
				
				if(isEqual){
					ret = "{{O|" + potentialEntry + "}}";
					break;
				}
			}
		}
		
		return ret;
	}
	
	public static String floatToString(float f){
		String ret = Float.toString(f);
		if(ret.endsWith(".0"))
				ret = ret.replaceAll(".0$", "");
		return ret;
	}
}
