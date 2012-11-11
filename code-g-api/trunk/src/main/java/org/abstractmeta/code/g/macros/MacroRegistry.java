package org.abstractmeta.code.g.macros;

import java.util.Map;

/**
 * Represents MacroRegistry
 *
 * @author Adrian Witas
 */
public interface MacroRegistry {

    /**
     * Registers a macro
     * @param name
     * @param value
     */

    void register(String name, String value);

    /**
     * Returns true if a given macros is registered
     * @param name
     * @return
     */
    boolean isRegistered(String name);

    /**
     * Return a macro for a given name
     * @param name
     * @return
     */
    String get(String name);


    /**
     * Some suggested macros, used by maven
     * <ul>
     * <li> ${basedir} represents the directory containing project root </li>
     * <li> ${project.build.directory} results in the path to your "target" dir </li>
     * <li> ${project.build.outputDirectory} results in the path to your "target/classes" dir</li>
     * <li> ${project.source.java.directory} result in the path to src/main/java</li>
     * <li> ${project.build.generated.sources.directory} result in the path to target/generated-sources/code-g</li>
     * <li> ${env.M2_HOME} maven home</li>
     </ul>
     * @return
     */
    Map<String, String> getRegistry();



}
