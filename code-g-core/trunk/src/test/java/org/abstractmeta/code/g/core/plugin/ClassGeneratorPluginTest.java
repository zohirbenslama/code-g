package org.abstractmeta.code.g.core.plugin;

import org.abstractmeta.code.g.CodeGenerator;
import org.abstractmeta.code.g.config.Descriptor;
import org.abstractmeta.code.g.core.CodeGeneratorImpl;
import org.abstractmeta.code.g.core.config.builder.DescriptorBuilder;
import org.abstractmeta.code.g.core.handler.SourceCompilerHandler;
import org.abstractmeta.code.g.core.macro.MacroRegistryImpl;
import org.abstractmeta.code.g.core.test.GenericInterfaceType;
import org.abstractmeta.code.g.core.test.GenericSubInterface;
import org.abstractmeta.code.g.macros.MacroRegistry;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Represents ClassGeneratorPluginTest
 *
 * @author Adrian Witas
 */
@Test
public class ClassGeneratorPluginTest {

    private final MacroRegistry macroRegistry;

    public ClassGeneratorPluginTest() {
        this.macroRegistry = new MacroRegistryImpl();
    }


    public void testGenericInterfaceImplementation() throws ClassNotFoundException {
        CodeGenerator codeBuilder = new CodeGeneratorImpl(macroRegistry);
        List<Descriptor> descriptors = Arrays.asList(
                new DescriptorBuilder().setSourceClass(GenericInterfaceType.class.getName()).setPlugin(ClassGeneratorPlugin.class.getName()).build()
        );

        {
            SourceCompilerHandler compilerHandler = new SourceCompilerHandler();
            codeBuilder.generate(descriptors, compilerHandler);
            List<String> generated = compilerHandler.getGeneratedTypes();
            Assert.assertEquals(generated.size(), 1);
            Assert.assertTrue(generated.get(0).endsWith("GenericInterfaceTypeImpl"));
            ClassLoader classLoader = compilerHandler.compile();
            Class generatedClass = classLoader.loadClass(generated.get(0));
            Assert.assertTrue(GenericInterfaceType.class.isAssignableFrom(generatedClass));
        }
//
//        {
//            MemCodeHandler codeHandler = new MemCodeHandler();
//            codeBuilder.generate(descriptors, codeHandler);
//            Assert.assertEquals(codeHandler.getSourceCode(codeHandler.getTypeNames().get(0)), "");
//        }
    }

    public void testGenericSubInterfaceImplementation() throws ClassNotFoundException {
        CodeGenerator codeBuilder = new CodeGeneratorImpl(macroRegistry);
        List<Descriptor> descriptors = Arrays.asList(
                new DescriptorBuilder().setSourceClass(GenericSubInterface.class.getName()).setPlugin(ClassGeneratorPlugin.class.getName()).build()
        );

        {
            SourceCompilerHandler compilerHandler = new SourceCompilerHandler();
            codeBuilder.generate(descriptors, compilerHandler);
            List<String> generated = compilerHandler.getGeneratedTypes();
            Assert.assertEquals(generated.size(), 1);
            Assert.assertTrue(generated.get(0).endsWith("GenericSubInterfaceImpl"));
            ClassLoader classLoader = compilerHandler.compile();
            Class generatedClass = classLoader.loadClass(generated.get(0));
            Assert.assertTrue(GenericSubInterface.class.isAssignableFrom(generatedClass));
        }

    }


}
