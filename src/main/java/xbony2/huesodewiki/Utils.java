package xbony2.huesodewiki;

import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.oredict.OreDictionary;

public class Utils {
	public static String getModName(ItemStack itemstack){
		ModContainer container = Loader.instance().getIndexedModList().get(itemstack.getItem().getCreatorModId(itemstack));
		
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
	
	public static String outputIngredient(Ingredient ingredient){
		StringBuilder ret = new StringBuilder();
		
		for(ItemStack itemstack : ingredient.getMatchingStacks()){
			ret.append(outputItem(itemstack));
		}
		
		return ret.toString();
	}
	
	public static String outputItemOutput(ItemStack itemstack){
		return "{{Gc|mod=" + getModAbbrevation(itemstack) + "|link=none|" + itemstack.getDisplayName() + (itemstack.getCount() != 1 ? "|" + itemstack.getCount() : "") + "}}";
	}
	
	/**
	 * @return null if nothing can be found.
	 */
	public static String outputOreDictionaryEntry(ItemStack[] list){
		try{
			ItemStack stack = list[0];
		
			int[] ids = OreDictionary.getOreIDs(stack);
			
			for(int i = 0; i < ids.length; i++){
				String potentialEntry = OreDictionary.getOreName(ids[i]);
				List<ItemStack> potentialCognate = OreDictionary.getOres(potentialEntry);
				
				boolean isEqual = potentialCognate.size() == list.length;
				
				if(isEqual) //so far, that is
					for(int j = 0; j < list.length; j++)
						if(potentialCognate.get(j).getItem() != list[j].getItem() && potentialCognate.get(j).getItemDamage() != list[j].getItemDamage())
							isEqual = false;
				
				if(isEqual)
					return "{{O|" + potentialEntry + "}}";
			}
		}catch(ArrayIndexOutOfBoundsException e){
			return null;
		}
		
		return null;
	}
	
	public static String doubleToString(double d){
		String ret = Double.toString(d);
		if(ret.endsWith(".0"))
				ret = ret.replaceAll(".0$", "");
		return ret;
	}
	
	public static String floatToString(float f){
		String ret = Float.toString(f);
		if(ret.endsWith(".0"))
				ret = ret.replaceAll(".0$", "");
		return ret;
	}

	/**
	 * Helper method for formatting lists in infobox parameters. This is here in case the way lists are formatted
	 * changes at any point (as this is currently being discussed at the time of writing these docs).
	 * @param strings The list of strings to format.
	 * @return A formatted string containing the entries in the strings parameter.
	 */
	public static String formatInfoboxList(Iterable<String> strings) {
		return String.join("<br />", strings);
	}
}
