package org.abstractmeta.code.g.core.util;

import org.abstractmeta.code.g.macros.MacroRegistry;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents simple Macro substitution utility.
 *
 * @author Adrian Witas
 */
public class MacroUtil {

    public static  <T extends Collection<String>>  T substitute(MacroRegistry registry, T collection, T newCollection) {
        if (collection == null) return newCollection;
        for (String item : collection) {
            newCollection.add(substitute(registry, item));
        }
        return newCollection;
    }


    public static Map<String, String> substitute(MacroRegistry registry, Map<String, String> map) {
        Map<String, String> result = new HashMap<String, String>();
        if(map == null) return result;
        for(String key: map.keySet()) {
            String value = substitute(registry, map.get(key));
            result.put(key, value);
        }
        return result;
    }


    public static String substitute(MacroRegistry registry, String fragment) {
        if (fragment == null) return null;
        for (String key : registry.getRegistry().keySet()) {
            if (fragment.contains(key)) {
                fragment = fragment.replace(key, registry.get(key));
            }
        }
        return fragment;
    }
}
