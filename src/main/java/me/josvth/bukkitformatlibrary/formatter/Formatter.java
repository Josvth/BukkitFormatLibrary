package me.josvth.bukkitformatlibrary.formatter;

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
