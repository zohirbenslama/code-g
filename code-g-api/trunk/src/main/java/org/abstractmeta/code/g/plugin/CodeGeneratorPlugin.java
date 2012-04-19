package org.abstractmeta.code.g.plugin;

import org.abstractmeta.code.g.code.JavaTypeRegistry;
import org.abstractmeta.code.g.config.Descriptor;

import java.util.List;

/**
 * Code Generator Plugin.
 *
 *
 * @author Adrian Witas
 */
public interface CodeGeneratorPlugin {

    /**
     * Generates code for the source java types. Types are looked up with supplied sourceTypeNames in a given registry.
     * Descriptor specifies configuration option like target package name, target class postfix etc ..
     * @param sourceTypeNames list of the source type names
     * @param registry java type registry
     * @param descriptor configuration descriptor
     * @return  list of the generated java types
     */
    List<String> generate(List<String> sourceTypeNames, JavaTypeRegistry registry, Descriptor descriptor);

    /**
     * Returns configuration option for a given name
     * @param descriptor descriptor
     * @param name option name
     * @return option
     * @throws NullPointerException
     */
    String getRequiredOption(Descriptor descriptor, String name);


    /**
     * Returns configuration option for a given name
     * @param descriptor descriptor
     * @param name option name
     * @return option
     */
    String getOption(Descriptor descriptor, String name);

}
