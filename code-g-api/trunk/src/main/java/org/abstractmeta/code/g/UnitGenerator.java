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
package org.abstractmeta.code.g;

import org.abstractmeta.code.g.code.JavaType;
import org.abstractmeta.code.g.code.SourcedJavaType;
import org.abstractmeta.code.g.config.UnitDescriptor;
import org.abstractmeta.code.g.macros.MacroRegistry;

import java.io.File;
import java.util.Collection;
import java.util.Map;

/**
 * Unit generator uses unit descriptor to drive generation process.
 * <p>
 * MacroRegistry is used to substitute all descriptor with specified macro's value.
 * </p>
 * <h2>Code generation</h2>
 * <ol>For each unit descriptor
 *
 * <li>Unit generator compiles source with compilation unit for specified class path entries{@link UnitDescriptor#getClassPathEntries()} and source path  {@link UnitDescriptor#getSourceDirectory()}</li>
 * <li>Unit generator delegates actual code generation to code generator with compilation unit class loader. See {@link CodeGenerator} for more details</li>
 * <li>All generated classes are added to this method scope registry</li>
 *
 * </ol>

 * <h2>Post code generation</h2>
 * <ol>For each unit descriptor if {@link UnitDescriptor#getPostDescriptor()} is defined
 * <li>Unit generator delegates actual code generation with a registry containing classes generated in code generation phase.</li>
 * <li>Generated code is stored in location defined by {@link UnitDescriptor#getTargetDirectory()}</li>
 * </ol>
 *
 * @author Adrian Witas
 */
public interface UnitGenerator {

    /**
     * Builds code for the specified descriptors.
     *
     * @param unitDescriptors descriptors
     * @return list of the source code files
     */
    Collection<SourcedJavaType> generate(Iterable<UnitDescriptor> unitDescriptors);



    /**
     * Builds code for the specified descriptors.
     *
     * @param unitDescriptors descriptors
     * @param classLoader class loader
     * @return list of the source code files
     */
    Collection<SourcedJavaType> generate(Iterable<UnitDescriptor> unitDescriptors, ClassLoader classLoader);

}
