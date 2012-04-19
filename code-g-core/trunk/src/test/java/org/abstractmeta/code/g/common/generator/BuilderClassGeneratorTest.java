package org.abstractmeta.code.g.common.generator;


import org.abstractmeta.code.g.CodeBuilder;
import org.abstractmeta.code.g.core.CodeBuilderImpl;
import org.abstractmeta.code.g.core.config.DescriptorBuilder;
import org.abstractmeta.code.g.core.factory.DescriptorFactory;
import org.abstractmeta.code.g.common.runtime.SourceCompilerStorageHandler;
import org.abstractmeta.code.g.config.Descriptor;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

@Test
public class BuilderClassGeneratorTest {


    public void testBuilderClassGenerator() throws Exception {
        CodeBuilder codeBuilder = new CodeBuilderImpl();
        DescriptorFactory descriptorFactory = new DescriptorFactory();
        Descriptor descriptor = descriptorFactory.create(Foo.class, true);

        codeBuilder.addDescriptor(new DescriptorBuilder().setInterfaces(Builder.class.getName()).merge(descriptor).build());
//        TestCodeStorageHandler codeHandler = new TestCodeStorageHandler();
//        codeBuilder.build(codeHandler);
//        Assert.assertEquals(codeHandler.getSourceCode(codeHandler.getTypeNames().get(0)), "");

        SourceCompilerStorageHandler compilerHandler = new SourceCompilerStorageHandler();
        codeBuilder.build(compilerHandler);
        List<String> generated = compilerHandler.getGeneratedTypes();
        ClassLoader classLoader = compilerHandler.compile(Foo.class.getClassLoader());
        Class generatedClass = classLoader.loadClass(generated.get(0));
        Builder builder = (Builder) generatedClass.newInstance();
        builder.setA(12);
        Foo foo = builder.build();
        Assert.assertEquals(foo.getA(), 12);
    }

    
    public static interface Builder {

        Foo build();
        
        Builder setA(int a);
    }

    public static class Foo {
        private final int a;
        private final String z;

        public Foo(int a, String z) {
            this.a = a;
            this.z = z;
        }

        public int getA() {
            return a;
        }

        public String getZ() {
            return z;
        }
    }
}
