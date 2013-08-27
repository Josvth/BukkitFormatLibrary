package me.josvth.bukkitformatlibrary;

import me.josvth.bukkitformatlibrary.formatter.AccentFormatter;
import me.josvth.bukkitformatlibrary.formatter.ColorFormatter;
import me.josvth.bukkitformatlibrary.formatter.Formatter;

import org.bukkit.configuration.ConfigurationSection;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormatManager {

	private final String groupIdentifier;

	private final Map<String, Class<? extends Formatter>> registeredFormatters = new HashMap<String, Class<? extends Formatter>>();

	private final Map<String, Formatter> formatters 	= new HashMap<String, Formatter>();
	private final Map<String, FormatterGroup> groups 	= new HashMap<String, FormatterGroup>();
	private final Map<String, String> messages 			= new HashMap<String, String>();
	private final Map<String, FormattedMessage> preFormatted = new HashMap<String, FormattedMessage>();

	public FormatManager() {
		this("%%");
	}

	public FormatManager(String groupIdentifier) {
		this.groupIdentifier = groupIdentifier;

		registerFormatter("accent", AccentFormatter.class);
		registerFormatter("color", ColorFormatter.class);

	}

	public void loadFormatters(ConfigurationSection section) {

		for (String key : section.getKeys(false)) {

			// We get the formatter class
			Class<? extends Formatter> clazz = getRegisteredFormatter(section.getString(key + ".type"));

			if (clazz != null) {
				try {

					Formatter formatter = null;

					formatter = clazz.getConstructor(String.class, Map.class).newInstance(key, section.getConfigurationSection(key + ".settings").getValues(false));

					// We add it
					addFormatter(formatter);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
	}

	public void loadGroups(ConfigurationSection section) {
		for (String key : section.getKeys(false)) {
			if (section.isList(key)) {

				List<Formatter> formatters = new LinkedList<Formatter>();

				// We add all loaded formatters to the list
				for (String formatterName : section.getStringList(key)) {
					Formatter formatter = getFormatter(formatterName);
					if (formatter != null) formatters.add(formatter);
				}

				// And we add the formatter group
				addGroup(new FormatterGroup(key, formatters));

			}
		}
	}

	public void loadMessages(ConfigurationSection section) {

		for (String key : section.getKeys(true)) {
			if (section.isString(key)) {

				String message = section.getString(key);

				// Check if we can pre format this message
				Matcher matcher = Pattern.compile(String.format("%s[^ ]+%s", groupIdentifier, groupIdentifier)).matcher(message);

				if (matcher.lookingAt()) {

					String[] groupNames = matcher.group().split(Pattern.quote("|"));

					// We take the rest of the string as the message
					message = message.substring(matcher.end());

					String preFormat = null;

					// We pre-format the message with the groups
					for (String groupName : groupNames) {
						FormatterGroup group = getGroup(groupName);
						if (group != null) {

							// Only if we found a valid group we initialize the variable
							if (preFormat == null) preFormat = message;

							// We pre-format the message following this group
							preFormat = group.format(preFormat);

						}
					}

					// Only if we could pre-format the message we add it
					if (preFormat != null)
						addPreFormattedMessage(key, new FormattedMessage(preFormat));

				}

				addMessage(key, message);

			}
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

	public String getMessage(String path) {
		return messages.get(path.toLowerCase());
	}

	public void addPreFormattedMessage(String key, FormattedMessage message) {
		preFormatted.put(key.toLowerCase(), message);
	}

	public FormattedMessage getPreFormattedMessage(String path) {
		return preFormatted.get(path.toLowerCase());
	}

	public FormattedMessage create(String path) {

		FormattedMessage message = getPreFormattedMessage(path);
		if (message != null) return message;

		return create(getDefaultGroup(), path);

	}

	public FormattedMessage create(String groupName, String path) {
		return create(getGroup(groupName, getDefaultGroup()), path);
	}

	public FormattedMessage create(FormatterGroup group, String path) {

		String message = getMessage(path);

		if (message == null)
			return new FormattedMessage(path);

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
