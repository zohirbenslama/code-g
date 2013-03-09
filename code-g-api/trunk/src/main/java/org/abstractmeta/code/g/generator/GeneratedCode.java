package org.abstractmeta.code.g.generator;

import org.abstractmeta.code.g.code.SourcedJavaTypeRegistry;
import org.abstractmeta.code.g.config.UnitDescriptor;

/**
 * Represents a generated code.
 *
 * @author Adrian Witas
 */
public interface GeneratedCode {

    ClassLoader getClassLoader();

    SourcedJavaTypeRegistry getRegistry();

    UnitDescriptor getUnitDescriptor();

}
