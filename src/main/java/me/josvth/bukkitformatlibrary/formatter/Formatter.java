package me.josvth.bukkitformatlibrary.formatter;

import java.util.ArrayList;
import java.util.List;

public abstract class Formatter {

	private final String name;

	public Formatter(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public ArrayList<String> formatAll(List<String> list) {
		ArrayList<String> formatted = new ArrayList<String>();
		for (String item : list)
			formatted.add(format(item));
		return formatted;
	}

	public abstract String format(String item);

}
