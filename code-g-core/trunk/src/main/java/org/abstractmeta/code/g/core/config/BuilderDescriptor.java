package org.abstractmeta.code.g.core.config;

import org.abstractmeta.code.g.core.plugin.ClassGenerator;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Represents BuilderDescriptor
 * <p>
 * </p>
 *
 * @author Adrian Witas
 */
public class BuilderDescriptor extends DescriptorImpl {

    public BuilderDescriptor(String sourcePackage, String sourceClass, String targetPackage, String targetPrefix, String targetPostfix, String superType, String interfaces, Set<String> exclusions, Set<String> inclusions, String plugin, List<String> compilationSources, Map<String, String> options, Map<String, String> immutableImplementation) {
        super(sourcePackage, sourceClass, targetPackage, targetPrefix, targetPostfix, superType, interfaces, exclusions, inclusions, plugin, compilationSources, options, immutableImplementation);
    }

    @Override
    public String getPlugin() {
        return BuilderDescriptor.class.getName();
    }


}
