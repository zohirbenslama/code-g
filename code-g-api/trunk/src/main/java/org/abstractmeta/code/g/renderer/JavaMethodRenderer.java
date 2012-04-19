package org.abstractmeta.code.g.renderer;

import org.abstractmeta.code.g.code.JavaMethod;
import org.abstractmeta.code.g.code.JavaTypeImporter;

/**
 * It is responsible for rendering java method.
 *
 * @author Adrian Witas
 */
public interface JavaMethodRenderer {

    String render(JavaMethod method, JavaTypeImporter importer, int indentSize);

}
