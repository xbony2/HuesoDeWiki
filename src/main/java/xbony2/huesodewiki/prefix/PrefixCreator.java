package xbony2.huesodewiki.prefix;

import net.minecraft.item.ItemStack;
import xbony2.huesodewiki.api.HuesoDeWikiAPI;

public class PrefixCreator {
	public static void init(){
		HuesoDeWikiAPI.prefixes.add(new CorrectTitlePrefix());
		HuesoDeWikiAPI.prefixes.add(new LowercaseTitlePrefix());
	}

	public static String createPrefixes(ItemStack itemstack){
		StringBuilder ret = new StringBuilder();

		HuesoDeWikiAPI.prefixes.stream().filter((prefix) -> prefix.canAdd(itemstack)).forEach((prefix) -> ret.append(prefix.getText(itemstack)).append('\n'));

		return ret.toString();
	}
}
