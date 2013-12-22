package me.josvth.bukkitformatlibrary.formatter;

import org.bukkit.ChatColor;

import java.util.Map;

public class ColorFormatter extends Formatter {

	private static final char DEFAULT_COLOR_CHAR = '&';
	private static final ChatColor DEFAULT_MAIN_COLOR = null;

	private char colorChar = DEFAULT_COLOR_CHAR;
	private ChatColor mainColor = null;

	public ColorFormatter(String name) {
		super(name);
	}

	public ColorFormatter(String name, char colorChar, ChatColor mainColor) {
		super(name);
		this.colorChar = colorChar;
		this.mainColor = mainColor;
	}

	@Override
	public String format(String message) {
        final StringBuilder builder = new StringBuilder();
		if (mainColor != null) {
			builder.append(mainColor);
		}
		builder.append(ChatColor.translateAlternateColorCodes(colorChar, message));
		return builder.toString();
	}

	public static final ColorFormatter deserialize(String name, Map<String, Object> settings) {

		char colorChar = DEFAULT_COLOR_CHAR;
		if (settings.get("color-char") instanceof String) {
			colorChar = ((String) settings.get("color-char")).charAt(0);
		}

		ChatColor mainColor = DEFAULT_MAIN_COLOR;
		if (settings.get("color-color") instanceof String) {
			mainColor = ChatColor.getByChar((String) settings.get("color-color"));
		}

		return new ColorFormatter(name, colorChar, mainColor);

	}
}
