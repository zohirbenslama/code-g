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
package org.abstractmeta.code.g.core.generator;

import org.abstractmeta.code.g.CodeGenerator;
import org.abstractmeta.code.g.config.Descriptor;
import org.abstractmeta.code.g.core.CodeGeneratorImpl;
import org.abstractmeta.code.g.core.config.builder.DescriptorBuilder;
import org.abstractmeta.code.g.core.handler.MemCodeHandler;
import org.abstractmeta.code.g.core.handler.SourceCompilerHandler;
import org.abstractmeta.code.g.core.macro.MacroRegistryImpl;
import org.abstractmeta.code.g.core.plugin.ClassGeneratorPlugin;
import org.abstractmeta.code.g.macros.MacroRegistry;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Represents RegistryTest
 *
 * @author Adrian Witas
 */
@Test
public class RegistryTest {


    private final MacroRegistry macroRegistry;

    public RegistryTest() {
        this.macroRegistry = new MacroRegistryImpl();
    }

    public void testRegistry() throws ClassNotFoundException, IOException {
        CodeGenerator codeBuilder = new CodeGeneratorImpl(macroRegistry);
        List<Descriptor> descriptors = Arrays.asList(
                new DescriptorBuilder().setSourceClass(FooRegistry.class.getName()).setPlugin(ClassGeneratorPlugin.class.getName()).build()
        );

        {
        SourceCompilerHandler compilerHandler = new SourceCompilerHandler(File.createTempFile("code-g", "test"));
        codeBuilder.generate(descriptors, compilerHandler);
        List<String> generated = compilerHandler.getGeneratedTypes();
        ClassLoader classLoader = compilerHandler.compile();
        Class generatedClass = classLoader.loadClass(generated.get(0));
        Assert.assertTrue(FooRegistry.class.isAssignableFrom(generatedClass));
        }

        {
            MemCodeHandler compilerHandler = new MemCodeHandler(File.createTempFile("code-g", "test"));
            codeBuilder.generate(descriptors, compilerHandler);
            List<String> generated = compilerHandler.getTypeNames();
            Assert.assertEquals(generated.size(), 1);
        }
    }


    public static interface FooRegistry {

        void register(int id, String name, Long value, Foo foo);

        boolean isRegistered(int id, String name, Long value);

        void unregister(int id, String name, Long value);

        Foo get(int id, String name, Long value);



    }

    public static class Foo {
        private int id;
        private String name;
        private Long longValue;
        private String description;

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Long getLongValue() {
            return longValue;
        }

        public void setLongValue(Long longValue) {
            this.longValue = longValue;
        }
    }

}
