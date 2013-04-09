package org.abstractmeta.code.g.core.config;

import org.abstractmeta.code.g.config.SourceMatcher;

import java.util.Collection;

/**
 * SourceMatcher implementation.
 *
 * @author Adrian Witas
 */
public class SourceMatcherImpl implements SourceMatcher {
    
    
    private String sourceDirectory;
    private Collection<String> classNames;
    private Collection<String> packageNames;
    private boolean includeSubpackages;
    private Collection<String> inclusionPatterns;
    private Collection<String> exclusionPatterns;
    private Collection<String> dependencyPackages;

    public String getSourceDirectory() {
        return sourceDirectory;
    }

    public void setSourceDirectory(String sourceDirectory) {
        this.sourceDirectory = sourceDirectory;
    }

    public Collection<String> getClassNames() {
        return classNames;
    }

    public void setClassNames(Collection<String> classNames) {
        this.classNames = classNames;
    }

    public Collection<String> getPackageNames() {
        return packageNames;
    }

    public void setPackageNames(Collection<String> packageNames) {
        this.packageNames = packageNames;
    }

    public boolean isIncludeSubpackages() {
        return includeSubpackages;
    }

    public void setIncludeSubpackages(boolean includeSubpackages) {
        this.includeSubpackages = includeSubpackages;
    }

    public Collection<String> getInclusionPatterns() {
        return inclusionPatterns;
    }

    public void setInclusionPatterns(Collection<String> inclusionPatterns) {
        this.inclusionPatterns = inclusionPatterns;
    }

    public Collection<String> getExclusionPatterns() {
        return exclusionPatterns;
    }

    public void setExclusionPatterns(Collection<String> exclusionPatterns) {
        this.exclusionPatterns = exclusionPatterns;
    }

    public Collection<String> getDependencyPackages() {
        return dependencyPackages;
    }

    public void setDependencyPackages(Collection<String> dependencyPackages) {
        this.dependencyPackages = dependencyPackages;
    }
}
