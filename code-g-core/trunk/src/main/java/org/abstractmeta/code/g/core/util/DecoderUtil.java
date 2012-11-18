package org.abstractmeta.code.g.core.util;


import java.util.*;

/**
 * Represents DecoderUtil
 *
 * @author Adrian Witas
 */
public class DecoderUtil {


    public static Map<String, String> matchWithPrefix(Map<String, String> properties, String prefix) {
        Map<String, String> result = new HashMap<String, String>();
        for (String key : properties.keySet()) {
            if (key.startsWith(prefix)) {
                int offset = Math.min(prefix.length() + 1, key.length());
                String property = key.substring(offset, key.length());
                result.put(property, properties.get(key));
            }
        }
        return result;
    }


    public static Map<Integer, Class> matchWithPrefixAsIntegerClassMap(Map<String, String> properties, String prefix) {
        Map<Integer, Class> result = new HashMap<Integer, Class>();
        for (String key : properties.keySet()) {
            if (key.startsWith(prefix)) {
                String property = key.substring(prefix.length(), key.length());
                String value = properties.get(key);
                try {
                    result.put(Integer.parseInt(property), Class.forName(value));
                } catch (ClassNotFoundException e) {
                    throw new IllegalStateException("Failed to load class " + value + " for " + key);
                }
            }
        }
        return result;
    }

    public static List<String> readStringList(Map<String, String> properties, String key) {
        return readStringCollection(properties, key, new ArrayList<String>());
    }


    public static Set<String> readStringSet(Map<String, String> properties, String key) {
        return readStringCollection(properties, key, new HashSet<String>());
    }

    @SuppressWarnings("unchecked")
    public static <T extends Collection> T readStringCollection(Map<String, String> properties, String key, T result) {
        if (!properties.containsKey(key)) {
            return result;
        }
        String value = properties.get(key);
        if (value == null || value.isEmpty()) {
            return result;
        }

        String[] items = value.split(",");
        for (String item : items) {
            result.add(item.trim());
        }
        return result;
    }


    public static String readStringRequired(Map<String, String> properties, String name) {
        String result = properties.get(name);
        if (result == null) {
            throw new IllegalStateException("property " + name + " is required");
        }
        return result;
    }


    public static Class readClassRequired(Map<String, String> properties, String name) {
        String result = properties.get(name);
        if (result == null) {
            throw new IllegalStateException("property " + name + " is required");
        }
        try {
            return Class.forName(result);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Failed to load class" + result, e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T extends Enum> T readEnum(Map<String, String> options, Class<T> implementationKindClass, String name, String defaultValue) {
        String result = options.get(name);
        if (result == null) {
            result = defaultValue;
        }
        return implementationKindClass.cast(Enum.valueOf(implementationKindClass, result.toUpperCase()));
    }

    public static String readString(Map<String, String> options, String name, String defaultValue) {
        String result = options.get(name);
        if (result == null) {
            return defaultValue;
        }
        return result;
    }
}
