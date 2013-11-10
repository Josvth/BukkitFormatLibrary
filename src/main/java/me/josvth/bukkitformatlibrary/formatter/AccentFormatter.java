package me.josvth.bukkitformatlibrary.formatter;

import me.josvth.bukkitformatlibrary.FormattedMessage;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AccentFormatter extends Formatter {

	private static final ChatColor DEFAULT_ACCENT_COLOR = ChatColor.GOLD;
	private static final String DEFAULT_BEGIN = ">>";
	private static final String DEFAULT_END = "<<";

	private ChatColor accentColor = DEFAULT_ACCENT_COLOR;
	private String begin = DEFAULT_BEGIN;
	private String end = DEFAULT_END;

	public AccentFormatter(String name) {
		super(name);
	}

	public AccentFormatter(String name, ChatColor accentColor, String begin, String end) {
		super(name);
		this.accentColor = accentColor;
		this.begin = begin;
		this.end = end;
	}

	@Override
	public String format(String message) {

		String regex = String.format("(?<!\\w)%s(.(?! %s))+(?<! )%s(?!\\w)", begin, begin, end);

		Matcher matcher = Pattern.compile(regex).matcher(message);

		ArrayList<String> parts = new ArrayList<String>();

		int lastPartEnd = 0;

		while (matcher.find()) {

			if (matcher.start() == 0) // if we found a highlight at the start we first add an empty string
				parts.add("");
			else
				parts.add(message.substring(lastPartEnd, matcher.start())); // if not we add the characters between the previous highligh and this one

			lastPartEnd = matcher.end(); // we remember the end of the last highlight

			parts.add(matcher.group().substring(begin.length(), matcher.group().length() - end.length())); // we add the highlighted part

		}

		if (lastPartEnd < message.length())
			parts.add(message.substring(lastPartEnd, message.length()));

		String lastColors = "";

		StringBuilder result = new StringBuilder(parts.get(0));

		for(int i = 1; i < parts.size(); i++) { // cycle through all positions

			if(i % 2 == 0) {// is position is even, append text color
				if (lastColors == "")
					result.append(ChatColor.RESET);
				else
					result.append(lastColors);
			} else {// if position is odd, append accent color
				lastColors = ChatColor.getLastColors(result.toString());
				result.append(accentColor);
			}

			result.append(parts.get(i));

		}

		return result.toString();

	}

	public static final AccentFormatter deserialize(String name, Map<String, Object> settings) {

		ChatColor chatColor = DEFAULT_ACCENT_COLOR;
		if (settings.get("accent-color") instanceof String) {
			chatColor = ChatColor.getByChar((String) settings.get("accent-color"));
		}

		String begin = DEFAULT_BEGIN;
		if (settings.get("accent-begin") instanceof String) {
			begin = (String) settings.get("accent-begin");
		}

		String end = DEFAULT_END;
		if (settings.get("accent-end") instanceof String) {
			end = (String) settings.get("accent-end");
		}

		return new AccentFormatter(name, chatColor, begin, end);

	}
}
