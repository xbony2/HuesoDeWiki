package xbony2.huesodewiki.api;

import java.util.ArrayList;
import java.util.List;

import xbony2.huesodewiki.api.category.ICategory;
import xbony2.huesodewiki.api.infobox.IInfoboxParameter;
import xbony2.huesodewiki.api.infobox.type.IType;

/**
 * Hooks for addons to use.
 * Do not add things directly, send IMC containing suppliers
 * of instances of these instead. The message method name is listed
 * in the relevant classes.
 */
public class HuesoDeWikiAPI {
	public static List<IWikiRecipe> recipes = new ArrayList<>();
	public static List<IPagePrefix> prefixes = new ArrayList<>();
	public static List<IInfoboxParameter> parameters = new ArrayList<>();
	public static List<ICategory> categories = new ArrayList<>();
	public static List<IType> types = new ArrayList<>();
}
