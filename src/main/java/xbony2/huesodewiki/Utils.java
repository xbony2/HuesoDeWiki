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
		return "{{Gc|mod=" + getModAbbrevation(itemstack) + "|link=none|" + itemstack.getDisplayName() + (itemstack.stackSize != 1 ? "|" + itemstack.stackSize : "") + "}}";
	}
	
	/**
	 * *Will* return null if nothing can be found.
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
	
	public static String getShapedLocation(int height, int width){
		return ((char)(width + 64)) + "" + height;
	}
	
	public static String getShapelessLocation(int index, int max){
		if(max > 6)
			switch(index){
			case 0:
				return "A1";
			case 1:
				return "B1";
			case 2:
				return "C1";
			case 3:
				return "A2";
			case 4:
				return "B2";
			case 5:
				return "C2";
			case 6:
				return "A3";
			case 7:
				return "B3";
			case 8:
				return "C3";
			}
		else
			switch(index){
			case 0:
				return "A1";
			case 1:
				return "B1";
			case 2:
				return "A2";
			case 3:
				return "B2";
			case 4:
				return "A3";
			case 5:
				return "B3";
			}
		return null;
	}
}
