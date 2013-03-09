package org.abstractmeta.code.g.config;


import java.util.Collection;

/**
 * Represents SourceFilter
 *
 * @author Adrian Witas
 */
public interface SourceFilter {

    String getSourceDirectory();

    String getClassNames();

    String getPackageNames();

    boolean includeSubpackages();

    Collection<String> getInclusionPatterns();

    Collection<String> getExclusionPatterns();

    /**
     * List of dependable packages which source classes depend on,
     * they need to be compiled in order to compile source code for source classes
     * @return
     */
    Collection<String> getDependencyPackages();

}

