package org.abstractmeta.code.g.core.util;

import java.util.Collection;

/**
 * A collection utility class
 * @author Adrian Witas
 */
public class CollectionUtil {

    public static boolean isEmpty(Collection collection) {
        return collection == null || collection.isEmpty();
    }

}
