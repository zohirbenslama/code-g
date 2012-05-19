package org.abstractmeta.code.g.config.loader;

import org.abstractmeta.code.g.code.JavaTypeRegistry;
import org.abstractmeta.code.g.config.Descriptor;

import java.util.Collection;

/**
 *
 * Java type loader.
 *
 * @author Adrian Witas
 */
public interface JavaTypeLoader {

    /**
     * Loads java types into registry for a given descriptor.
     * @param registry java type
     * @param descriptor configuration descriptor
     * @param classLoader class laoder
     * @return collection of loaded java type names
     */
    Collection<String> load(JavaTypeRegistry registry, Descriptor descriptor, ClassLoader classLoader);
}
