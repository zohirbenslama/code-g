package org.abstractmeta.code.g.core.config.properties;

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
                String property = key.substring(prefix.length(), key.length());
                result.put(property, properties.get(key));
            }
        }
        return result;
    }

    public static List<String> readAsStringList(Map<String, String> properties, String key) {
        return readAsStringCollection(properties, key, new ArrayList<String>());
    }


    public static Set<String> readAsStringSet(Map<String, String> properties, String key) {
        return readAsStringCollection(properties, key, new HashSet<String>());
    }

    @SuppressWarnings("unchecked")
    public static <T extends Collection> T readAsStringCollection(Map<String, String> properties, String key, T result) {
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

}
