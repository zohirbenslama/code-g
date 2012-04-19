package org.abstractmeta.code.g.common;


import org.abstractmeta.code.g.CodeBuilder;
import org.abstractmeta.code.g.core.factory.DescriptorFactory;
import org.abstractmeta.code.g.common.runtime.SourceCompilerStorageHandler;
import org.abstractmeta.code.g.common.test.Foo;
import org.abstractmeta.code.g.common.test.User;
import org.abstractmeta.code.g.common.util.TestHelper;
import org.abstractmeta.code.g.config.Descriptor;
import com.google.common.collect.ImmutableList;
import org.abstractmeta.code.g.core.CodeBuilderImpl;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Test
public class CodeBuilderTest {



    public void testCodeClassGenerator() throws IOException, ClassNotFoundException {
        CodeBuilder codeBuilder = new CodeBuilderImpl();
        DescriptorFactory descriptorFactory = new DescriptorFactory();
        codeBuilder.addDescriptor(descriptorFactory.create(Foo.class, false));
        SourceCompilerStorageHandler compilerHandler = new SourceCompilerStorageHandler();
        codeBuilder.build(compilerHandler);
        List<String> generated = compilerHandler.getGeneratedTypes();
        Assert.assertEquals(generated.size(), 1);
        Assert.assertTrue(generated.get(0).endsWith("FooImpl"));
        ClassLoader classLoader = compilerHandler.compile();
        Class generatedClass = classLoader.loadClass(generated.get(0));
        Assert.assertTrue(Foo.class.isAssignableFrom(generatedClass));
    }

    public void testCodeBuilderWithSimpleClassGenerator() throws Exception {
        CodeBuilder codeBuilder = new CodeBuilderImpl();
        DescriptorFactory descriptorFactory = new DescriptorFactory();
        codeBuilder.addDescriptor(descriptorFactory.create(User.class, true));



        SourceCompilerStorageHandler compilerHandler = new SourceCompilerStorageHandler();
        codeBuilder.build(compilerHandler);
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


    public void testCodeBuilderWithSimpleClassGeneratorWithCustomCollection() throws Exception {
        CodeBuilder codeBuilder = new CodeBuilderImpl();
        DescriptorFactory descriptorFactory = new DescriptorFactory();
        Descriptor descriptor = descriptorFactory.create(User.class, true);
        descriptor.getImmutableImplementation().put(List.class.getName(), ImmutableList.class.getName() + ".copyOf");
        codeBuilder.addDescriptor(descriptor);

//        TestCodeStorageHandler codeHandler = new TestCodeStorageHandler();
//        codeBuilder.build(codeHandler);
//        Assert.assertEquals(codeHandler.getSourceCode(codeHandler.getTypeNames().get(1)), "");
        SourceCompilerStorageHandler compilerHandler = new SourceCompilerStorageHandler();
        codeBuilder.build(compilerHandler);
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
        UnsupportedOperationException expectedException = null;
        try {
        user.getAliases().add("Dummy");
        } catch (UnsupportedOperationException e) {
            expectedException = e;
        }
        Assert.assertNotNull(expectedException, "should not update immutable list");
    }


}
