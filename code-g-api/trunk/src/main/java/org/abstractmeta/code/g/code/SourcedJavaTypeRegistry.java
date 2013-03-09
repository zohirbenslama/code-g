package org.abstractmeta.code.g.code;

import java.util.Collection;

/**
 * Represents SourcedJavaTypeRegistry
 * @author Adrian Witas
 */
public interface SourcedJavaTypeRegistry {

    void register(SourcedJavaType type);

    JavaType get(String typeName);

    boolean isRegistered(String typeName);

    void unregister(String typeName);

    Collection<SourcedJavaType> get();

}
