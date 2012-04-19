package org.abstractmeta.code.g.renderer;

import org.abstractmeta.code.g.code.JavaField;
import org.abstractmeta.code.g.code.JavaTypeImporter;

/**
 * It is responsible for rendering java field.
 *
 * @author Adrian Witas
 */
public interface JavaFieldRenderer {

    String render(JavaField field, JavaTypeImporter importer, int indentSize);

}
