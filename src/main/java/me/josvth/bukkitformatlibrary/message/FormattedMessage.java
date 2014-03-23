package me.josvth.bukkitformatlibrary.message;

import me.josvth.bukkitformatlibrary.formatter.Formatter;
import org.bukkit.command.CommandSender;

import java.util.regex.Matcher;

public class FormattedMessage {

    private final String message;

    public FormattedMessage(Formatter formatter, String message) {
        this.message = formatter.format(message);
    }

    public FormattedMessage(String message) {
        this.message = message;
    }

    public String get() {
        return message;
    }

    public String get(String... arguments) {

        if (getRaw() != null) {

            String message = getRaw();

            for (int i = 1; i < arguments.length; i+= 2) {
                message = message.replaceAll(Matcher.quoteReplacement(arguments[i - 1]), Matcher.quoteReplacement(arguments[i]));
            }

            return message;

        }

        return getRaw();

    }

    public void send(CommandSender sender) {
        if (getRaw() != null) {
            sender.sendMessage(get());
        }
    }

    public void send(CommandSender sender, String... arguments) {
        if (getRaw() != null) {
            sender.sendMessage(get(arguments));
        }
    }

    public String getRaw() {
        return message;
    }
}
