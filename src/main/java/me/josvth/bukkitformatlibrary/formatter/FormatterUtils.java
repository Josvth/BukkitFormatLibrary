package me.josvth.bukkitformatlibrary.formatter;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.NoSuchElementException;

public class FormatterUtils {

    public static Constructor getConstructor(Class<? extends Formatter> formatter) throws NoSuchMethodException {
        return formatter.getConstructor(String.class);
    }

    public static Method getDeserializerMethod(Class<? extends Formatter> formatter) throws NoSuchMethodException {
        final Method method = formatter.getDeclaredMethod("deserialize", String.class, Map.class);
        if (!Formatter.class.isAssignableFrom(method.getReturnType())) {
            throw new NoSuchElementException();
        }
        return method;
    }

}
