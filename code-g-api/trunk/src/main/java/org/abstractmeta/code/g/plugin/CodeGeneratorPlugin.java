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
package org.abstractmeta.code.g.plugin;

import org.abstractmeta.code.g.code.JavaTypeRegistry;
import org.abstractmeta.code.g.config.Descriptor;

import java.util.Collection;
import java.util.List;
import java.util.Map;

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
     * @param sourceTypeNames collection of the source type names
     * @param registry java type registry
     * @param descriptor configuration descriptor
     * @return  list of the generated java types
     */
    List<String> generate(Collection<String> sourceTypeNames, JavaTypeRegistry registry, Descriptor descriptor);

    /**
     * Configuration options
     * @return configuration options
     */
    Map<String, String> getOptions();
    
}
