package org.abstractmeta.code.g.config;

import java.util.Collection;
import java.util.List;

/**
 * Represents a code unit descriptor.
 * It is a code aggregation source by target source directory
 *
 * @author Adrian Witas
 */
public interface UnitDescriptor {
    
    String getSourcePackage();

    String getTargetDirectory();
    
    String getSourceDirectory();
    
    List<String> getClassPathEntries();
    
    List<? extends Descriptor> getDescriptors();
}
