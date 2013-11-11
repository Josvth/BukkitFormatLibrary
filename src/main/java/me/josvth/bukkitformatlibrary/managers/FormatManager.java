package me.josvth.bukkitformatlibrary.managers;

import me.josvth.bukkitformatlibrary.FormattedMessage;
import me.josvth.bukkitformatlibrary.formatter.*;
import me.josvth.bukkitformatlibrary.formatter.Formatter;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormatManager {

	public static final Pattern GROUP_PATTERN = Pattern.compile("%{2}[^ %]+%{2}");

	protected final Map<String, Class<? extends Formatter>> registeredFormatters = new HashMap<String, Class<? extends Formatter>>();

	protected final Map<String, Formatter> formatters 	       	= new HashMap<String, Formatter>();
	protected final Map<String, FormattedMessage> messages 		= new HashMap<String, FormattedMessage>();

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
		}
	}

	public void unload() {
		registeredFormatters.clear();
		formatters.clear();
		messages.clear();
	}

	// Formatter methods
	public void registerFormatter(String ID, Class<? extends Formatter> formatter) {

		try {
			getDeserializeMethod(formatter);
			registeredFormatters.put(ID.toLowerCase(), formatter);
			return;
		} catch (NoSuchMethodException e) {

		}

		try {
			getConstructor(formatter);
			registeredFormatters.put(ID.toLowerCase(), formatter);
			return;
		} catch (NoSuchMethodException e) {

		}

		throw new IllegalArgumentException("Formatter does not have proper constructor or deserialize method.");

	}

	protected Constructor getConstructor(Class<? extends Formatter> formatter) throws NoSuchMethodException {
		return formatter.getConstructor(String.class);
	}

	protected Method getDeserializeMethod(Class<? extends Formatter> formatter) throws NoSuchMethodException {
		final Method method = formatter.getDeclaredMethod("deserialize", String.class, Map.class);
		if (!Formatter.class.isAssignableFrom(method.getReturnType())) {
			throw new NoSuchElementException();
		}
		return method;
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
	public Map<String, FormattedMessage> getMessages() {
		return messages;
	}

	public FormattedMessage addMessage(String key, String message, boolean preformat) {

		if (preformat) {
			message = preformatMessage(message);
		}

		FormattedMessage formattedMessage = new FormattedMessage(message);
		messages.put(key.toLowerCase(), formattedMessage);

		return formattedMessage;
	}

	public String preformatMessage(String message) {

		if (getDefaultFormatter() != null) {
			message = getDefaultFormatter().format(message);
		}

		Matcher matcher = GROUP_PATTERN.matcher(message);

		if (matcher.lookingAt()) {

			String[] formatterNames = matcher.group().substring(2, matcher.group().length() - 2).split(Pattern.quote("|"));

			// We take the rest of the string as the message
			message = message.substring(matcher.end());

			// We pre-format the message with the groups
			for (String formatterName : formatterNames) {

				Formatter formatter = getFormatter(formatterName);

				if (formatter != null) {

					// We pre-format the message following this group
					message = formatter.format(message);

				}

			}

		}

		return message;
	}

	public FormattedMessage getMessage(String key) {
		FormattedMessage message = messages.get(key.toLowerCase());
		if (message == null) {
			message = new FormattedMessage(key);
		}
		return message;
	}

}
