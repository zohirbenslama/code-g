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


import com.google.common.collect.ImmutableList;
import org.abstractmeta.code.g.CodeGenerator;
import org.abstractmeta.code.g.config.Descriptor;
import org.abstractmeta.code.g.core.config.builder.DescriptorBuilder;
import org.abstractmeta.code.g.core.handler.SourceCompilerHandler;
import org.abstractmeta.code.g.core.plugin.BuilderGeneratorPlugin;
import org.abstractmeta.code.g.core.plugin.ClassGeneratorPlugin;
import org.abstractmeta.code.g.core.test.Bar;
import org.abstractmeta.code.g.core.test.User;
import org.abstractmeta.code.g.core.util.TestHelper;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.annotation.Nonnull;
import javax.annotation.meta.When;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Test
public class CodeGeneratorImplTest {


    public void testCodeClassGenerator() throws IOException, ClassNotFoundException {
        CodeGenerator codeBuilder = new CodeGeneratorImpl();
        List<Descriptor> descriptors = Arrays.asList(
                new DescriptorBuilder().setSourceClass(Bar.class.getName()).setPlugin(ClassGeneratorPlugin.class.getName()).build()
        );
        SourceCompilerHandler compilerHandler = new SourceCompilerHandler();
        codeBuilder.generate(descriptors, compilerHandler);
        List<String> generated = compilerHandler.getGeneratedTypes();
        Assert.assertEquals(generated.size(), 1);
        Assert.assertTrue(generated.get(0).endsWith("BarImpl"));
        ClassLoader classLoader = compilerHandler.compile();
        Class generatedClass = classLoader.loadClass(generated.get(0));
        Assert.assertTrue(Bar.class.isAssignableFrom(generatedClass));
    }

    public void testCodeBuilderWithSimpleClassGenerator() throws Exception {
        CodeGenerator codeBuilder = new CodeGeneratorImpl();
        List<Descriptor> descriptors = Arrays.asList(
                new DescriptorBuilder().setSourceClass(User.class.getName()).setPlugin(ClassGeneratorPlugin.class.getName()).build(),
                new DescriptorBuilder().setSourcePackage(User.class.getPackage().getName() + ".impl").setPlugin(BuilderGeneratorPlugin.class.getName()).build()
        );

        SourceCompilerHandler compilerHandler = new SourceCompilerHandler();
        codeBuilder.generate(descriptors, compilerHandler);
        List<String> generated = compilerHandler.getGeneratedTypes();

        Assert.assertEquals(generated.size(), 2);
        ClassLoader classLoader = compilerHandler.compile();
        Class generatedClass = classLoader.loadClass(generated.get(1));
        Object builder = generatedClass.newInstance();
        TestHelper.invokeMethod(builder, "setId", new Class[]{int.class}, 1);
        TestHelper.invokeMethod(builder, "setActive", new Class[]{Boolean.class}, true);

        TestHelper.invokeMethod(builder, "addAliases", new Class[]{String[].class}, new Object[]{new String[]{"foo", "bar"}});
        User user = User.class.cast(TestHelper.invokeMethod(builder, "build", new Class[]{}));
        Assert.assertEquals(user.getId(), 1);
        Assert.assertEquals(user.getAliases(), Arrays.asList("foo", "bar"));
        user.getAliases().add("Dummy");
        Assert.assertEquals(user.getAliases().size(), 3);

    }


    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testCodeBuilderWithSimpleClassGeneratorWithCustomCollection() throws Exception {
        CodeGenerator codeBuilder = new CodeGeneratorImpl();
        List<Descriptor> descriptors = Arrays.asList(
                new DescriptorBuilder().setSourceClass(User.class.getName()).setPlugin(ClassGeneratorPlugin.class.getName()).build(),
                new DescriptorBuilder().setSourcePackage(User.class.getPackage().getName() + ".impl").setPlugin(BuilderGeneratorPlugin.class.getName())
                        .addImmutableImplementation(List.class.getName(), ImmutableList.class.getName() + ".copyOf")
                        .build()
        );

        SourceCompilerHandler compilerHandler = new SourceCompilerHandler();
        codeBuilder.generate(descriptors, compilerHandler);
        List<String> generated = compilerHandler.getGeneratedTypes();

        ClassLoader classLoader = compilerHandler.compile();
        Class generatedClass = classLoader.loadClass(generated.get(1));
        Object builder = generatedClass.newInstance();
        TestHelper.invokeMethod(builder, "setActive", new Class[]{Boolean.class}, true);
        TestHelper.invokeMethod(builder, "addAliases", new Class[]{String[].class}, new Object[]{new String[]{"foo", "bar"}});
        Assert.assertNotNull(TestHelper.invokeMethod(builder, "getAliases", new Class[]{}));

        User user = User.class.cast(TestHelper.invokeMethod(builder, "build", new Class[]{}));
        Assert.assertEquals(user.getAliases(), Arrays.asList("foo", "bar"));
        user.getAliases().clear();
    }


}
