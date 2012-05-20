package org.abstractmeta.code.g.core.config.builder;

import com.google.common.collect.ImmutableMap;
import org.abstractmeta.code.g.config.Descriptor;
import org.abstractmeta.code.g.core.config.DescriptorImpl;
import org.abstractmeta.code.g.core.plugin.BuilderGeneratorPlugin;
import org.abstractmeta.code.g.core.plugin.ClassGeneratorPlugin;
import org.abstractmeta.code.g.core.util.ReflectUtil;
import org.abstractmeta.code.g.core.util.StringUtil;
import org.abstractmeta.code.g.plugin.CodeGeneratorPlugin;

import java.util.*;

/**
 * Represents DescriptorBuilder
 *
 * @author Adrian Witas
 */
public class DescriptorBuilder {

    public static final String TARGET_PACKAGE = "targetPackage";
    public static final String TARGET_PREFIX = "targetPrefix";
    public static final String TARGET_POSTFIX = "targetPostfix";


    public final static Map<String, Map<String, String>> PLUGIN_DEFAULTS = ImmutableMap.<String, Map<String, String>>of(
            ClassGeneratorPlugin.class.getName(), ImmutableMap.of(
            TARGET_PACKAGE, "impl",
            TARGET_POSTFIX, "Impl"),
            BuilderGeneratorPlugin.class.getName(), ImmutableMap.of(
            TARGET_PACKAGE, "builder",
            TARGET_POSTFIX, "Builder")
    );

    private String sourcePackage;
    private String sourceClass;
    private String targetPackage;
    private String targetPrefix;
    private String targetPostfix;
    private String superType;
    private String interfaces;
    private Set<String> exclusions = new HashSet<String>();
    private Set<String> inclusion = new HashSet<String>();

    private String plugin;

    private List<String> compilationSources = new ArrayList<String>();
    private Map<String, String> options = new HashMap<String, String>();
    private Map<String, String> immutableImplementation = new HashMap<String, String>();


    public String getSourcePackage() {
        return sourcePackage;
    }

    public DescriptorBuilder setSourcePackage(String sourcePackage) {
        this.sourcePackage = sourcePackage;
        return this;
    }

    public String getSourceClass() {
        return sourceClass;
    }

    public DescriptorBuilder setSourceClass(String sourceClass) {
        this.sourceClass = sourceClass;
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

    public String getPlugin() {
        return plugin;
    }

    public DescriptorBuilder setPlugin(String plugin) {
        this.plugin = plugin;
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

    public String getTargetPrefix() {
        return targetPrefix;
    }

    public DescriptorBuilder setTargetPrefix(String targetPrefix) {
        this.targetPrefix = targetPrefix;
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
        if (descriptor.getSourcePackage() != null) {
            this.sourcePackage = descriptor.getSourcePackage();
        }
        if (descriptor.getSourceClass() != null) {
            this.sourceClass = descriptor.getSourceClass();
        }

        if (descriptor.getTargetPackage() != null) {
            this.targetPackage = descriptor.getTargetPackage();
        }
        if (descriptor.getTargetPrefix() != null) {
            this.targetPrefix = descriptor.getTargetPrefix();
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
        if (descriptor.getPlugin() != null) {
            this.plugin = descriptor.getPlugin();
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


    public Map<String, String> getPluginDefault(String pluginName) {
        if(pluginName == null) {
            return Collections.emptyMap();
        }
        CodeGeneratorPlugin plugin = ReflectUtil.loadInstance(CodeGeneratorPlugin.class, pluginName);

        Map<String, String> result = plugin.getOptions();
        if(result != null && ! result.isEmpty())   {
            return result;
        }

        result = PLUGIN_DEFAULTS.get(pluginName);
        if (result == null) return Collections.emptyMap();
        return result;
    }

    public Descriptor build() {
        Map<String, String> defaults = getPluginDefault(plugin);
        if (sourceClass != null && sourcePackage == null) {
            sourcePackage = StringUtil.substringBeforeLastIndexOf(sourceClass, ".");
        }
        if (targetPackage == null) {
            if (defaults.containsKey(TARGET_PACKAGE)) {
                targetPackage = sourcePackage + "." + defaults.get(TARGET_PACKAGE);
            } else {
                targetPackage = sourcePackage;
            }
        }
        if (targetPostfix == null && defaults.containsKey(TARGET_POSTFIX)) {
            targetPostfix = defaults.get(TARGET_POSTFIX);
        }
        if (targetPrefix == null && defaults.containsKey(TARGET_PREFIX)) {
            targetPrefix = defaults.get(TARGET_PREFIX);
        }
        return new DescriptorImpl(sourcePackage, sourceClass, targetPackage, targetPrefix, targetPostfix, superType, interfaces, exclusions, inclusion, plugin, compilationSources, options, immutableImplementation);
    }

}
