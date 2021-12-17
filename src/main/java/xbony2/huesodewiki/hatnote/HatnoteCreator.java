package xbony2.huesodewiki.hatnote;

import net.minecraft.world.item.ItemStack;
import xbony2.huesodewiki.api.HuesoDeWikiAPI;

public class HatnoteCreator {
	public static void init(){
		HuesoDeWikiAPI.hatnotes.add(new CorrectTitleHatnote());
		HuesoDeWikiAPI.hatnotes.add(new LowercaseTitleHatnote());
	}

	public static String createHatenotes(ItemStack itemstack){
		StringBuilder ret = new StringBuilder();

		HuesoDeWikiAPI.hatnotes.stream().filter((hatnote) ->
			hatnote.canAdd(itemstack)).forEach((prefix) ->
				ret.append(prefix.getText(itemstack)).append('\n'));

		return ret.toString();
	}
}
