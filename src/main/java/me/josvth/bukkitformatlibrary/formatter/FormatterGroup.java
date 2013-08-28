package me.josvth.bukkitformatlibrary.formatter;

import me.josvth.bukkitformatlibrary.formatter.Formatter;

import java.util.List;

public class FormatterGroup {

	private final String ID;

	private final List<Formatter> formatters;

	public FormatterGroup(String ID, List<Formatter> formatters) {
		this.ID = ID.toLowerCase();
		this.formatters = formatters;
	}

	public String getID() {
		return ID;
	}

	public String format(String message) {
		for (Formatter formatter : formatters)
			message = formatter.format(message);
		return message;
	}


}
