package org.abstractmeta.code.g.core.plugin;

import org.abstractmeta.code.g.config.Descriptor;
import org.abstractmeta.code.g.plugin.CodeGeneratorPlugin;
import org.abstractmeta.code.g.plugin.CodeGeneratorPluginLoader;

import java.util.*;

/**
 * Plugin loader, loads plugin with descriptor configuration option.
 *
 * @author Adrian Witas
 */
public class CodeGeneratorPluginLoaderImpl implements CodeGeneratorPluginLoader {

    private final List<String> pluginPackages;


    public CodeGeneratorPluginLoaderImpl() {
        this(new ArrayList<String>());
    }

    public CodeGeneratorPluginLoaderImpl(List<String> pluginPackages) {
        this.pluginPackages = pluginPackages;
    }


    @Override
    public void addPluginPackageName(String packageName) {
        pluginPackages.add(packageName);
    }

    public Map<String, CodeGeneratorPlugin> loadPlugins(Collection<Descriptor> descriptors, ClassLoader classLoader) {
        if (classLoader == null) {
            classLoader = this.getClass().getClassLoader();
        }
        Map<String, CodeGeneratorPlugin> result = new HashMap<String, CodeGeneratorPlugin>();
        for (Descriptor descriptor : descriptors) {
            loadPlugins(result, descriptor, classLoader);
        }
        return result;
    }

    protected void loadPlugins(Map<String, CodeGeneratorPlugin> plugins, Descriptor descriptor, ClassLoader classLoader) {
        for (String pluginName : descriptor.getPlugins()) {
            if (plugins.containsKey(pluginName)) {
                continue;
            }
            CodeGeneratorPlugin plugin = loadPlugin(classLoader, pluginName);
            plugins.put(pluginName, plugin);
        }
    }

    protected CodeGeneratorPlugin loadPlugin(ClassLoader classLoader, String pluginName) {
        Exception caughtException = null;
        Class type = null;
        try {
            if (pluginName.indexOf('.') != -1) {
                type = classLoader.loadClass(pluginName);
            } else {
                for (String pluginPackageCandidate : pluginPackages) {
                    String pluginCandidate = pluginPackageCandidate + "." + pluginName;
                    try {
                        type = classLoader.loadClass(pluginCandidate);
                        break;
                    } catch (Exception e) {
                        caughtException = e;
                    }
                }
            }
            return CodeGeneratorPlugin.class.cast(type.newInstance());

        } catch (ClassCastException e) {
            caughtException = new IllegalStateException("Failed to case " + type, e);
        } catch (Exception e) {
            caughtException = e;
        }
        throw new IllegalStateException("Failed to load plugin " + pluginName, caughtException);
    }

}
