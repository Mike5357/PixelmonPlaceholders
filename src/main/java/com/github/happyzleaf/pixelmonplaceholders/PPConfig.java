package com.github.happyzleaf.pixelmonplaceholders;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.io.File;
import java.io.IOException;

public class PPConfig {
	public static boolean disableEggInfo = true;

	private static String disabledEggMessage = "&cThe eggs are disabled.";
	public static Text disabledEggText;
	
	private static String evolutionNotAvailableMessage = "&cThe pixelmon does not evolve with that condition.";
	public static Text evolutionNotAvailableText;
	
	private static String moveNotAvailableMessage = "Attack not available.";
	public static Text moveNotAvailableText;
	
	private static String noneMessage = "None";
	public static Text noneText;
	
	public static int maxFractionDigits = 2;
	public static int minFractionDigits = 0;
	
	private static String teamMemberNotAvailableMessage = "";
	public static Text teamMemberNotAvailableText;

	public static double rayTraceDistance = 80d;

	public static int adjustMissingPokedexCount = 0;
	
	private static ConfigurationLoader<CommentedConfigurationNode> loader;
	private static CommentedConfigurationNode node;
	private static File file;
	
	public static void init(ConfigurationLoader<CommentedConfigurationNode> loader, File file) {
		PPConfig.loader = loader;
		PPConfig.file = file;
		
		loadConfig();
	}
	
	public static void loadConfig() {
		load();

		if (!file.exists() || node.getNode("miscellaneous", "adjustMissingPokedexCount").isVirtual()) {
			saveConfig();
		}
		
		ConfigurationNode miscellaneous = node.getNode("miscellaneous");
		disableEggInfo = miscellaneous.getNode("disableEggInfo").getBoolean();
		maxFractionDigits = miscellaneous.getNode("maxFractionDigits").getInt();
		minFractionDigits = miscellaneous.getNode("minFractionDigits").getInt();
		rayTraceDistance = miscellaneous.getNode("rayTraceDistance").getDouble();
		adjustMissingPokedexCount = miscellaneous.getNode("adjustMissingPokedexCount").getInt();
		
		ConfigurationNode messages = node.getNode("messages");
		disabledEggMessage = messages.getNode("disabledEgg").getString();
		evolutionNotAvailableMessage = messages.getNode("evolutionNotAvailable").getString();
		moveNotAvailableMessage = messages.getNode("moveNotAvailable").getString();
		noneMessage = messages.getNode("none").getString();
		teamMemberNotAvailableMessage = messages.getNode("teamMemberNotAvailable").getString();
		
		disabledEggText = deserialize(disabledEggMessage);
		evolutionNotAvailableText = deserialize(evolutionNotAvailableMessage);
		moveNotAvailableText = deserialize(moveNotAvailableMessage);
		noneText = deserialize(noneMessage);
		teamMemberNotAvailableText = deserialize(teamMemberNotAvailableMessage);
	}
	
	public static void saveConfig() {
		CommentedConfigurationNode miscellaneous = node.getNode("miscellaneous");
		miscellaneous.getNode("disableEggInfo").setValue(disableEggInfo);
		miscellaneous.getNode("maxFractionDigits").setComment("How many digits should be used in decimal values. 2 means 1.12, 3 means 1.123 etc.").setValue(maxFractionDigits);
		miscellaneous.getNode("minFractionDigits").setComment("The opposite of maxFractionDigits, fills with zeros until there are enough digits. 2 means 1.00, 3 means 1.000 etc.").setValue(minFractionDigits);
		miscellaneous.getNode("rayTraceDistance").setComment("How far the ray trace will look for pokémon. Default: 80.0").setValue(rayTraceDistance);
		miscellaneous.getNode("adjustMissingPokedexCount").setComment("Subtract this number from the total dex size count to fix incorrectly calculated dex percentages - MAKE SURE TO PROPERLY ADJUST THIS EVERY UPDATE THAT RESOLVES THIS ISSUE OR ADDS NEW POKEMON").setValue(adjustMissingPokedexCount);
		
		CommentedConfigurationNode messages = node.getNode("messages");
		messages.getNode("disabledEgg").setValue(disabledEggMessage);
		messages.getNode("evolutionNotAvailable").setValue(evolutionNotAvailableMessage);
		messages.getNode("moveNotAvailable").setValue(moveNotAvailableMessage);
		messages.getNode("none").setValue(noneMessage);
		messages.getNode("teamMemberNotAvailable").setValue(teamMemberNotAvailableMessage);
		
		save();
	}
	
	private static Text deserialize(String message) {
		return TextSerializers.FORMATTING_CODE.deserialize(message);
	}
	
	private static void load() {
		try {
			node = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void save() {
		try {
			loader.save(node);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
