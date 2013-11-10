package me.josvth.bukkitformatlibrary.formatter;

import org.bukkit.ChatColor;

import java.util.Map;

public class FixerFormatter extends Formatter{

	private String prefix = null;
	private String suffix = null;

	public FixerFormatter(String name, String prefix, String suffix) {
		super(name);
		this.prefix = prefix;
		this.suffix = suffix;
	}

	@Override
	public String format(String message) {
		final StringBuilder builder = new StringBuilder();
		if (prefix != null) {
			builder.append(prefix);
		}

		builder.append(message);

		if (suffix != null) {
			builder.append(suffix);
		}

		return builder.toString();
	}

	public static final FixerFormatter deserialize(String name, Map<String, Object> settings) {

		String prefix = null;
		if (settings.get("fixer-prefix") instanceof String) {
			prefix = ((String) settings.get("fixer-prefix"));
		}

		String suffix = null;
		if (settings.get("fixer-suffix") instanceof String) {
			suffix = ((String) settings.get("fixer-suffix"));
		}

		return new FixerFormatter(name, prefix, suffix);

	}

}
