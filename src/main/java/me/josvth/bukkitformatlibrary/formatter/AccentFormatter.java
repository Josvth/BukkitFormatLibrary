package me.josvth.bukkitformatlibrary.formatter;

import org.bukkit.ChatColor;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AccentFormatter extends Formatter {

	private static final String DEFAULT_BEGIN = ">>";
	private static final String DEFAULT_END = "<<";
	private static final ChatColor DEFAULT_ACCENT_COLOR = ChatColor.GOLD;

	private final ChatColor accentColor;
	private final String begin;
	private final String end;

	public AccentFormatter(String name) {
		this(name, DEFAULT_BEGIN, DEFAULT_END, DEFAULT_ACCENT_COLOR);
	}

	public AccentFormatter(String name, Map<String, Object> settings) {
		this(
				name,
				(settings.get("begin") instanceof String)? (String)settings.get("begin"): DEFAULT_BEGIN,
				(settings.get("end") instanceof String)? (String)settings.get("end"): DEFAULT_END,
				(settings.get("accent-color") instanceof String)? ChatColor.getByChar((String) settings.get("accent-color")): DEFAULT_ACCENT_COLOR
			);
	}

	public AccentFormatter(String name, String begin, String end, ChatColor accentColor) {
		super(name);
		this.begin = begin;
		this.end = end;
		this.accentColor = accentColor;
	}

	@Override
	public String format(String message) {

		String regex = String.format("(?<!\\w)%s(.(?! %s))+(?<! )%s(?!\\w)", begin, begin, end);

		Matcher matcher = Pattern.compile(regex).matcher(message);

		//TODO make this work soon

		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}
}
