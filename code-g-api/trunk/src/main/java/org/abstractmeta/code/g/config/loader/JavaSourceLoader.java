package org.abstractmeta.code.g.config.loader;

import org.abstractmeta.code.g.config.Descriptor;

import java.io.File;
import java.util.Map;

/**
 * Java source loader.
 * 
 * @author Adrian Witas
 */
public interface JavaSourceLoader {

    /**
     * Loads class sources from source directory for a given descriptors.
     * @param sourceDirectory source directory
     * @param descriptors descriptors
     * @return map where key is a class name, value source code.
     */
    Map<String, String> load(File sourceDirectory, Iterable<Descriptor> descriptors);
    
}
