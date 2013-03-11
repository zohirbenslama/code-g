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
package org.abstractmeta.code.g.property;

import java.util.Map;

/**
 * Represents PropertyRegistry
 * All used in descriptor properties will be expanded with registered values.
 *
 * @author Adrian Witas
 */
public interface PropertyRegistry {

    /**
     * Registers a macro
     * @param name
     * @param value
     */

    void register(String name, String value);

    /**
     * Returns true if a given property is registered
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
     * Some suggested property, used by maven
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
