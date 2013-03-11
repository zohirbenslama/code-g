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

import org.testng.annotations.Test;

/**
 * Represents RegistryTest
 *
 * @author Adrian Witas
 */
@Test
public class RegistryTest {

//
//    private final PropertyRegistry macroRegistry;
//
//    public RegistryTest() {
//        this.macroRegistry = new PropertyRegistryImpl();
//    }
//
//    public void testRegistry() throws ClassNotFoundException, IOException {
//        DescriptorGenerator codeBuilder = new DescriptorGeneratorImpl(macroRegistry);
//        List<Descriptor> descriptors = Arrays.asList(
//                new DescriptorBuilder().setSourceClasses(FooRegistry.class.getName()).setPlugin(ClassGeneratorPlugin.class.getName()).build()
//        );
//
//        {
//        SourceCompilerHandler compilerHandler = new SourceCompilerHandler(File.createTempFile("code-g", "test"));
//        codeBuilder.generate(descriptors, compilerHandler);
//        List<String> generated = compilerHandler.getGeneratedTypes();
//        ClassLoader classLoader = compilerHandler.compile();
//        Class generatedClass = classLoader.loadClassesFromSourceDirectory(generated.get(0));
//        Assert.assertTrue(FooRegistry.class.isAssignableFrom(generatedClass));
//        }
//
//        {
//            MemCodeHandler compilerHandler = new MemCodeHandler(File.createTempFile("code-g", "test"));
//            codeBuilder.generate(descriptors, compilerHandler);
//            List<String> generated = compilerHandler.getTypeNames();
//            Assert.assertEquals(generated.size(), 1);
//        }
//    }
//
//
//    public static interface FooRegistry {
//
//        void register(int id, String name, Long value, Foo foo);
//
//        boolean isRegistered(int id, String name, Long value);
//
//        void unregister(int id, String name, Long value);
//
//        Foo get(int id, String name, Long value);
//
//
//
//    }
//
//    public static class Foo {
//        private int id;
//        private String name;
//        private Long longValue;
//        private String description;
//
//        public int getId() {
//            return id;
//        }
//
//        public String getName() {
//            return name;
//        }
//
//        public String getDescription() {
//            return description;
//        }
//
//        public void setId(int id) {
//            this.id = id;
//        }
//
//        public void setName(String name) {
//            this.name = name;
//        }
//
//        public void setDescription(String description) {
//            this.description = description;
//        }
//
//        public Long getLongValue() {
//            return longValue;
//        }
//
//        public void setLongValue(Long longValue) {
//            this.longValue = longValue;
//        }
//    }

}
