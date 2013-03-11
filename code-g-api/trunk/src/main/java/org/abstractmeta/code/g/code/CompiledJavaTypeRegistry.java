package org.abstractmeta.code.g.code;

import java.util.Collection;

/**
 * Represents CompiledJavaTypeRegistry
 * @author Adrian Witas
 */
public interface CompiledJavaTypeRegistry {

    void register(CompiledJavaType type);

    JavaType get(String typeName);

    boolean isRegistered(String typeName);

    void unregister(String typeName);

    Collection<CompiledJavaType> get();

}
