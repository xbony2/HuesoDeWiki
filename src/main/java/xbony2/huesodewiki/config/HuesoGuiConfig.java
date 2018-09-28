package xbony2.huesodewiki.config;

import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
import xbony2.huesodewiki.HuesoDeWiki;

//Adapted from Quat's mods, who adapted it from Choonster's TestMod3, who adapted it from Ender IO.
public class HuesoGuiConfig extends GuiConfig {

	public HuesoGuiConfig(GuiScreen parent) {
		super(parent, getConfigElements(), HuesoDeWiki.MODID, false, false, "HuesoDeWiki Config");
	}

	private static List<IConfigElement> getConfigElements() {
		Configuration c = Config.config;

		return c.getCategoryNames().stream().filter(name -> !c.getCategory(name).isChild()).map(name -> new ConfigElement(c.getCategory(name).setLanguageKey(HuesoDeWiki.MODID + ".config." + name))).collect(Collectors.toList());
	}

}
