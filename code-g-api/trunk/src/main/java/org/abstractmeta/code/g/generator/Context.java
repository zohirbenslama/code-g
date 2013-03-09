package org.abstractmeta.code.g.generator;

/**
 * Represents Code Generation Context
 * @author Adrian Witas
 */
public interface Context {

    <T> T getOptional(Class<T> key);

    <T> T get(Class<T> key);

    <T> void put(Class<T> key, T value);

    boolean contains(Class key);

    boolean remove(Class key);

}
