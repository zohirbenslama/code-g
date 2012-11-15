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
import org.abstractmeta.code.g.config.Descriptor;
import org.abstractmeta.code.g.handler.CodeHandler;

import java.util.Collection;


/**
 * Represents a code builder.
 * <p>This abstraction is responsible for building code.
 * It uses code generation plugins to generate a custom code.
 * </p>
 * <p><b>Usage:</b>
 * <code><pre>
 *     CodeGenerator codeGenerator = new CodeBuilderImpl();
 *     DescriptorFactory descriptorFactory = new DescriptorFactory();
 *     Collection&lt;Descriptor> descriptors = Arrays.asList(
 *         ...
 *     );
 *     PersistenceHandler persistenceHandler = new PersistenceHandler(new File("target/generated/code-r/"));
 *     codeGenerator.generate(descriptors, persistenceHandler);
 *     List<File> generated = persistenceHandler.getGeneratedFiles();
 *
 * </pre></code>
 * <p/>
 * </p>
 */
public interface CodeGenerator {


    /**
     * Builds code for the specified descriptors.
     *
     * @param descriptors descriptors
     * @param handler     code handler
     * @param classLoader class loader
     */
    Collection<SourcedJavaType> generate(Iterable<Descriptor> descriptors, CodeHandler handler, ClassLoader classLoader);


    /**
     * Builds code for the specified descriptors.
     *
     * @param descriptors descriptors
     * @param handler     code handler
     */
    Collection<SourcedJavaType>  generate(Iterable<Descriptor> descriptors, CodeHandler handler);


}
