package xbony2.huesodewiki.prefix;

import net.minecraft.item.ItemStack;
import xbony2.huesodewiki.api.IPagePrefix;

import java.util.ArrayList;
import java.util.List;

public class PrefixCreator {
	private static List<IPagePrefix> prefixes = new ArrayList<>();

	static {
		prefixes.add(new CorrectTitlePrefix());
		prefixes.add(new LowercaseTitlePrefix());
	}

	public static String createPrefixes(ItemStack itemstack){
		StringBuilder ret = new StringBuilder();

		prefixes.stream().filter((prefix) -> prefix.canAdd(itemstack)).forEach((prefix) -> ret.append(prefix.getText(itemstack)).append('\n'));

		return ret.toString();
	}
}
