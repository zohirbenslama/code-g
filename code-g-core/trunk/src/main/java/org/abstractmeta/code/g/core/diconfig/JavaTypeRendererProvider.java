package org.abstractmeta.code.g.core.diconfig;

import org.abstractmeta.code.g.core.renderer.TypeRenderer;
import org.abstractmeta.code.g.renderer.JavaTypeRenderer;

import javax.inject.Provider;

/**
 * Default JavaTypeRenderer provider.
 *
 * @author Adrian Witas
 */
public class JavaTypeRendererProvider implements Provider<JavaTypeRenderer> {
    @Override
    public JavaTypeRenderer get() {
        return new TypeRenderer();
    }
}

        
