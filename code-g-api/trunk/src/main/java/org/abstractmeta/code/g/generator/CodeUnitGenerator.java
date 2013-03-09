package org.abstractmeta.code.g.generator;

import org.abstractmeta.code.g.code.SourcedJavaType;
import org.abstractmeta.code.g.config.UnitDescriptor;

import java.util.Collection;

/**
 * Represents CodeUnitGenerator
 *
 * @author Adrian Witas
 */
public interface CodeUnitGenerator {

    /**
     * Generates code for a unit descriptor
     * @param descriptor
     * @return
     */
   GeneratedCode generate(UnitDescriptor descriptor);

}
