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


import java.util.List;

/**
 * Represents a code unit descriptor.
 * It is a code aggregation source by target source directory
 *
 * @author Adrian Witas
 */
public interface UnitDescriptor {

    String getSourcePackage();

    String getTargetDirectory();

    String getSourceDirectory();

    List<String> getClassPathEntries();

    List<? extends Descriptor> getDescriptors();

    /**
     * If specified this descriptor would execute after all unit descriptor are processed
     * with all java types generated
     *
     * @return post descriptor
     */
    Descriptor getPostDescriptor();

    void setClassLoader(ClassLoader classLoader);

    ClassLoader getClassLoader();
}
