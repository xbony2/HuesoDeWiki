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
		//And now for the magic
		StringBuilder page = new StringBuilder(InfoboxCreator.createInfobox(itemstack));
		
		page.append('\n');
		page.append(FirstSentenceCreator.createFirstSentence(itemstack));
		page.append(RecipeCreator.createRecipes(itemstack));
		page.append('\n');
		page.append('\n');
		page.append("{{Navbox ").append(getModName(itemstack)).append("}}").append('\n');
		page.append('\n');
		page.append(CategoryCreator.createCategories(itemstack)).append('\n');
		page.append('\n');
		page.append("<languages />").append('\n');
		page.append('\n');
		
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(page.toString()), null);
	}
}
