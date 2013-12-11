package me.josvth.bukkitformatlibrary.message;

import java.util.HashMap;
import java.util.Map;

public class MessageHolder {

    private final Map<String, FormattedMessage> messages = new HashMap<String, FormattedMessage>();

    public void clear() {
        messages.clear();
    }

    public Map<String, FormattedMessage> getMessages() {
        return messages;
    }

    public FormattedMessage addMessage(String key, String message) {

        FormattedMessage formattedMessage = new FormattedMessage(message);
        messages.put(key.toLowerCase(), formattedMessage);

        return formattedMessage;
    }

    public FormattedMessage getMessage(String key) {
        FormattedMessage message = messages.get(key.toLowerCase());
        if (message == null) {
            message = new FormattedMessage(key);
        }
        return message;
    }


}
