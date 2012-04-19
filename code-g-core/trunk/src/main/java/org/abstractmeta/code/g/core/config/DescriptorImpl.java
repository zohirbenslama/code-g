package org.abstractmeta.code.g.core.config;


import org.abstractmeta.code.g.config.Descriptor;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Default descriptor implementation.
 *
 * @author Adrian Witas
 */

public class DescriptorImpl implements Descriptor {

    private String source;
    private String targetPackage;
    private String targetPostfix;
    private String superType;
    private String interfaces;
    private Set<String> exclusions;
    private Set<String> inclusions;
    private Map<String, String> immutableImplementation;

    private  List<String> plugins;
    private  List<String> compilationSources;
    private Map<String, String> options;

    public DescriptorImpl() {
    }

    public DescriptorImpl(String source, String targetPackage, String targetPostfix, String superType, String interfaces, Set<String> exclusions, Set<String> inclusions, List<String> plugins, List<String> compilationSources, Map<String, String> options, Map<String, String> immutableImplementation) {
        this.source = source;
        this.targetPackage = targetPackage;
        this.targetPostfix = targetPostfix;
        this.superType = superType;
        this.interfaces = interfaces;
        this.exclusions = exclusions;
        this.inclusions = inclusions;
        this.plugins = plugins;
        this.compilationSources = compilationSources;
        this.options = options;
        this.immutableImplementation = immutableImplementation;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setTargetPackage(String targetPackage) {
        this.targetPackage = targetPackage;
    }

    public void setTargetPostfix(String targetPostfix) {
        this.targetPostfix = targetPostfix;
    }

    public void setExclusions(Set<String> exclusions) {
        this.exclusions = exclusions;
    }

    public void setPlugins(List<String> plugins) {
        this.plugins = plugins;
    }

    public void setCompilationSources(List<String> compilationSources) {
        this.compilationSources = compilationSources;
    }

    public void setOptions(Map<String, String> options) {
        this.options = options;
    }

    public void setSuperType(String superType) {
        this.superType = superType;
    }

    public String getInterfaces() {
        return interfaces;
    }

    public void setInterfaces(String interfaces) {
        this.interfaces = interfaces;
    }

    public void setInclusions(Set<String> inclusions) {
        this.inclusions = inclusions;
    }

    public void setImmutableImplementation(Map<String, String> immutableImplementation) {
        this.immutableImplementation = immutableImplementation;
    }

    public String getSource() {
        return source;
    }

    public String getTargetPackage() {
        return targetPackage;
    }

    public String getTargetPostfix() {
        return targetPostfix;
    }


    @Override
    public String getSuperType() {
        return superType;
    }

    public Set<String> getExclusions() {
        return exclusions;
    }

    @Override
    public Set<String> getInclusions() {
        return inclusions;
    }

    public List<String> getPlugins() {
        return plugins;
    }

    public List<String> getCompilationSources() {
        return compilationSources;
    }

    public Map<String, String> getOptions() {
        return options;
    }

    @Override
    public Map<String, String> getImmutableImplementation() {
        return immutableImplementation;
    }

    @Override
    public String toString() {
        return "DescriptorImpl{" +
                "source='" + source + '\'' +
                ", targetPackage='" + targetPackage + '\'' +
                ", targetPostfix='" + targetPostfix + '\'' +
                ", superType='" + superType + '\'' +
                ", interfaces='" + interfaces + '\'' +
                ", exclusions=" + exclusions +
                ", inclusions=" + inclusions +
                ", immutableImplementation=" + immutableImplementation +
                ", plugins=" + plugins +
                ", compilationSources=" + compilationSources +
                ", options=" + options +
                '}';
    }
}
