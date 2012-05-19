package org.abstractmeta.toolbox.codegen.plugin;

import org.abstractmeta.code.g.core.config.DescriptorImpl;
import org.abstractmeta.code.g.core.plugin.BuilderGeneratorPlugin;

/**
 * Represents BuilderGenerator
 * <p>
 * </p>
 *
 * @author Adrian Witas
 */
public class BuilderGenerator extends DescriptorImpl{

    @Override
    public String getPlugin() {
        return BuilderGeneratorPlugin.class.getName();
    }
}
