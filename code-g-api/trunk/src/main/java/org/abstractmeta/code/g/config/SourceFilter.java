package org.abstractmeta.code.g.config;


import java.util.Collection;

/**
 * Represents SourceFilter
 *
 * @author Adrian Witas
 */
public interface SourceFilter {

    String getSourceDirectory();

    Collection<String> getClassNames();

    Collection<String> getPackageNames();

    boolean isIncludeSubpackages();

    /**
     * Optional package based class inclusion pattern
     * @return collection of pattern
     */
    Collection<String> getInclusionPatterns();

    /**
     * Optional package based class exclusion pattern
     * @return collection of pattern
     */
    Collection<String> getExclusionPatterns();


    /**
     * List of dependable packages which source classes depend on,
     * they need to be compiled in order to compile source code for source classes,
     * but this package classes will not be included in the filtered result set
     * @return
     */
    Collection<String> getDependencyPackages();

}

