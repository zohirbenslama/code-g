package org.abstractmeta.code.g.core.macro;

import org.abstractmeta.code.g.macros.MacroRegistry;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents MacroRegistryImpl
 *
 * @author Adrian Witas
 */
public class MacroRegistryImpl implements MacroRegistry {

    private final Map<String, String> registry;


    public MacroRegistryImpl() {
        this(new HashMap<String, String>());
    }

    public MacroRegistryImpl(Map<String, String> registry) {
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
