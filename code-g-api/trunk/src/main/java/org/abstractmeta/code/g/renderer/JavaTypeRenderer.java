package org.abstractmeta.code.g.renderer;

import org.abstractmeta.code.g.code.JavaType;
import org.abstractmeta.code.g.code.JavaTypeImporter;

/**
 * It is responsible for rendering java type.
 *
 * @author Adrian Witas
 */
public interface JavaTypeRenderer {

    String render(JavaType type, JavaTypeImporter importer, int indentSize);

}
