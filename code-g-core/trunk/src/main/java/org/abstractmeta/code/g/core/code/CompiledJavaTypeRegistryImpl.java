package org.abstractmeta.code.g.core.code;

import org.abstractmeta.code.g.code.CompiledJavaType;
import org.abstractmeta.code.g.code.CompiledJavaTypeRegistry;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents CompiledJavaTypeRegistry
 *
 * @author Adrian Witas
 */
public class CompiledJavaTypeRegistryImpl implements CompiledJavaTypeRegistry {

    private final Map<String, CompiledJavaType> registry;

    public CompiledJavaTypeRegistryImpl() {
        this(new HashMap<String, CompiledJavaType>());
    }

    public CompiledJavaTypeRegistryImpl(Map<String, CompiledJavaType> registry) {
        this.registry = registry;
    }

    @Override
    public void register(CompiledJavaType type) {
        registry.put(type.getType().getName(), type);
    }

    @Override
    public CompiledJavaType get(String typeName) {
        return registry.get(typeName);
    }

    @Override
    public boolean isRegistered(String typeName) {
        return registry.containsKey(typeName);
    }

    @Override
    public void unregister(String typeName) {
        registry.remove(typeName);
    }

    @Override
    public Collection<CompiledJavaType> get() {
        return registry.values();
    }
}
