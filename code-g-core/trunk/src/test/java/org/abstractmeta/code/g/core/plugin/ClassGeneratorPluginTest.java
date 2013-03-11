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
package org.abstractmeta.code.g.core.plugin;

import org.testng.annotations.Test;

/**
 * This test basic class generator plugin.
 *
 * @author Adrian Witas
 */
@Test
public class ClassGeneratorPluginTest {

//    private final UnitDescriptorsDecoder decoder;
//    private final PropertyRegistry macroRegistry;
//
//    public ClassGeneratorPluginTest() {
//        this.macroRegistry = new PropertyRegistryImpl();
//        this.decoder = new UnitDescriptorsDecoder();
//        File baseDirectory = new File(".").getAbsoluteFile();
//        this.macroRegistry.register("${basedir}", baseDirectory.getAbsolutePath());
//    }
//
//
//    public void testGenericInterfaceImplementation() throws ClassNotFoundException {
//        DescriptorGenerator codeBuilder = new DescriptorGeneratorImpl(macroRegistry);
//        List<Descriptor> descriptors = Arrays.asList(
//                new DescriptorBuilder().setSourceClasses(GenericInterfaceType.class.getName()).setPlugin(ClassGeneratorPlugin.class.getName()).build()
//        );
//
//        {
//            SourceCompilerHandler compilerHandler = new SourceCompilerHandler();
//            codeBuilder.generate(descriptors, compilerHandler);
//            List<String> generated = compilerHandler.getGeneratedTypes();
//            Assert.assertEquals(generated.size(), 1);
//            Assert.assertTrue(generated.get(0).endsWith("GenericInterfaceTypeImpl"));
//            ClassLoader classLoader = compilerHandler.compile();
//            Class generatedClass = classLoader.loadClassesFromSourceDirectory(generated.get(0));
//            Assert.assertTrue(GenericInterfaceType.class.isAssignableFrom(generatedClass));
//        }
//    }
//
//
//    public void testGenericSubInterfaceImplementation() throws ClassNotFoundException {
//        DescriptorGenerator codeBuilder = new DescriptorGeneratorImpl(macroRegistry);
//        List<Descriptor> descriptors = Arrays.asList(
//                new DescriptorBuilder().setSourceClasses(GenericSubInterface.class.getName()).setPlugin(ClassGeneratorPlugin.class.getName()).build()
//        );
//
//        {
//            SourceCompilerHandler compilerHandler = new SourceCompilerHandler();
//            codeBuilder.generate(descriptors, compilerHandler);
//            List<String> generated = compilerHandler.getGeneratedTypes();
//            Assert.assertEquals(generated.size(), 1);
//            Assert.assertTrue(generated.get(0).endsWith("GenericSubInterfaceImpl"));
//            ClassLoader classLoader = compilerHandler.compile();
//            Class generatedClass = classLoader.loadClassesFromSourceDirectory(generated.get(0));
//            Assert.assertTrue(GenericSubInterface.class.isAssignableFrom(generatedClass));
//        }
//
//    }
//
//
//    public void testSubClassImplementation() throws Exception {
//        Properties properties = PropertiesUtil.loadFromFile(new File("src/test/code-g/super-unit-provider-test.properties"));
//        List<UnitDescriptor> unitDescriptors = decoder.decode(Maps.fromProperties(properties));
//        CodeUnitGenerator unitGenerator = new CodeUnitGeneratorImpl(macroRegistry, new SourceCompilerHandler.Factory());
//        List<SourcedJavaType> generated = new ArrayList<SourcedJavaType>(unitGenerator.generate(unitDescriptors));
//        Class messageImpType = unitDescriptors.get(0).getClassLoader().loadClassesFromSourceDirectory(generated.get(0).getType().getName());
//        AMessage message = AMessage.class.cast(messageImpType.getConstructor(int.class, String.class).newInstance(1, "Test"));
//        Assert.assertEquals(message.getId(), 1);
//        Assert.assertEquals(message.getName(), "Test");
//
//    }
//
//    public void testTrackedSubClassImplementation() throws Exception {
//        Properties properties = PropertiesUtil.loadFromFile(new File("src/test/code-g/command-unit-provider-test.properties"));
//        List<UnitDescriptor> unitDescriptors = decoder.decode(Maps.fromProperties(properties));
//        CodeUnitGenerator unitGenerator = new CodeUnitGeneratorImpl(macroRegistry, new MemCodeHandler.Factory());
//
//        List<SourcedJavaType> generated = new ArrayList<SourcedJavaType>(unitGenerator.generate(unitDescriptors));
//        Assert.assertEquals(generated.get(0).getSourceCode(), "");
//    }


}
