package xbony2.huesodewiki.config;

import java.util.*;
import java.util.function.Predicate;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

public class Config {

	private static final List<String> DEFAULT_NAME_CORRECTIONS = Arrays.asList("Iron Chest=Iron Chests", "Minecraft=Vanilla", "Thermal Expansion=Thermal Expansion 5", "Pressurized Defense=Pressurized Defence", "Thaumcraft=Thaumcraft 6");
	private static final List<String> DEFAULT_LINK_CORRECTIONS = Collections.singletonList("Esteemed Innovation=Esteemed Innovation (mod)");

	public static ForgeConfigSpec.BooleanValue use2SpaceStyle;
	public static ForgeConfigSpec.BooleanValue useStackedCategoryStyle;
	public static ForgeConfigSpec.BooleanValue printOutputToLog;
	
	private static ForgeConfigSpec.ConfigValue<List<? extends String>> nameCorrectionsRaw;
	private static ForgeConfigSpec.ConfigValue<List<? extends String>> linkCorrectionsRaw;

	public static Map<String, String> nameCorrections = new HashMap<>();
	public static Map<String, String> linkCorrections = new HashMap<>();

	private Config(ForgeConfigSpec.Builder builder){
		use2SpaceStyle = builder.comment("Use \"2spacestyle\"- put an extra space in headers (like \"== Recipe ==\", as vs \"==Recipe==\").")
				.define("use2SpaceStyle", false);
		useStackedCategoryStyle = builder.comment("Use \"stacked\" category style- put each category on its own line.")
				.define("useStackedCategoryStyle", false);
		printOutputToLog = builder.comment("Enable to print the generated output to the console log- for debugging purposes or as a workaround for OpenJDK bug JDK-8179547 on Linux")
				.define("printOutputToLog", false);

		Predicate<Object> validator = s -> s instanceof String && ((String) s).split("=", 2).length == 2;

		nameCorrectionsRaw = builder.comment("Name fixes. Is a map- first entry is the mod's internal name, second is the FTB Wiki's name.")
				.defineList("nameCorrections", DEFAULT_NAME_CORRECTIONS, validator);
		linkCorrectionsRaw = builder.comment("Link fixes. Is a map- first entry is the mod's name, second is the FTB Wiki's page.")
				.defineList("linkCorrections", DEFAULT_LINK_CORRECTIONS, validator);

	}

	public static final ForgeConfigSpec CLIENT_SPEC;
	public static final Config CLIENT_CONFIG;

	static{
		Pair<Config, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Config::new);
		CLIENT_CONFIG = specPair.getLeft();
		CLIENT_SPEC = specPair.getRight();
	}

	private static void readCorrections(List<? extends String> source, Map<String, String> dest){
		dest.clear();
		for(String s : source){
			String[] split = s.split("=", 2);

			if(split.length != 2)
				continue;

			dest.put(split[0], split[1]);
		}
	}

	public static void onConfigLoad(ModConfig.Loading event){
		readCorrections(linkCorrectionsRaw.get(), linkCorrections);
		readCorrections(nameCorrectionsRaw.get(), nameCorrections);
	}

	public static void onConfigReload(ModConfig.ConfigReloading event){
		readCorrections(linkCorrectionsRaw.get(), linkCorrections);
		readCorrections(nameCorrectionsRaw.get(), nameCorrections);
	}
}
