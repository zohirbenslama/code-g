package org.abstractmeta.code.g.core.generator;

import org.abstractmeta.code.g.generator.Context;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Represents Context
 *
 * @author Adrian Witas
 */
public class ContextImpl implements Context {

    private final Map<Class, Object> context = new HashMap<Class, Object>();

    @Override
    public <T> T getOptional(Class<T> key) {
        Object result = context.get(key);
        if (result == null) return null;
        return key.cast(result);
    }

    @Override
    public <T> T getOptional(Class<T> key, T defaultValue) {
        T result = getOptional(key);
        if (result == null) return defaultValue;
        return result;
    }

    @Override
    public <T> T get(Class<T> key) {
        T result = getOptional(key);
        if (result == null) {
            throw new NoSuchElementException(key.getName());
        }
        return result;
    }

    @Override
    public <T> void put(Class<T> key, T value) {
        if (contains(key)) {
            throw new IllegalStateException("type " + key.getName() + " has been registered");
        }
        replace(key, value);
    }

    @Override
    public <T> void replace(Class<T> key, T value) {
        context.put(key, value);
        putInterfaceKey(key.getInterfaces(), value);
        if(key.getSuperclass() != null && ! Object.class.equals(key.getSuperclass())) {
            putInterfaceKey(key.getSuperclass().getInterfaces(), value);
        }
    }


    protected void putInterfaceKey(Class[] interfaces, Object value) {
        if (interfaces == null) return;
        for (Class iface : interfaces) {
            context.put(iface, value);
        }
    }


    @Override
    public boolean contains(Class key) {
        return context.containsKey(key);
    }

    @Override
    public boolean remove(Class key) {
        context.remove(key);
        removeInterfaceKey(key.getInterfaces());
        if(key.getSuperclass() != null && ! Object.class.equals(key.getSuperclass())) {
           removeInterfaceKey(key.getSuperclass().getInterfaces());
        }
        return true;
    }


    protected void removeInterfaceKey(Class[] interfaces) {
        if (interfaces == null) return;
        for (Class iface : interfaces) {
            context.remove(iface);
        }
    }
}
