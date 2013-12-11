package me.josvth.bukkitformatlibrary.message.managers;

import me.josvth.bukkitformatlibrary.formatter.Formatter;
import me.josvth.bukkitformatlibrary.formatter.FormatterGroup;
import me.josvth.bukkitformatlibrary.formatter.FormatterUtils;
import org.bukkit.configuration.ConfigurationSection;

import java.util.*;

public class YamlMessageManager extends MessageManager {

	public YamlMessageManager() {
		super();
	}

//	public YamlMessageManager(BasicFormatManager manager, boolean includeMessages) {
//		super(manager, includeMessages);
//	}

	public YamlMessageManager(ConfigurationSection formattersSection, ConfigurationSection messageSection) {
		loadFormatters(formattersSection);
		loadMessages(messageSection);
	}

	public void loadFormatters(ConfigurationSection section) {

		Set<String> unloadedFormatters = section.getKeys(false);

		int iteration = 0;

		while (iteration < 10) {

			Iterator<String> iterator = unloadedFormatters.iterator();

			while (iterator.hasNext()) {

				String name = iterator.next();

				String type = getType(section, name, null);

				if ("group".equalsIgnoreCase(type)) {

					try {

						List<String> formatterNames = getFormatterNames(section, name, null);

						if (formatterNames != null) {

							// Check if all groups formatters are loaded
							if (getFormatterHolder().getFormatters().keySet().containsAll(formatterNames)) {

								List<Formatter> groupFormatters = new ArrayList<Formatter>();

								// Make formatter list
								for (String formatter : formatterNames) {
									groupFormatters.add(getFormatterHolder().getFormatter(formatter));
								}

								// Add group formatter
                                getFormatterHolder().getFormatters().put(name, new FormatterGroup(name, groupFormatters));

								// Remove from unloaded
								iterator.remove();

							}

						} else {

							// Remove from unloaded
							iterator.remove();

						}


					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					}

				} else if (type != null) {

					Class<? extends Formatter> clazz = getFormatterHolder().getRegisteredFormatter(type);

					if (clazz != null) {

						try {
                            getFormatterHolder().addFormatter((Formatter) FormatterUtils.getDeserializerMethod(clazz).invoke(name, getSettings(section, name, null)));
                       	} catch (Exception dese) {
							try {
                                getFormatterHolder().addFormatter((Formatter) FormatterUtils.getConstructor(clazz).newInstance(name));
							} catch (Exception cone) {

							}
						}

					}

					// Remove from unloaded
					iterator.remove();

				}

			}

			iteration++;

		}

	}


    private List<String> getFormatterNames(ConfigurationSection section, String groupName, Set<String> children) throws IllegalArgumentException {

		List<String> formatters = null;

		if (section.isString(groupName + ".parent")) {
			if (children == null) children = new HashSet<String>();
			children.add(groupName);
			formatters = getFormatterNames(section, groupName + ".parent", children);
		}

		List<String> groupFormatters = section.getStringList(groupName + ".formatters");

		if (groupFormatters != null) {
			if (formatters == null) {
				formatters = groupFormatters;
			} else {
				formatters.addAll(groupFormatters);
			}
		}

		if (children != null) {
			for (String formatter : formatters) {
				if (children.contains(formatter))
					throw new IllegalArgumentException(formatter + " loops with " + groupName + "!");
			}
		}

		return formatters;

	}

	private String getType(ConfigurationSection section, String formatterName, Set<String> children) throws IllegalArgumentException {

		String type = section.getString(formatterName + ".type");

		if (type != null)
			return type;

		String parentName = section.getString(formatterName + ".parent");

		if (parentName != null) {

			if (children == null) {
				children = new HashSet<String>();
			} else {
				for (String child : children)
					if (parentName.equals(child))
						throw new IllegalArgumentException(formatterName + " loops with " + parentName);
			}

			children.add(formatterName);

			type = getType(section, parentName, children);

		}

		return type;

	}

	private Map<String, Object> getSettings(ConfigurationSection section, String formatterName, Set<String> children) throws IllegalArgumentException {

		Map<String, Object> settings = null;

		String parentName = section.getString(formatterName + ".parent");

		if (parentName != null) {

			if (children == null) {
				children = new HashSet<String>();
			} else {
				for (String child : children)
					if (parentName.equals(child))
						throw new IllegalArgumentException(formatterName + " loops with " + parentName);
			}

			children.add(formatterName);

			settings = getSettings(section, parentName, children);

		}

		if (section.isConfigurationSection(formatterName + ".settings")) {
			if (settings == null) {
				settings = section.getConfigurationSection(formatterName + ".settings").getValues(false);
			} else {
				settings.putAll(section.getConfigurationSection(formatterName + ".settings").getValues(false));
			}
		}

		return settings;

	}

	public void loadMessages(ConfigurationSection section) {

		for (String key : section.getKeys(true)) {
			if (section.isString(key)) {
                addMessage(key, section.getString(key), true);
			}
		}

	}


}
