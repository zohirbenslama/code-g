package org.abstractmeta.code.g.core.property;

import org.abstractmeta.code.g.property.PropertyRegistry;

import java.util.HashMap;
import java.util.Map;

/**
 * PropertyRegistry implementation
 *
 * @author Adrian Witas
 */
public class PropertyRegistryImpl implements PropertyRegistry {

    private final Map<String, String> registry;


    public PropertyRegistryImpl() {
        this(new HashMap<String, String>());
    }

    public PropertyRegistryImpl(Map<String, String> registry) {
        this.registry = registry;
    }

    @Override
    public void register(String name, String value) {
        this.registry.put(name, value);
    }

    @Override
    public boolean isRegistered(String name) {
        return registry.containsKey(name);
    }

    @Override
    public String get(String name) {
        return registry.get(name);
    }

    @Override
    public Map<String, String> getRegistry() {
        return registry;
    }
}
