package me.josvth.bukkitformatlibrary.formatter;

import org.bukkit.ChatColor;

import java.util.Map;

public class ColorFormatter extends Formatter {

	private static final char DEFAULT_COLOR_CHAR = '&';
	private static final ChatColor DEFAULT_MAIN_COLOR = ChatColor.WHITE;

	private final char colorChar;
	private final ChatColor mainColor;

	public ColorFormatter(String name, Map<String, Object> settings) {
		super(name, settings);
		colorChar = (settings != null && settings.get("color-char") instanceof String)? ((String) settings.get("color-char")).charAt(0) : DEFAULT_COLOR_CHAR;
		mainColor = (settings != null && settings.get("main-color") instanceof String)? ChatColor.getByChar((String) settings.get("main-color")) : DEFAULT_MAIN_COLOR;
	}

	@Override
	public String format(String message) {
		return mainColor + ChatColor.translateAlternateColorCodes(colorChar, message);  //To change body of implemented methods use File | Settings | File Templates.
	}
}
