package me.josvth.bukkitformatlibrary.formatter;

import org.bukkit.ChatColor;

import java.util.Map;

public class FixerFormatter extends Formatter{

	private final String prefix;
	private final String suffix;

	public FixerFormatter(String name, Map<String, Object> settings) {
		super(name, settings);
		prefix = (settings != null && settings.get("prefix") instanceof String)? (String) settings.get("prefix") : "";
		suffix = (settings != null && settings.get("suffix") instanceof String)? (String) settings.get("suffix") : "";
	}

	@Override
	public String format(String message) {
		return prefix + message + suffix;
	}

}
