package me.josvth.bukkitformatlibrary.formatter;

import java.util.List;

public class FormatterGroup extends Formatter {

	private final List<Formatter> formatters;

	public FormatterGroup(String name, List<Formatter> formatters) {
		super(name);
		this.formatters = formatters;
	}

	@Override
	public String format(String message) {
		for (Formatter formatter : formatters)
			message = formatter.format(message);
		return message;
	}

}
