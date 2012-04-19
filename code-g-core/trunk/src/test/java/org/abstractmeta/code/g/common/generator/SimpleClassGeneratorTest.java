package org.abstractmeta.code.g.common.generator;

import org.abstractmeta.code.g.CodeBuilder;
import org.abstractmeta.code.g.common.util.TestCodeStorageHandler;
import org.abstractmeta.code.g.core.CodeBuilderImpl;
import org.abstractmeta.code.g.common.runtime.SourceCompilerStorageHandler;
import org.abstractmeta.code.g.common.test.Bar;
import org.abstractmeta.code.g.core.config.DescriptorBuilder;
import org.abstractmeta.code.g.core.factory.DescriptorFactory;
import org.abstractmeta.code.g.common.generator.test.CollectionTest;
import org.abstractmeta.code.g.config.Descriptor;
import com.google.common.collect.ImmutableList;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;


@Test
public class SimpleClassGeneratorTest {

    public void testBuilderClassGeneratorCollectionCustomization() throws Exception {
        CodeBuilder codeBuilder = new CodeBuilderImpl();
        DescriptorFactory descriptorFactory = new DescriptorFactory();
        Descriptor descriptor = descriptorFactory.create(CollectionTest.class, false);
        descriptor.getImmutableImplementation().put(List.class.getName(), ImmutableList.class.getName() + ".copyOf");
        codeBuilder.addDescriptor(descriptor);
        TestCodeStorageHandler codeHandler = new TestCodeStorageHandler();
        codeBuilder.build(codeHandler);
        Assert.assertEquals(codeHandler.getSourceCode(codeHandler.getTypeNames().get(0)), "");

        SourceCompilerStorageHandler compilerHandler = new SourceCompilerStorageHandler();
        codeBuilder.build(compilerHandler);
        List<String> generated = compilerHandler.getGeneratedTypes();
        ClassLoader classLoader = compilerHandler.compile(Bar.class.getClassLoader());
        Class generatedClass = classLoader.loadClass(generated.get(0));
        CollectionTest collectionTest = (CollectionTest) generatedClass.getConstructors()[0].newInstance(new ArrayList(), new ArrayList());
        Assert.assertNotNull(collectionTest);
        collectionTest.addBar(new BarImpl(1, "id 1"));
        collectionTest.addBar(new BarImpl(2, "id 2"));
        Assert.assertEquals(collectionTest.getBar(1).getId(), 1);
        Assert.assertEquals(collectionTest.getBar("id 2").getId(), 2);

    }


    public void testBuilderClassGenerator() throws Exception {
        DescriptorFactory descriptorFactory = new DescriptorFactory();


        {
            CodeBuilder codeBuilder = new CodeBuilderImpl();
            Descriptor descriptor = descriptorFactory.create(Foo.class, false);
            codeBuilder.addDescriptor(descriptor);
            SourceCompilerStorageHandler compilerHandler = new SourceCompilerStorageHandler();
            codeBuilder.build(compilerHandler);
            List<String> generated = compilerHandler.getGeneratedTypes();
            ClassLoader classLoader = compilerHandler.compile(Foo.class.getClassLoader());
            Class generatedClass = classLoader.loadClass(generated.get(0));
            Foo foo = (Foo) generatedClass.getConstructor(int.class, String.class).newInstance(1, "a");
            Assert.assertEquals(foo.getA(), 1);
            Assert.assertEquals(foo.getZ(), "a");
        }
        {
            CodeBuilder codeBuilder = new CodeBuilderImpl();
            Descriptor descriptor = descriptorFactory.create(AbstractFoo.class, false);
            codeBuilder.addDescriptor(descriptor);
//            TestCodeStorageHandler codeHandler = new TestCodeStorageHandler();
//            codeBuilder.build(codeHandler);
//            Assert.assertEquals(codeHandler.getSourceCode(codeHandler.getTypeNames().get(0)), "");

            SourceCompilerStorageHandler compilerHandler = new SourceCompilerStorageHandler();
            codeBuilder.build(compilerHandler);
            List<String> generated = compilerHandler.getGeneratedTypes();
            ClassLoader classLoader = compilerHandler.compile(Foo.class.getClassLoader());
            Class generatedClass = classLoader.loadClass(generated.get(0));
            AbstractFoo foo = (AbstractFoo) generatedClass.getConstructor(int.class, String.class).newInstance(1, "a");
            Assert.assertEquals(foo.getA(), 1);
            Assert.assertEquals(foo.getZ(), "a");
        }


    }


    public static interface Foo {
        public int getA();

        public String getZ();
    }

    public static abstract class AbstractFoo {
        abstract public int getA();

        abstract public String getZ();
    }


    class BarImpl implements Bar {
        private final int id;
        private final String name;

        BarImpl(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

}
