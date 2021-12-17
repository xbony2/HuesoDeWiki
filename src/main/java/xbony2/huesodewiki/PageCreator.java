package xbony2.huesodewiki;

import static xbony2.huesodewiki.Utils.getModName;

import net.minecraft.world.item.ItemStack;
import xbony2.huesodewiki.category.CategoryCreator;
import xbony2.huesodewiki.hatnote.HatnoteCreator;
import xbony2.huesodewiki.infobox.InfoboxCreator;
import xbony2.huesodewiki.recipe.RecipeCreator;

public class PageCreator {
	public static String createPage(ItemStack itemstack){
		StringBuilder page = new StringBuilder();

		page.append(HatnoteCreator.createHatenotes(itemstack));
		page.append(InfoboxCreator.createInfobox(itemstack));
		page.append('\n');
		page.append(FirstSentenceCreator.createFirstSentence(itemstack));
		page.append('\n');
		page.append(RecipeCreator.createRecipes(itemstack));
		page.append('\n');
		page.append("{{Navbox ").append(getModName(itemstack)).append("}}").append('\n');
		page.append('\n');
		page.append(CategoryCreator.createCategories(itemstack)).append('\n');
		page.append('\n');
		page.append("<languages />").append('\n');
		page.append('\n');
		
		return page.toString();
	}
}
