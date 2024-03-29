package xbony2.huesodewiki;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Ingredient.Value;
import net.minecraft.world.item.crafting.Ingredient.TagValue;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.registries.IForgeRegistry;
import xbony2.huesodewiki.config.Config;

public class Utils {
	private static final MethodHandle INGREDIENT_ACCEPTED_ITEMS;
	private static final MethodHandle TAGLIST_TAG;

	static {
		try {
			Field field = ObfuscationReflectionHelper.findField(Ingredient.class, "f_43902_"); // values
			INGREDIENT_ACCEPTED_ITEMS = MethodHandles.lookup().unreflectGetter(field);

			field = ObfuscationReflectionHelper.findField(TagValue.class, "f_43959_"); // tag
			TAGLIST_TAG = MethodHandles.lookup().unreflectGetter(field);
		}catch(IllegalAccessException e){
			throw new RuntimeException("Failed to lookup field", e);
		}
	}

	public static String getModName(String modid){
		ModContainer container = ModList.get().getModContainerById(modid).orElse(null);
		if(container == null)
			return "Vanilla";
		else{
			String modName = container.getModInfo().getDisplayName();
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

	public static String getModAbbrevation(IForgeRegistry<?> entry){
		return getModAbbrevation(getModName(entry.getRegistryName().getNamespace()));
	}

	/**
	 * Thanks to <a href="https://github.com/mezz/JustEnoughItems/commit/a8a0405cec5fddf28efaa320b150089b01c155dc">mezz/JEI</a>  for this solution.
	 */
	public static ItemStack getResultItem(Recipe<?> recipe){
		// If there's a null pointer from this then we have much bigger issues than recipes -bony
		return recipe.getResultItem(Minecraft.getInstance().level.registryAccess());
	}

	public static String outputItem(ItemStack itemstack){
		return "{{Gc|mod=" + getModAbbrevation(itemstack) + "|dis=false|" + itemstack.getHoverName().getString() + "}}";
	}

	public static String outputIngredient(Ingredient ingredient){
		StringBuilder ret = new StringBuilder();

		Value[] acceptedItems;
		
		try {
			acceptedItems = (Value[]) INGREDIENT_ACCEPTED_ITEMS.invokeExact(ingredient);
		}catch(Throwable throwable){
			throw new RuntimeException(throwable);
		}
		
		List<TagKey<Item>> tags = new ArrayList<>();
		
		for(Value acceptedItem : acceptedItems){
			if(acceptedItem instanceof TagValue tagList){
				try {
					TagKey<Item> tag = (TagKey<Item>) TAGLIST_TAG.invokeExact(tagList);
					tags.add(tag);
				}catch(Throwable throwable){
					throw new RuntimeException(throwable);
				}
			}
		}
		
		for(TagKey<Item> tag : tags)
			ret.append(outputTag(tag));

		for(ItemStack itemstack : ingredient.getItems())
			if(tags.stream().noneMatch(itemstack::is))
				ret.append(outputItem(itemstack));

		return ret.toString();
	}

	public static String outputTag(TagKey<Item> tag){
		return "{{O|" + tag.location() + "}}";
	}

	public static String outputItemOutput(ItemStack itemstack){
		return "{{Gc|mod=" + getModAbbrevation(itemstack) + "|link=none|" + itemstack.getHoverName().getString() + (itemstack.getCount() != 1 ? "|" + itemstack.getCount() : "") + "}}";
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
	 * @return State corresponding to the item
	 */
	public static BlockState stackToBlockState(ItemStack itemstack){
		return Block.byItem(itemstack.getItem()).defaultBlockState();
	}

	/**
	 * Formats lists in infobox parameters.
	 *
	 * @param strings The list of strings to format.
	 * @return A formatted string containing the entries in the strings parameter.
	 */
	public static String formatInfoboxList(Iterable<String> strings){
		return String.join("<br />", strings);
	}

	/**
	 * @return The ItemStack that the player is currently hovering over. If they are hovering over an empty slot,
	 * are not hovering over a slot or they are hovering over a slot in a non-supported Gui, returns an
	 * empty ItemStack.
	 */
	@SuppressWarnings({"rawtypes", "resource"})
	@Nonnull
	public static ItemStack getHoveredItemStack(){
		Screen currentScreen = Minecraft.getInstance().screen;

		if(currentScreen instanceof AbstractContainerScreen containerScreen){
			Slot hovered = containerScreen.getSlotUnderMouse();

			if(hovered != null)
				return hovered.getItem();
		}

		return ItemStack.EMPTY;
	}

	/**
	 * Adds the provided string to the system clipboard, and logs it if logging of copied strings is enabled
	 *
	 * @param toCopy The string to add to the clipboard
	 */
	@SuppressWarnings("resource")
	public static void copyString(String toCopy){
		Minecraft.getInstance().keyboardHandler.setClipboard(toCopy);

		if(Config.printOutputToLog.get())
			HuesoDeWiki.LOGGER.info("Generated text:\n" + toCopy);
	}

	/**
	 * Returns a letter corresponding to the specified number. (eg. 1 will return 'A')
	 */
	public static char getAlphabetLetter(int index){
		return (char) (index + 'A' - 1);
	}
}
