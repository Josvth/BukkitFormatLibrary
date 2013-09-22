package me.josvth.bukkitformatlibrary.formatter;

import me.josvth.bukkitformatlibrary.FormattedMessage;
import org.bukkit.ChatColor;

import java.util.ArrayList;
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

	public AccentFormatter(String name, Map<String, Object> settings) {
		super(name, settings);

		begin = (settings.get("begin") instanceof String)? (String)settings.get("begin"): DEFAULT_BEGIN;
		end = (settings.get("end") instanceof String)? (String)settings.get("end"): DEFAULT_END;
		accentColor = (settings.get("accent-color") instanceof String)? ChatColor.getByChar((String) settings.get("accent-color")): DEFAULT_ACCENT_COLOR;
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

}
