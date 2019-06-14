package xbony2.huesodewiki.api;

import java.util.ArrayList;
import java.util.List;

import xbony2.huesodewiki.api.category.ICategory;
import xbony2.huesodewiki.api.infobox.IInfoboxParameter;

public class HuesoDeWikiAPI {
	public static List<IWikiRecipe> recipes = new ArrayList<>();
	public static List<IPagePrefix> prefixes = new ArrayList<>();
	public static List<IInfoboxParameter> parameters = new ArrayList<>();
	public static List<ICategory> categories = new ArrayList<>();
}
