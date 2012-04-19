package org.abstractmeta.toolbox.codegen.plugin;


import org.abstractmeta.code.g.common.config.DescriptorImpl;
import org.abstractmeta.code.g.common.plugin.BuilderGeneratorPlugin;
import org.abstractmeta.code.g.common.plugin.ClassGeneratorPlugin;
import org.abstractmeta.code.g.common.util.LoaderUtil;

import java.util.*;

/**
 * Represents  Descriptor.
 *
 * @author Adrian Witas
 */

public class Descriptor extends DescriptorImpl {

    public static final String DEFAULT_TARGET_PACKAGE = "impl";

    public static final String DEFAULT_TARGET_POSTFIX = "Impl";

    public static final  List<String> DEFAULT_PLUGINS =  Arrays.asList(ClassGeneratorPlugin.class.getName(), BuilderGeneratorPlugin.class.getName());



    @Override
    public Map<String, String> getOptions() {
        Map<String, String> options = super.getOptions();
        if (options != null && options.size() > 0) {
            Map<String, String> result = new HashMap<String, String>(getDefaultOptions());
            result.putAll(options);
            return result;
        }
        return getDefaultOptions();
    }

    public Map<String, String> getDefaultOptions() {
        if(! (getPlugins().contains(ClassGeneratorPlugin.class.getSimpleName())
                && getPlugins().contains(BuilderGeneratorPlugin.class.getSimpleName()))) {
            return Collections.emptyMap();
        }
        Map<String, String> defaultOptions = new HashMap<String, String>();
        String sourcePackageName = LoaderUtil.extractPackageName(getSource());
        defaultOptions.put(BuilderGeneratorPlugin.class.getSimpleName() + ".source", getTargetPackage());
        defaultOptions.put(BuilderGeneratorPlugin.class.getSimpleName() + ".targetPackage", sourcePackageName + ".builder");
        defaultOptions.put(BuilderGeneratorPlugin.class.getSimpleName() + ".targetPostfix", "Builder");
        return defaultOptions;
    }

        @Override
    public String getTargetPostfix() {
        String result = super.getTargetPostfix();
        if (result != null) {
            return result;
        }
        return DEFAULT_TARGET_POSTFIX;

    }


    @Override
    public List<String> getPlugins() {
        List<String> result =  super.getPlugins();
        if(result != null && result.size() > 0) {
            return result;
        }
        return getDefaultPlugins();
    }

    
    protected List<String> getDefaultPlugins() {
        return DEFAULT_PLUGINS;
    }
    
    @Override
    public String getTargetPackage() {
        String result = super.getTargetPackage();
        if (result != null) {
            return result;
        }
        String sourcePackage = LoaderUtil.extractPackageName(getSource());
        return String.format("%s.%s", sourcePackage, DEFAULT_TARGET_PACKAGE);
    }



}
