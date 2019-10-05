package xbony2.huesodewiki;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.oredict.OreDictionary;
import xbony2.huesodewiki.config.Config;

public class Utils {
	public static String getModName(String modid){
		ModContainer container = Loader.instance().getIndexedModList().get(modid);
		
		if(container == null)
			return "Vanilla";
		else{
			String modName = container.getName();
			return Config.nameCorrections.get(modName) != null ? Config.nameCorrections.get(modName) : modName;
		}
	}
	
	public static String getModName(ItemStack itemstack){
		return getModName(itemstack.getItem().getCreatorModId(itemstack));
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
			List<String> validEntries = new ArrayList<>();
		
			int[] ids = OreDictionary.getOreIDs(stack);
			
			for(int i = 0; i < ids.length; i++){
				String potentialEntry = OreDictionary.getOreName(ids[i]);
				List<ItemStack> potentialCognate = OreDictionary.getOres(potentialEntry);
				
				NonNullList<ItemStack> potentialCognateExploded = NonNullList.create();
				for(ItemStack itemStack : potentialCognate)
					if(itemStack.getMetadata() == OreDictionary.WILDCARD_VALUE)
						itemStack.getItem().getSubItems(CreativeTabs.SEARCH, potentialCognateExploded);
					else
						potentialCognateExploded.add(itemStack);
					
				boolean isEqual = potentialCognateExploded.size() == list.length;
				
				if(isEqual) //so far, that is
					for(int j = 0; j < list.length; j++)
						if(potentialCognateExploded.get(j).getItem() != list[j].getItem() && potentialCognateExploded.get(j).getItemDamage() != list[j].getItemDamage())
							isEqual = false;
				
				if(isEqual)
					validEntries.add("{{O|" + potentialEntry + "}}");
			}
			if(validEntries.size() == 1) 
				return validEntries.get(0);
			if(validEntries.size() > 1) 
				return " One of:  " + String.join("", validEntries);
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
	 * @param itemstack Stack to convert into blockstate
	 * @return State corresponding to the item, taking metadata into account
	 */
	@SuppressWarnings("deprecation")
	public static IBlockState stackToBlockState(ItemStack itemstack){
		Block block = Block.getBlockFromItem(itemstack.getItem());
		
		try {
			return block.getStateFromMeta(itemstack.getMetadata());
		}catch(Exception e){
			return block.getDefaultState();
		}
	}

	/**
	 * Formats lists in infobox parameters.
	 * @param strings The list of strings to format.
	 * @return A formatted string containing the entries in the strings parameter.
	 */
	public static String formatInfoboxList(Iterable<String> strings){
		return String.join("<br />", strings);
	}

	/**
	 * @return The ItemStack that the player is currently hovering over. If they are hovering over an empty slot,
	 * 		   are not hovering over a slot or they are hovering over a slot in a non-supported Gui, returns an
	 * 		   empty ItemStack.
	 */
	@Nonnull
	public static ItemStack getHoveredItemStack(){
		GuiScreen currentScreen = Minecraft.getMinecraft().currentScreen;
		
		if(currentScreen instanceof GuiContainer){
			Slot hovered = ((GuiContainer)currentScreen).getSlotUnderMouse();
			
			if(hovered != null)
				return hovered.getStack();
		}
		
		return ItemStack.EMPTY;
	}

	/**
	 * Adds the provided string to the system clipboard, and logs it if logging of copied strings is enabled 
	 * @param toCopy The string to add to the clipboard
	 */
	public static void copyString(String toCopy){
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(toCopy), null);
		
		if(Config.printOutputToLog) 
			HuesoDeWiki.LOGGER.info("Generated text:\n" + toCopy);
	}

	/**
	 * Returns a letter corresponding to the specified number. (eg. 1 will return 'A')
	 */
	public static char getAlphabetLetter(int index){
		return (char)(index + 'A' - 1);
	}
}
