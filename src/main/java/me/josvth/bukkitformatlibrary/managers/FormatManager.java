package me.josvth.bukkitformatlibrary.managers;

import me.josvth.bukkitformatlibrary.FormattedMessage;
import me.josvth.bukkitformatlibrary.formatter.*;
import me.josvth.bukkitformatlibrary.formatter.Formatter;

import java.util.*;

public class FormatManager {

	protected final Map<String, Class<? extends Formatter>> registeredFormatters = new HashMap<String, Class<? extends Formatter>>();

	protected final Map<String, Formatter> formatters 	= new HashMap<String, Formatter>();
	protected final Map<String, FormatterGroup> groups 	= new HashMap<String, FormatterGroup>();
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
		groups.putAll(manager.groups);
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

	// Group methods
	public void addGroup(FormatterGroup group) {
		groups.put(group.getID(), group);
	}

	public FormatterGroup getGroup(String groupName) {
		return groups.get(groupName.toLowerCase());
	}

	public FormatterGroup getGroup(String groupName, FormatterGroup def) {
		FormatterGroup group = getGroup(groupName);
		if (group == null) return def;
		return group;
	}

	public FormatterGroup getDefaultGroup() {
		return groups.get("default");
	}

	// Message methods
	public void addMessage(String key, String message) {
		messages.put(key.toLowerCase(), message);
	}

	public String getMessage(String key) {
		return messages.get(key.toLowerCase());
	}

	public void addPreFormattedMessage(String key, FormattedMessage message) {
		preFormatted.put(key.toLowerCase(), message);
	}

	public FormattedMessage getPreFormattedMessage(String key) {
		return preFormatted.get(key.toLowerCase());
	}

	public FormattedMessage create(String key) {

		FormattedMessage message = getPreFormattedMessage(key);
		if (message != null) return message;

		return create(getDefaultGroup(), key);

	}

	public FormattedMessage create(String groupName, String key) {
		return create(getGroup(groupName, getDefaultGroup()), key);
	}

	public FormattedMessage create(FormatterGroup group, String key) {

		String message = getMessage(key);

		if (message == null)
			return new FormattedMessage(key);

		if (group == null)
			return new FormattedMessage(message);
		else
			return new FormattedMessage(group.format(message));

	}

	public FormattedMessage createRaw(String message) {
		return new FormattedMessage(getDefaultGroup().format(message));
	}

	public FormattedMessage createRaw(String groupName, String message) {
		return createRaw(getGroup(groupName), message);
	}
	
	public FormattedMessage createRaw(FormatterGroup group, String message) {

		if (group == null)
			return new FormattedMessage(message);
		else
			return new FormattedMessage(group.format(message));

	}
}
