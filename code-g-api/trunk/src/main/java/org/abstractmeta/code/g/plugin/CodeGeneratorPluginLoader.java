package org.abstractmeta.code.g.plugin;

import org.abstractmeta.code.g.config.Descriptor;

import java.util.Collection;
import java.util.Map;

/**
 * Represents CodeGenerator plugin loader.
 *
 * @author Adrian Witas
 */
public interface CodeGeneratorPluginLoader {

    /**
     * Add plugin package name. This option is used
     * for plugin named specified in descriptor as simple class name,
     * in this case all package names are scanned till the plugin is loaded.
     *
     * @param packageName add plugin package name
     *
     */
    void addPluginPackageName(String packageName);

    /**
     * Loads plugin defined in the supplied descriptors,
     * @param descriptors configuration descriptors
     * @param classLoader class loader
     * @return map of plugin name, instance of relevant  plugin
     */
    Map<String, CodeGeneratorPlugin> loadPlugins(Collection<Descriptor> descriptors, ClassLoader classLoader);

}
