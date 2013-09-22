package me.josvth.bukkitformatlibrary.managers;

import me.josvth.bukkitformatlibrary.FormattedMessage;
import me.josvth.bukkitformatlibrary.formatter.*;
import me.josvth.bukkitformatlibrary.formatter.Formatter;

import java.util.*;

public class FormatManager {

	protected final Map<String, Class<? extends Formatter>> registeredFormatters = new HashMap<String, Class<? extends Formatter>>();

	protected final Map<String, Formatter> formatters 	= new HashMap<String, Formatter>();
	protected final Map<String, String> messages 		= new HashMap<String, String>();
	protected final Map<String, FormattedMessage> preFormatted = new HashMap<String, FormattedMessage>();

	public FormatManager() {
		registerFormatter("accent", AccentFormatter.class);
		registerFormatter("color", ColorFormatter.class);
		registerFormatter("fixer", FixerFormatter.class);
	}
	
	public FormatManager(FormatManager manager, boolean includeMessages) {
		registeredFormatters.putAll(manager.registeredFormatters);
		formatters.putAll(manager.formatters);
		if(includeMessages) {
			messages.putAll(manager.messages);
			preFormatted.putAll(manager.preFormatted);
		}
	}

	// Formatter methods
	public void registerFormatter(String ID, Class<? extends Formatter> formatter) {

		// We check if we can construct this formatter
		try {

			formatter.getConstructor(String.class, Map.class);

			// We register the formatter
			registeredFormatters.put(ID.toLowerCase(), formatter);

		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}

	}

	public Class<? extends Formatter> getRegisteredFormatter(String ID) {
		return registeredFormatters.get(ID.toLowerCase());
	}

	public void addFormatter(Formatter formatter) {
		formatters.put(formatter.getName(), formatter);
	}

	public Formatter getFormatter(String formatterName) {
		return formatters.get(formatterName.toLowerCase());
	}

	public Map<String, Formatter> getFormatters() {
		return formatters;
	}

	public Formatter getDefaultFormatter() {
		return formatters.get("default");
	}

	// Message methods
	public void addMessage(String key, String message) {
		messages.put(key.toLowerCase(), message);
	}

	public void addPreFormattedMessage(String key, FormattedMessage message) {
		preFormatted.put(key.toLowerCase(), message);
	}

	public String getUnformattedMessage(String key) {
		return messages.get(key.toLowerCase());
	}

	public FormattedMessage getPreformattedMessage(String key) {
		return preFormatted.get(key.toLowerCase());
	}

	public FormattedMessage getMessage(String key) {

		FormattedMessage message = getPreformattedMessage(key);

		if (message != null) return message;

		return getMessage("default", key);

	}

	public FormattedMessage getMessage(String formatterName, String key) {

		final String message = getUnformattedMessage(key);

		if (message == null)
			return new FormattedMessage(key);

		final Formatter formatter = getFormatter(formatterName);

		if (formatter == null)
			return new FormattedMessage(message);
		else
			return formatter.format(message);

	}

}
