package org.abstractmeta.code.g.core.diconfig;

import org.abstractmeta.code.g.code.JavaTypeRegistry;
import org.abstractmeta.code.g.core.code.JavaTypeRegistryImpl;

import javax.inject.Provider;

/**
 * Default JavaTypeRegistry provider.
 *
 * @author Adrian Witas
 */
public class JavaTypeRegistryProvider implements Provider<JavaTypeRegistry> {


    @Override
    public JavaTypeRegistry get() {
        return new JavaTypeRegistryImpl();
    }
}

        
