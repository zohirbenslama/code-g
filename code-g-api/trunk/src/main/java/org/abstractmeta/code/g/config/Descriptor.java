package org.abstractmeta.code.g.config;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
* Represents code generator configuration descriptor
*
* @author Adrian Witas
*/
public interface Descriptor {

    /**
     * Generation source. It could be one of the followings:
     * <ul>
     *     <li>Package name - in this case only classes matched to this package are used as source.t</li>
     *     <li>Package name with wildcard '*', all classes matched this or sub packages are used as source </li>
     *     <li>Class name</li>
     * </ul>
     * @return source
     */
    String getSource();

    /**
     * Named of target package used to generated code.
     * @return target package
     */
    String getTargetPackage();

    /**
     * Target class postfix.
     * @return generated  class postfix
     */
    String getTargetPostfix();


    /**
     * Optional supper class that generated class needs to extend
     * @return  super class
     */
    String getSuperType();


    /**

     * Optional interfaces that generated class needs to implement
     * @return  super class
     */
    String getInterfaces();

    /**
     * Simple class exclusion names.
     * @return exclusion names.
     */
    Set<String> getExclusions();

    /**
     * Simple class inclusion names.
     * @return inclusion names.
     */
    Set<String> getInclusions();



    /**
     * Plugins class names.
     * <p> Both full and simple class names are supported.
     * For the latter CodeGeneratorPluginLoader#addPluginPackageName should be used
     * to specify all package to be used to resolve simple class name.
     * </p>
     *
     * @return pluging class names.
     */
    List<String> getPlugins();

    /**
     * Current project packages used to compile auto generator sources.
     * To avoid mixing up auto generated code with the actual code,
     * The generation source may depend on some packages with in the same problem.
     * This is not issue when source code is a separate maven project.
     *
     * @return compilation path.
     */
    List<String> getCompilationSources();
    
    
    /**
     * Plugin configuration options.
     * @return configuration options.
     */
    Map<String, String> getOptions();

    /**
     * Immutable type implementation mappings.
     * For instance if you want use immutable List.class, this
     * mappings should have the following entry:
     * <br />
     * java.util.List => com.google.common.collect.ImmutableList.copyOf
     *
     * @return
     */
    Map<String, String> getImmutableImplementation();
    
    
}
