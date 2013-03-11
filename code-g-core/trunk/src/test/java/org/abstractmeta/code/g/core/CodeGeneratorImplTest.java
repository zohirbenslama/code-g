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
package org.abstractmeta.code.g.core;


import org.testng.annotations.Test;

@Test
public class CodeGeneratorImplTest {

//    private final PropertyRegistry macroRegistry;
//
//    public CodeGeneratorImplTest() {
//        this.macroRegistry = new PropertyRegistryImpl();
//    }
//
//    public void testCodeClassGenerator() throws IOException, ClassNotFoundException {
//        DescriptorGenerator codeGenerator = new DescriptorGeneratorImpl(macroRegistry);
//        List<Descriptor> descriptors = Arrays.asList(
//                new DescriptorBuilder().setSourceClasses(Bar.class.getName()).setPlugin(ClassGeneratorPlugin.class.getName()).build()
//        );
//
//        SourceCompilerHandler compilerHandler = new SourceCompilerHandler(File.createTempFile("code-g", "test"));
//        codeGenerator.generate(descriptors, compilerHandler);
//        List<String> generated = compilerHandler.getGeneratedTypes();
//        Assert.assertEquals(generated.size(), 1);
//        Assert.assertTrue(generated.get(0).endsWith("BarImpl"));
//
//
//        ClassLoader classLoader = compilerHandler.compile();
//        Class generatedClass = classLoader.loadClassesFromSourceDirectory(generated.get(0));
//        Assert.assertTrue(Bar.class.isAssignableFrom(generatedClass));
//    }
//
//    public void testCodeGeneratorWithSimpleClassGenerator() throws Exception {
//        DescriptorGenerator codeGenerator = new DescriptorGeneratorImpl(macroRegistry);
//        List<Descriptor> descriptors = Arrays.asList(
//                new DescriptorBuilder().setSourceClasses(User.class.getName()).setTargetPackagePostfix("impl").setPlugin(ClassGeneratorPlugin.class.getName()).build(),
//                new DescriptorBuilder().setSourcePackages(User.class.getPackage().getName() + ".impl").setPlugin(BuilderGeneratorPlugin.class.getName()).build()
//        );
//
//        SourceCompilerHandler compilerHandler = new SourceCompilerHandler(File.createTempFile("code-g", "test"));
//        codeGenerator.generate(descriptors, compilerHandler);
//
//        List<String> generated = compilerHandler.getGeneratedTypes();
//        Assert.assertEquals(generated.size(), 2 ,"Expected 2 items ,but has  " + generated);
//        ClassLoader classLoader = compilerHandler.compile();
//        Class generatedClass = classLoader.loadClassesFromSourceDirectory(generated.get(1));
//        Object builder = generatedClass.newInstance();
//        ReflectUtil.invokeMethod(builder, "setId", new Class[]{int.class}, 1);
//        ReflectUtil.invokeMethod(builder, "setActive", new Class[]{Boolean.class}, true);
//
//        ReflectUtil.invokeMethod(builder, "addAliases", new Class[]{String[].class}, new Object[]{new String[]{"foo", "bar"}});
//        User user = User.class.cast(ReflectUtil.invokeMethod(builder, "build", new Class[]{}));
//        Assert.assertEquals(user.getId(), 1);
//        Assert.assertEquals(user.getAliases(), Arrays.asList("foo", "bar"));
//        user.getAliases().add("Dummy");
//        Assert.assertEquals(user.getAliases().size(), 3);
//
//    }
//
//
//    @Test(expectedExceptions = UnsupportedOperationException.class)
//    public void testcodeGeneratorWithSimpleClassGeneratorWithCustomCollection() throws Exception {
//        DescriptorGenerator codeGenerator = new DescriptorGeneratorImpl(macroRegistry);
//        List<Descriptor> descriptors = Arrays.asList(
//                new DescriptorBuilder().setSourceClasses(User.class.getName()).setPlugin(ClassGeneratorPlugin.class.getName()).build(),
//                new DescriptorBuilder().addSourcePackages(User.class.getPackage().getName() + ".impl").setPlugin(BuilderGeneratorPlugin.class.getName())
//                        .addImmutableImplementation(List.class.getName(), ImmutableList.class.getName() + ".copyOf")
//                        .build()
//        );
//
//        SourceCompilerHandler compilerHandler = new SourceCompilerHandler(File.createTempFile("code-g", "test"));
//        codeGenerator.generate(descriptors, compilerHandler);
//        List<String> generated = compilerHandler.getGeneratedTypes();
//
//        ClassLoader classLoader = compilerHandler.compile();
//        Class generatedClass = classLoader.loadClassesFromSourceDirectory(generated.get(1));
//        Object builder = generatedClass.newInstance();
//        ReflectUtil.invokeMethod(builder, "setActive", new Class[]{Boolean.class}, true);
//        ReflectUtil.invokeMethod(builder, "addAliases", new Class[]{String[].class}, new Object[]{new String[]{"foo", "bar"}});
//        Assert.assertNotNull(ReflectUtil.invokeMethod(builder, "getAliases", new Class[]{}));
//
//        User user = User.class.cast(ReflectUtil.invokeMethod(builder, "build", new Class[]{}));
//        Assert.assertEquals(user.getAliases(), Arrays.asList("foo", "bar"));
//        user.getAliases().clear();
//    }


}
