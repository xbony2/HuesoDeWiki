package xbony2.huesodewiki;

import static xbony2.huesodewiki.Utils.getModName;

import net.minecraft.item.ItemStack;
import xbony2.huesodewiki.config.Config;
import xbony2.huesodewiki.infobox.parameters.TypeParameter;

public class FirstSentenceCreator {
	public static String createFirstSentence(ItemStack itemstack){
		String name = itemstack.getDisplayName();
		String modName = getModName(itemstack);
		String type = new TypeParameter().getParameterText(itemstack);
		String linkFix = Config.linkCorrections.get(modName); //is null if there isn't a change required.
		
		StringBuilder ret = new StringBuilder();
		ret.append("The '''");
		ret.append(name);
		ret.append("''' ");
		ret.append(name.endsWith("s") ? "are " : "is ");
		ret.append(type.startsWith("a") || type.startsWith("e") || type.startsWith("i") || type.startsWith("o") || type.startsWith("u") ? "an " : "a ");//blerg
		ret.append(type);
		ret.append(" added by [[").append((linkFix != null ? linkFix + "|" : ""));
		ret.append(modName);
		ret.append("]].\n");
		return ret.toString();
	}
}
