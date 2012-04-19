package org.abstractmeta.code.g.code;


import java.util.Collection;


/**
 * Java type registry.
 *
 */
public interface JavaTypeRegistry {

    void register(JavaType type);

    JavaType get(String typeName);

    boolean isRegistered(String typeName);

    void unregister(String typeName);

    Collection<JavaType> get();

}
