package xbony2.huesodewiki;

import static xbony2.huesodewiki.Utils.getModName;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import xbony2.huesodewiki.category.CategoryCreator;
import xbony2.huesodewiki.infobox.InfoboxCreator;
import xbony2.huesodewiki.recipe.RecipeCreator;

public class PageCreator {
	public static void createPage(ItemStack itemstack){
		String name = itemstack.getDisplayName();
		String modName = getModName(itemstack);
		
		String linkFix = HuesoDeWiki.linkCorrections.get(modName); //is null if there isn't a change required.
		String type = itemstack.getItem() instanceof ItemBlock ? "block" : "item";
		
		//And now for the magic
		String page = InfoboxCreator.createInfobox(itemstack);
		
		page += "\n";
		page += "The '''" + name + "''' is " + (type == "block" ? "a block" : "an item") + " added by [[" + (linkFix != null ? linkFix + "|" : "") + modName + "]]." + "\n";
		page += RecipeCreator.createRecipes(itemstack);
		page += "\n";
		page += "\n";
		page += "{{Navbox " + modName + "}}" + "\n";
		page += "\n";
		page += CategoryCreator.createCategories(itemstack) + "\n";
		page += "\n";
		page += "<languages />" + "\n";
		page += "\n";
		
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(page), null);
	}
}
