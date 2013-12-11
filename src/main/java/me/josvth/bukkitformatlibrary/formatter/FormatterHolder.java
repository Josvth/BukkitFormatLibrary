package me.josvth.bukkitformatlibrary.formatter;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class FormatterHolder {

    protected final Map<String, Class<? extends Formatter>> registeredFormatters = new HashMap<String, Class<? extends Formatter>>();
    protected final Map<String, Formatter> formatters = new HashMap<String, Formatter>();

    public void clear() {
        registeredFormatters.clear();
        formatters.clear();
    }

    // Formatter methods
    public void registerFormatter(String ID, Class<? extends Formatter> formatter) {

        try {
            FormatterUtils.getDeserializerMethod(formatter);
            registeredFormatters.put(ID.toLowerCase(), formatter);
            return;
        } catch (NoSuchMethodException e) {

        }

        try {
            FormatterUtils.getConstructor(formatter);
            registeredFormatters.put(ID.toLowerCase(), formatter);
            return;
        } catch (NoSuchMethodException e) {

        }

        throw new IllegalArgumentException("Formatter does not have proper constructor or deserialize method.");

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


}
