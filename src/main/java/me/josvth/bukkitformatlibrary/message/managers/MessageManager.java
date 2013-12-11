package me.josvth.bukkitformatlibrary.message.managers;

import me.josvth.bukkitformatlibrary.formatter.*;
import me.josvth.bukkitformatlibrary.message.FormattedMessage;
import me.josvth.bukkitformatlibrary.message.MessageHolder;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MessageManager {

    public static final Pattern GROUP_PATTERN = Pattern.compile("%{2}[^ %]+%{2}");
    private final FormatterHolder formatterHolder = new FormatterHolder();
    private final MessageHolder messageHolder = new MessageHolder();

    public MessageManager() {
        formatterHolder.registerFormatter("accent", AccentFormatter.class);
        formatterHolder.registerFormatter("color", ColorFormatter.class);
        formatterHolder.registerFormatter("fixer", FixerFormatter.class);
    }

//    public MessageManager(BasicFormatManager manager, boolean includeMessages) {
//        formatterHolder.getRegisterdFormatters().putAll(manager.registeredFormatters);
//        formatters.putAll(manager.formatters);
//        if(includeMessages) {
//            messages.putAll(manager.messages);
//        }
//    }

    public void unload() {
        formatterHolder.clear();
        messageHolder.clear();
    }

    public FormattedMessage addMessage(String key, String message, boolean preformat) {

        if (preformat) {
            message = preformatMessage(message);
        }

        return messageHolder.addMessage(key.toLowerCase(), message);

    }

    public String preformatMessage(String message) {

        if (getFormatterHolder().getDefaultFormatter() != null) {
            message = getFormatterHolder().getDefaultFormatter().format(message);
        }

        Matcher matcher = GROUP_PATTERN.matcher(message);

        if (matcher.lookingAt()) {

            String[] formatterNames = matcher.group().substring(2, matcher.group().length() - 2).split(Pattern.quote("|"));

            // We take the rest of the string as the message
            message = message.substring(matcher.end());

            // We pre-format the message with the groups
            for (String formatterName : formatterNames) {

                Formatter formatter = getFormatterHolder().getFormatter(formatterName);

                if (formatter != null) {

                    // We pre-format the message following this group
                    message = formatter.format(message);

                }

            }

        }

        return message;
    }

    public FormatterHolder getFormatterHolder() {
        return formatterHolder;
    }

    public MessageHolder getMessageHolder() {
        return messageHolder;
    }

}
