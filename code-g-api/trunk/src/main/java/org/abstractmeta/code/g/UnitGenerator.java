package org.abstractmeta.code.g;

import org.abstractmeta.code.g.config.Descriptor;
import org.abstractmeta.code.g.config.UnitDescriptor;
import org.abstractmeta.code.g.handler.CodeHandler;

import java.io.File;
import java.util.Collection;
import java.util.List;

/**
 * Represents UnitGenerator
 * <p>
 * </p>
 *
 * @author Adrian Witas
 */
public interface UnitGenerator {

    /**
     * Builds code for the specified descriptors.
     *
     * @param unitDescriptor descriptor
     * @return list of the source code files
     */
   Collection<File> generate(UnitDescriptor unitDescriptor);


}
