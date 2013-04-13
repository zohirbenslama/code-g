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

import java.util.Properties;

/**
* Represents code plugin configuration descriptor
*
* @author Adrian Witas
*/
public interface Descriptor {

    /**
     * Source filter
     * @return
     */
    SourceMatcher getSourceMatcher();

    /**
     * Returns naming convention
     * This is to replace class generator convention
     * @return
     */
    NamingConvention getNamingConvention();

    /**
     * Code generator class names.
     * @return code generator class name.
     */
    String getGeneratorClass();

    /**
     * Return configuration properties
     * This properties will be used to instantiate a generator specific configuration class.
     * <b>Note</b> that property names need to match field name on configuration class.
     * @return
     */
    Properties getProperties();



}
