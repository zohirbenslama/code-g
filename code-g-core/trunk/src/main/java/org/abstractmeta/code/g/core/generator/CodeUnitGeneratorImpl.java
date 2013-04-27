package org.abstractmeta.code.g.core.generator;

import com.google.common.io.Files;
import org.abstractmeta.code.g.code.CompiledJavaType;
import org.abstractmeta.code.g.code.CompiledJavaTypeRegistry;
import org.abstractmeta.code.g.code.JavaType;
import org.abstractmeta.code.g.config.Descriptor;
import org.abstractmeta.code.g.config.UnitDescriptor;
import org.abstractmeta.code.g.core.code.CompiledJavaTypeRegistryImpl;
import org.abstractmeta.code.g.core.util.ReflectUtil;
import org.abstractmeta.code.g.generator.CodeGenerator;
import org.abstractmeta.code.g.generator.CodeUnitGenerator;
import org.abstractmeta.code.g.generator.Context;
import org.abstractmeta.code.g.generator.GeneratedCode;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

/**
 * Represents CodeUnitGeneratorImpl
 *
 * @author Adrian Witas
 */
public class CodeUnitGeneratorImpl implements CodeUnitGenerator {

    @Override
    public GeneratedCode generate(UnitDescriptor unitDescriptor) {
        GeneratedCodeImpl result = new GeneratedCodeImpl();
        result.setRegistry(new CompiledJavaTypeRegistryImpl());
        ContextImpl context = new ContextImpl();
        context.put(CompiledJavaTypeRegistry.class, result.getRegistry());
        for (Descriptor descriptor : unitDescriptor.getDescriptors()) {
            generate(descriptor, context, result.getRegistry());
        }
        if (unitDescriptor.getPostDescriptor() != null) {
            generate(unitDescriptor.getPostDescriptor(), context, result.getRegistry());
        }
        if (context.contains(ClassLoader.class)) {
            result.setClassLoader(context.get(ClassLoader.class));
        }
        result.setUnitDescriptor(unitDescriptor);
        persistJavaSources(result);
        return result;

    }

    protected void generate(Descriptor descriptor, Context context, CompiledJavaTypeRegistry registry) {
        CodeGenerator codeGenerator = ReflectUtil.getInstance(CodeGenerator.class, descriptor.getGeneratorClass());
        context.replace(Descriptor.class, descriptor);
        @SuppressWarnings("unchecked")
        Collection<CompiledJavaType> generatedTypes = codeGenerator.generate(context);
        if (generatedTypes == null) return;
        for (CompiledJavaType compiledJavaType : generatedTypes) {
            registry.register(compiledJavaType);
        }
    }

    protected void persistJavaSources(GeneratedCodeImpl result) {
        UnitDescriptor unitDescriptor = result.getUnitDescriptor();
        if (unitDescriptor.getTargetSourceDirectory() != null) {
            File targetSourceDirectory = new File(unitDescriptor.getTargetSourceDirectory());
            checkIfExistOrCreate(targetSourceDirectory);
            for (CompiledJavaType compiledJavaType : result.getRegistry().get()) {
                persistJavaClass(targetSourceDirectory, compiledJavaType);
            }
        }
    }

    private void persistJavaClass(File targetSourceDirectory, CompiledJavaType compiledJavaType) {
        JavaType javaType = compiledJavaType.getType();
        if(javaType.isNested()) return;
        File packageDirectory = new File(targetSourceDirectory, javaType.getPackageName().replace(".", "/"));
        checkIfExistOrCreate(packageDirectory);
        File javaClassFile = new File(packageDirectory, javaType.getSimpleName() + ".java");
        try {
            Files.write(compiledJavaType.getSourceCode().toString().getBytes(), javaClassFile);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to write class " + javaClassFile.getAbsolutePath(), e);
        }
    }

    protected void checkIfExistOrCreate(File directory) {
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                throw new IllegalStateException("Failed to create directory:" + directory.getAbsolutePath());
            }
        }
    }




}
