package org.abstractmeta.code.g.renderer;

import org.abstractmeta.code.g.code.JavaConstructor;
import org.abstractmeta.code.g.code.JavaTypeImporter;

/**
 * It is responsible for rendering java constructor.
 *
 * @author Adrian Witas
 */
public interface JavaConstructorRenderer {
    
    String render(JavaConstructor constructor, JavaTypeImporter importer, int indentSize);
    
}
