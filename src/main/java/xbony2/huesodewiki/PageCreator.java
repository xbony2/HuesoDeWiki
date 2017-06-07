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
		
		//And now for the magic
		StringBuilder page = new StringBuilder(InfoboxCreator.createInfobox(itemstack));
		
		page.append("\n");
		page.append("The '''" + name + "''' is " + (itemstack.getItem() instanceof ItemBlock ? "a block" : "an item") + " added by [[" + (linkFix != null ? linkFix + "|" : "") + modName + "]]." + "\n");
		page.append(RecipeCreator.createRecipes(itemstack));
		page.append("\n");
		page.append("\n");
		page.append("{{Navbox " + modName + "}}" + "\n");
		page.append("\n");
		page.append(CategoryCreator.createCategories(itemstack) + "\n");
		page.append("\n");
		page.append("<languages />" + "\n");
		page.append("\n");
		
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(page.toString()), null);
	}
}
