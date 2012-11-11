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

import org.abstractmeta.code.g.CodeGenerator;
import org.abstractmeta.code.g.UnitGenerator;
import org.abstractmeta.code.g.code.JavaType;
import org.abstractmeta.code.g.code.JavaTypeRegistry;
import org.abstractmeta.code.g.config.Descriptor;
import org.abstractmeta.code.g.config.UnitDescriptor;
import org.abstractmeta.code.g.config.loader.JavaSourceLoader;
import org.abstractmeta.code.g.core.config.builder.DescriptorBuilder;
import org.abstractmeta.code.g.core.config.loader.JavaSourceLoaderImpl;
import org.abstractmeta.code.g.core.handler.PersistenceCodeHandler;
import org.abstractmeta.code.g.core.util.MacroUtil;
import org.abstractmeta.code.g.handler.CodeHandler;
import org.abstractmeta.code.g.macros.MacroRegistry;
import org.abstractmeta.toolbox.compilation.compiler.JavaSourceCompiler;
import org.abstractmeta.toolbox.compilation.compiler.impl.JavaSourceCompilerImpl;

import java.io.File;
import java.util.*;

/**
 * Default UnitGenerator implementation.
 *
 * @author Adrian Witas
 */
public class UnitGeneratorImpl implements UnitGenerator {

    private final JavaSourceLoader sourceLoader;
    private final MacroRegistry macroRegistry;

    public UnitGeneratorImpl(MacroRegistry macroRegistry) {
        this(new JavaSourceLoaderImpl(), macroRegistry);
    }

    protected UnitGeneratorImpl(JavaSourceLoader sourceLoader, MacroRegistry macroRegistry) {
        this.sourceLoader = sourceLoader;
        this.macroRegistry = macroRegistry;
    }


    public Collection<File> generate(UnitDescriptor unitDescriptor, List<JavaType> generatedTypes, Set<Descriptor> postPlugins) {
        JavaSourceCompiler javaSourceCompiler = new JavaSourceCompilerImpl();
        JavaSourceCompiler.CompilationUnit compilationUnit = javaSourceCompiler.createCompilationUnit();
        compilationUnit.addClassPathEntries(MacroUtil.substitute(macroRegistry, unitDescriptor.getClassPathEntries(), new ArrayList<String>()));
        Collection<Descriptor> descriptors = getDescriptor(unitDescriptor);
        ClassLoader classLoader;

        if (unitDescriptor.getSourceDirectory() != null) {
            File sourceDirectory = new File(MacroUtil.substitute(macroRegistry, unitDescriptor.getSourceDirectory()));
            Map<String, String> javaSources = sourceLoader.load(sourceDirectory, descriptors);
            for (String className : javaSources.keySet()) {
                compilationUnit.addJavaSource(className, javaSources.get(className));
            }
            classLoader = javaSourceCompiler.compile(compilationUnit);
        } else {
            classLoader = this.getClass().getClassLoader();
        }


        PersistenceCodeHandler codeHandler = new PersistenceCodeHandler(new File(MacroUtil.substitute(macroRegistry, unitDescriptor.getTargetDirectory())));
        CodeGenerator codeGenerator = new CodeGeneratorImpl(macroRegistry);

        Collection<JavaType> generatedTypeForThisUnit = codeGenerator.generate(descriptors, codeHandler, classLoader);
        generatedTypes.addAll(generatedTypeForThisUnit);
        if (unitDescriptor.getPostDescriptor() != null) {
            postPlugins.add(unitDescriptor.getPostDescriptor());
        }
        return codeHandler.getGeneratedFiles();
    }


    @Override
    public Collection<File> generate(Iterable<UnitDescriptor> unitDescriptors) {
        List<JavaType> generatedTypes = new ArrayList<JavaType>();
        Set<Descriptor> postDescriptors = new HashSet<Descriptor>();
        Collection<File> result = new HashSet<File>();
        for (UnitDescriptor descriptor : unitDescriptors) {
            Collection<File> filesForThisUnit = generate(descriptor, generatedTypes, postDescriptors);
            result.addAll(filesForThisUnit);
        }
        for (UnitDescriptor unitDescriptor : unitDescriptors) {
            applyPostDescriptors(unitDescriptor, generatedTypes);
        }
        return result;
    }

    private void applyPostDescriptors(UnitDescriptor unitDescriptor, List<JavaType> generatedTypes) {
        Descriptor postDescriptor = unitDescriptor.getPostDescriptor();
        if (postDescriptor == null) return;
        CodeGeneratorImpl codeGenerator = new CodeGeneratorImpl(macroRegistry);
        JavaTypeRegistry typeRegistry = codeGenerator.getRegistryProvider().get();
        for (JavaType javaType : generatedTypes) {
            typeRegistry.register(javaType);
        }
        PersistenceCodeHandler codeHandler = new PersistenceCodeHandler(new File(MacroUtil.substitute(macroRegistry, unitDescriptor.getTargetDirectory())));
        Descriptor descriptor = codeGenerator.substitute(postDescriptor);
        codeGenerator.runPlugin(typeRegistry, Collections.<String>emptyList(), this.getClass().getClassLoader(), descriptor, codeHandler);
    }


    protected List<Descriptor> getDescriptor(UnitDescriptor unitDescriptor) {
        List<Descriptor> result = new ArrayList<Descriptor>();
        String sourcePackage = MacroUtil.substitute(macroRegistry, unitDescriptor.getSourcePackage());
        for (Descriptor descriptor : unitDescriptor.getDescriptors()) {
            DescriptorBuilder descriptorBuilder = new DescriptorBuilder();
            descriptorBuilder.merge(descriptor);
            if (descriptor.getSourcePackage() == null && descriptor.getSourceClass() == null) {
                descriptorBuilder.setSourcePackage(sourcePackage);
            }
            result.add(descriptorBuilder.build());

        }
        return result;
    }


}

