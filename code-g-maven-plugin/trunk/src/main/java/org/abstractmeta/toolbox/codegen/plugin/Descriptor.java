package org.abstractmeta.toolbox.codegen.plugin;


import org.abstractmeta.code.g.core.config.DescriptorImpl;
import org.abstractmeta.code.g.core.plugin.BuilderGenerator;
import org.abstractmeta.code.g.core.plugin.ClassGenerator;
import org.abstractmeta.code.g.core.util.LoaderUtil;

import java.util.*;

/**
 * Represents  Descriptor.
 *
 * @author Adrian Witas
 */

public class Descriptor extends DescriptorImpl {


    public static final String DEFAULT_TARGET_PACKAGE = "impl";
    public static final String DEFAULT_BUILDER_TARGET_PACKAGE = "builder";

    public static final String DEFAULT_TARGET_POSTFIX = "Impl";
    public static final String DEFAULT_BUILDER_TARGET_POSTFIX = "Builder";

    public static final  List<String> DEFAULT_PLUGINS =  Arrays.asList(ClassGenerator.class.getSimpleName(), BuilderGenerator.class.getSimpleName());



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
        if(! (getPlugins().contains(ClassGenerator.class.getSimpleName())
                && getPlugins().contains(BuilderGenerator.class.getSimpleName()))) {
            return Collections.emptyMap();
        }
        Map<String, String> defaultOptions = new HashMap<String, String>();
        String sourcePackageName = LoaderUtil.extractPackageName(getSourceClass());
        defaultOptions.put(BuilderGenerator.class.getSimpleName() + ".source", getTargetPackage());
        defaultOptions.put(BuilderGenerator.class.getSimpleName() + ".targetPackage", sourcePackageName + "." + DEFAULT_BUILDER_TARGET_PACKAGE);
        defaultOptions.put(BuilderGenerator.class.getSimpleName() + ".targetPostfix", DEFAULT_BUILDER_TARGET_POSTFIX);
        return defaultOptions;
    }

    protected String getOptionName(Class clazz, String optionName) {
       return clazz.getSimpleName() + "." + optionName;
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
        String sourcePackage = LoaderUtil.extractPackageName(getSourceClass());
        if(isBuilderPlugin()) {
            return String.format("%s.%s", sourcePackage, "builder");
        }
        return String.format("%s.%s", sourcePackage, DEFAULT_TARGET_PACKAGE);
    }


    protected boolean isBuilderPlugin() {
          return getPlugins().size() == 1 && BuilderGenerator.class.getSimpleName().contains(getPlugins().get(0));
    }


}
