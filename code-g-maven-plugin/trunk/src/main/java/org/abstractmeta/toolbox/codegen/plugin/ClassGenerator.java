package org.abstractmeta.toolbox.codegen.plugin;

import org.abstractmeta.code.g.core.config.DescriptorImpl;
import org.abstractmeta.code.g.core.plugin.ClassGeneratorPlugin;

/**
 * Represents ClassGenerator
 * <p>
 * </p>
 *
 * @author Adrian Witas
 */
public class ClassGenerator extends DescriptorImpl{
    @Override
    public String getPlugin() {
        return ClassGeneratorPlugin.class.getName();
    }
}
