package xbony2.huesodewiki.config;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xbony2.huesodewiki.HuesoDeWiki;

public class Config {

	private static final String[] DEFAULT_NAME_CORRECTIONS = new String[]{"Iron Chest", "Iron Chests", "Minecraft", "Vanilla", "Thermal Expansion", "Thermal Expansion 5", "Pressurized Defense", "Pressurized Defence", "Thaumcraft", "Thaumcraft 6"};
	private static final String[] DEFAULT_LINK_CORRECTIONS = new String[]{"Esteemed Innovation", "Esteemed Innovation (mod)"};

	public static boolean use2SpaceStyle;
	public static boolean useStackedCategoryStyle;
	public static boolean printOutputToLog;

	public static Map<String, String> nameCorrections = new HashMap<>();
	public static Map<String, String> linkCorrections = new HashMap<>();

	static Configuration config;

	private static final int CONFIG_VERSION = 1;

	public static void initConfig(File file){
		config = new Configuration(file, String.valueOf(CONFIG_VERSION));
		config.load();

		readConfig();
		updateConfig();
		
		MinecraftForge.EVENT_BUS.register(Config.class);
	}

	private static void readConfig(){
		use2SpaceStyle = config.getBoolean("Use2SpaceStyle", "Main", false, "Use \"2spacestyle\"- put an extra space in headers (like \"== Recipe ==\", as vs \"==Recipe==\").");
		useStackedCategoryStyle = config.getBoolean("UseStackedCategoryStyle", "Main", false, "Use \"stacked\" category style– put each category on its own line.");
		String[] nameCorrections = config.getStringList("NameCorrections", "Main", DEFAULT_NAME_CORRECTIONS, "Name fixes. Is a map- first entry is the mod's internal name, second is the FTB Wiki's name.");
		String[] linkCorrections = config.getStringList("LinkCorrections", "Main", DEFAULT_LINK_CORRECTIONS, "Link fixes. Is a map- first entry is the mod's name, second is the FTB Wiki's page.");
		printOutputToLog = config.getBoolean("PrintOutputToLog", "Main", false, "Enable to print the generated output to the console log- for debugging purposes or as a workaround for OpenJDK bug JDK-8179547 on Linux");

		for(int i = 0; i < nameCorrections.length - 1; i += 2)
			Config.nameCorrections.put(nameCorrections[i], nameCorrections[i + 1]);

		for(int i = 0; i < linkCorrections.length - 1; i += 2)
			Config.nameCorrections.put(linkCorrections[i], linkCorrections[i + 1]);

		if(config.hasChanged())
			config.save();
	}

	private static void updateConfig(){
		int version;

		try {
			if(config.getLoadedConfigVersion() == null)
				version = 0;
			else
				version = Integer.parseInt(config.getLoadedConfigVersion());
		}catch(NumberFormatException e){
			HuesoDeWiki.LOGGER.error("Invalid config version!", e);
			return;
		}

		if(version > CONFIG_VERSION){
			HuesoDeWiki.LOGGER.error("Future config version detected!");
			return;
		}

		if(version <= 0){
			HuesoDeWiki.LOGGER.info("Updating HuesoDeWiki config");

			HuesoDeWiki.LOGGER.info("Resetting name corrections to default");
			config.get("Main", "NameCorrections", DEFAULT_NAME_CORRECTIONS).set(DEFAULT_NAME_CORRECTIONS);

			readConfig();
		}
	}

	@SubscribeEvent
	public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event){
		if(HuesoDeWiki.MODID.equals(event.getModID()))
			readConfig();
	}
}
