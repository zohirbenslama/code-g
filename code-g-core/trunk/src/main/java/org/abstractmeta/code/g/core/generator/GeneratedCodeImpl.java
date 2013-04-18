package org.abstractmeta.code.g.core.generator;

import org.abstractmeta.code.g.code.CompiledJavaTypeRegistry;
import org.abstractmeta.code.g.config.UnitDescriptor;
import org.abstractmeta.code.g.generator.GeneratedCode;

/**
 * Represents GeneratedCodeImpl
 *
 * @author Adrian Witas
 */
public class GeneratedCodeImpl implements GeneratedCode {

    private ClassLoader classLoader;
    private CompiledJavaTypeRegistry registry;
    private UnitDescriptor unitDescriptor;


    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public CompiledJavaTypeRegistry getRegistry() {
        return registry;
    }

    public void setRegistry(CompiledJavaTypeRegistry registry) {
        this.registry = registry;
    }

    public UnitDescriptor getUnitDescriptor() {
        return unitDescriptor;
    }

    public void setUnitDescriptor(UnitDescriptor unitDescriptor) {
        this.unitDescriptor = unitDescriptor;
    }
}
