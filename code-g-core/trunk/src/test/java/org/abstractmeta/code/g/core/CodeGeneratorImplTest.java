package org.abstractmeta.code.g.core;


import com.google.common.collect.ImmutableList;
import org.abstractmeta.code.g.CodeGenerator;
import org.abstractmeta.code.g.config.Descriptor;
import org.abstractmeta.code.g.core.config.DescriptorBuilder;
import org.abstractmeta.code.g.core.plugin.BuilderGenerator;
import org.abstractmeta.code.g.core.plugin.ClassGenerator;
import org.abstractmeta.code.g.core.runtime.SourceCompilerHandler;
import org.abstractmeta.code.g.core.test.Bar;
import org.abstractmeta.code.g.core.test.User;
import org.abstractmeta.code.g.core.util.TestHelper;
import org.testng.Assert;
import org.testng.annotations.Test;
import sun.tools.jstat.Literal;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Test
public class CodeGeneratorImplTest {



    public void testCodeClassGenerator() throws IOException, ClassNotFoundException {
        CodeGenerator codeBuilder = new CodeGeneratorImpl();
        List<Descriptor> descriptors = Arrays.asList(
                new DescriptorBuilder().setSourceClass(Bar.class.getName()).setPlugin(ClassGenerator.class.getName()).build()
        );
        SourceCompilerHandler compilerHandler = new SourceCompilerHandler();
        codeBuilder.build(descriptors, compilerHandler);
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
            new DescriptorBuilder().setSourceClass(User.class.getName()).setPlugin(ClassGenerator.class.getName()).build(),
            new DescriptorBuilder().setSourcePackage(User.class.getPackage().getName() + ".impl").setPlugin(BuilderGenerator.class.getName()).build()
        );

        SourceCompilerHandler compilerHandler = new SourceCompilerHandler();
        codeBuilder.build(descriptors, compilerHandler);
        List<String> generated = compilerHandler.getGeneratedTypes();

        Assert.assertEquals(generated.size(), 2);
        ClassLoader classLoader = compilerHandler.compile();
        Class generatedClass = classLoader.loadClass(generated.get(1));
        Object builder = generatedClass.newInstance();
        TestHelper.invokeMethod(builder, "setId", new Class[]{int.class}, 1);
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
                new DescriptorBuilder().setSourceClass(User.class.getName()).setPlugin(ClassGenerator.class.getName()).build(),
                new DescriptorBuilder().setSourcePackage(User.class.getPackage().getName() + ".impl").setPlugin(BuilderGenerator.class.getName())
                        .addImmutableImplementation(List.class.getName(), ImmutableList.class.getName() + ".copyOf")
                        .build()
        );

        SourceCompilerHandler compilerHandler = new SourceCompilerHandler();
        codeBuilder.build(descriptors, compilerHandler);
        List<String> generated = compilerHandler.getGeneratedTypes();

        ClassLoader classLoader = compilerHandler.compile();
        Class generatedClass = classLoader.loadClass(generated.get(1));
        Object builder = generatedClass.newInstance();

        TestHelper.invokeMethod(builder, "addAliases", new Class[]{String[].class}, new Object[]{new String[]{"foo", "bar"}});

        User user = User.class.cast(TestHelper.invokeMethod(builder, "build", new Class[]{}));
        Assert.assertEquals(user.getAliases(), Arrays.asList("foo", "bar"));
        user.getAliases().clear();
    }


}
