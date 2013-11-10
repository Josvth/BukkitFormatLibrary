package me.josvth.bukkitformatlibrary.formatter;

import me.josvth.bukkitformatlibrary.FormattedMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class Formatter {

	private final String name;

	public Formatter(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public abstract String format(String message);

}
