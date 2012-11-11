/**
 * Copyright 2011 Adrian Witas
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.abstractmeta.code.g.config;

import org.abstractmeta.code.g.config.loader.JavaSourceLoader;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
* Represents code plugin configuration descriptor
*
* @author Adrian Witas
*/
public interface Descriptor {


    /**
     * A source package used to generate a code.
     * <ul>
     *     <li>Package name - in this case only classes matched to this package are used as source.t</li>
     *     <li>Package name with wildcard '*', all classes matched this or sub packages are used as source </li>
     * </ul>
     * @return source package
     */
    String getSourcePackage();


    /**
     * A source class used to generate a code.
     *
     * @return source class
     */
    String getSourceClass();


    /**
     * Named of target package used to generated code.
     * @return target package
     */
    String getTargetPackage();


    /**
     * Target class prefix.
     * @return generated class prefix
     */
    String getTargetPrefix();

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
     * For the latter PluginLoader#addPluginPackageName should be used
     * to specify all package to be used to resolve simple class name.
     * </p>
     *
     * @return plugin class name.
     */
    String getPlugin();


    /**
     * Current project packages used to compile auto plugin sources.
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
     * java.util.List => com.google.core.collect.ImmutableList.copyOf
     *
     * @return
     */
    Map<String, String> getImmutableImplementation();


    /**
     *
     * @return
     */
    String getJavaTypeLoaderClassName();
}
