package me.josvth.bukkitformatlibrary.managers;

import me.josvth.bukkitformatlibrary.FormattedMessage;
import me.josvth.bukkitformatlibrary.formatter.Formatter;
import me.josvth.bukkitformatlibrary.formatter.FormatterGroup;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YamlFormatManager extends FormatManager {

	public YamlFormatManager() {
		super();
	}
	
	public YamlFormatManager(FormatManager manager, boolean includeMessages) {
		super(manager, includeMessages);
	}

	public YamlFormatManager(ConfigurationSection formattersSection, ConfigurationSection groupsSection, ConfigurationSection messageSection, String groupIdentifier) {
		loadFormatters(formattersSection);
		loadGroups(groupsSection);
		loadMessages(messageSection, groupIdentifier);
	}

	public void loadFormatters(ConfigurationSection section) {

		for (String key : section.getKeys(false)) {

			Class<? extends Formatter> clazz = getRegisteredFormatter(getType(section, key));

			if (clazz != null) {
				try {

					Formatter formatter = null;

					formatter = clazz.getConstructor(String.class, Map.class).newInstance(key, getSettings(section, key));

					// We add it
					addFormatter(formatter);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
	}

	private String getType(ConfigurationSection section, String formatterID) {

		String type = section.getString(formatterID + ".type");

		if (type != null)
			return type;

		if (section.isString(formatterID + ".parent"))
			type = getType(section, formatterID + ".parent");

		return type;

	}

	private Map<String, Object> getSettings(ConfigurationSection section, String formatterID) {

		Map<String, Object> settings = null;

		if (section.isString(formatterID + ".parent")) {
			settings = getSettings(section, formatterID + ".parent");
		}

		if (section.isConfigurationSection(formatterID + ".settings")) {
			if (settings == null) {
				settings = section.getConfigurationSection(formatterID + ".settings").getValues(false);
			} else {
				settings.putAll(section.getConfigurationSection(formatterID + ".settings").getValues(false));
			}
		}

		return settings;

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

	public void loadMessages(ConfigurationSection section, String groupIdentifier) {

		Pattern pattern = Pattern.compile(String.format("%s[^ ]+%s", groupIdentifier, groupIdentifier));
		
		for (String key : section.getKeys(true)) {
			if (section.isString(key)) {

				String message = section.getString(key);

				// Check if we can pre format this message
				Matcher matcher = pattern.matcher(message);

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

	private static final void putAllUnique(Map<String, Object> mapA, Map<String, Object> mapB) {
		for (Map.Entry<String, Object> entry : mapB.entrySet())
			if (!mapA.containsKey(entry.getKey())) mapA.put(entry.getKey(), entry.getValue());
	}
}
