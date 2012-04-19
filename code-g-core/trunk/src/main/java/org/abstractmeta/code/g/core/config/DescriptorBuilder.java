package org.abstractmeta.code.g.core.config;

import org.abstractmeta.code.g.config.Descriptor;

import java.util.*;

/**
 * Represents DescriptorBuilder
 *
 * @author Adrian Witas
 */
public class DescriptorBuilder {

    private String source;
    private String targetPackage;
    private String targetPostfix;
    private String superType;
    private String interfaces;
    private Set<String> exclusions = new HashSet<String>();
    private Set<String> inclusion = new HashSet<String>();

    private List<String> plugins = new ArrayList<String>();
    private List<String> compilationSources = new ArrayList<String>();
    private Map<String, String> options = new HashMap<String, String>();
    private Map<String, String> immutableImplementation = new HashMap<String, String>();


    public String getSource() {
        return source;
    }

    public DescriptorBuilder setSource(String source) {
        this.source = source;
        return this;
    }

    public Set<String> getExclusions() {
        return exclusions;
    }

    public DescriptorBuilder setExclusions(Set<String> exclusions) {
        this.exclusions = exclusions;
        return this;
    }

    public DescriptorBuilder addExclusions(String... exclusions) {
        Collections.addAll(this.exclusions, exclusions);
        return this;
    }


    public Set<String> getInclusion() {
        return inclusion;
    }

    public DescriptorBuilder setInclusion(Set<String> inclusion) {
        this.inclusion = inclusion;
        return this;
    }

    public DescriptorBuilder addInclusion(String... inclusion) {
        Collections.addAll(this.inclusion, inclusion);
        return this;
    }

    

    public String getSuperType() {
        return superType;
    }

    public DescriptorBuilder setSuperType(String superType) {
        this.superType = superType;
        return this;

    }

    public String getInterfaces() {
        return interfaces;
    }

    public DescriptorBuilder setInterfaces(String interfaces) {
        this.interfaces = interfaces;
        return this;
    }
    
    public DescriptorBuilder addImmutableImplementation(String source, String target) {
        immutableImplementation.put(source, target);
        return this;
    }

    public List<String> getPlugins() {
        return plugins;
    }

    public DescriptorBuilder setPlugins(List<String> plugins) {
        this.plugins = plugins;
        return this;
    }


    public DescriptorBuilder addPlugins(String... plugins) {
        Collections.addAll(this.plugins, plugins);
        return this;
    }

    public List<String> getCompilationSources() {
        return compilationSources;
    }

    public DescriptorBuilder setCompilationSources(List<String> compilationSources) {
        this.compilationSources = compilationSources;
        return this;
    }

    public DescriptorBuilder addCompilationPaths(String... compilationPaths) {
        Collections.addAll(this.compilationSources, compilationPaths);
        return this;
    }

    public Map<String, String> getOptions() {
        return options;
    }

    public DescriptorBuilder setOptions(Map<String, String> options) {
        this.options = options;
        return this;
    }

    public DescriptorBuilder addOptions(String key, String value) {
        this.options.put(key, value);
        return this;
    }

    public String getTargetPackage() {
        return targetPackage;
    }

    public DescriptorBuilder setTargetPackage(String targetPackage) {
        this.targetPackage = targetPackage;
        return this;
    }

    public String getTargetPostfix() {
        return targetPostfix;
    }

    public DescriptorBuilder setTargetPostfix(String targetPostfix) {
        this.targetPostfix = targetPostfix;
        return this;
    }

    public DescriptorBuilder merge(Descriptor descriptor) {
        if (descriptor.getSource() != null) {
            this.source = descriptor.getSource();
        }
        if (descriptor.getTargetPackage() != null) {
            this.targetPackage = descriptor.getTargetPackage();
        }
        if (descriptor.getTargetPostfix() != null) {
            this.targetPostfix = descriptor.getTargetPostfix();
        }
        if (descriptor.getSuperType() != null) {
            this.superType = descriptor.getSuperType();
        }
        if (descriptor.getInterfaces() != null) {
            this.interfaces = descriptor.getInterfaces();
        }
        if (descriptor.getExclusions() != null) {
            this.exclusions.addAll(descriptor.getExclusions());
        }
        if (descriptor.getInclusions() != null) {
            this.inclusion.addAll(descriptor.getInclusions());
        }
        if (descriptor.getPlugins() != null) {
            this.plugins.addAll(descriptor.getPlugins());
        }
        if (descriptor.getCompilationSources() != null) {
            this.compilationSources.addAll(descriptor.getCompilationSources());
        }
        if (descriptor.getOptions() != null) {
            this.options.putAll(descriptor.getOptions());
        }
        if (descriptor.getImmutableImplementation() != null) {
            this.immutableImplementation.putAll(descriptor.getImmutableImplementation());
        }
        return this;
    }


    public Descriptor build() {
        return new DescriptorImpl(source, targetPackage, targetPostfix, superType, interfaces, exclusions, inclusion, plugins, compilationSources, options, immutableImplementation);
    }

}
