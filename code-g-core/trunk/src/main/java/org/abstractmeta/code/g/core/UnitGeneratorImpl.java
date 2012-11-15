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
import org.abstractmeta.code.g.code.JavaTypeRegistry;
import org.abstractmeta.code.g.code.SourcedJavaType;
import org.abstractmeta.code.g.config.Descriptor;
import org.abstractmeta.code.g.config.UnitDescriptor;
import org.abstractmeta.code.g.config.loader.JavaSourceLoader;
import org.abstractmeta.code.g.core.config.builder.DescriptorBuilder;
import org.abstractmeta.code.g.core.config.loader.JavaSourceLoaderImpl;
import org.abstractmeta.code.g.core.handler.PersistenceCodeHandler;
import org.abstractmeta.code.g.core.util.MacroUtil;
import org.abstractmeta.code.g.handler.CodeHandler;
import org.abstractmeta.code.g.handler.CodeHandlerFactory;
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
    private final CodeHandlerFactory codeHandlerFactory;


    public UnitGeneratorImpl(MacroRegistry macroRegistry, CodeHandlerFactory codeHandlerFactory) {
        this(new JavaSourceLoaderImpl(), macroRegistry, codeHandlerFactory);
    }

    protected UnitGeneratorImpl(JavaSourceLoader sourceLoader, MacroRegistry macroRegistry, CodeHandlerFactory codeHandlerFactory) {
        this.sourceLoader = sourceLoader;
        this.macroRegistry = macroRegistry;
        this.codeHandlerFactory = codeHandlerFactory;
    }


    public void generate(UnitDescriptor unitDescriptor, List<SourcedJavaType> generatedTypes, Set<Descriptor> postPlugins, ClassLoader classLoader) {
        CodeGenerator codeGenerator = new CodeGeneratorImpl(macroRegistry);
        JavaSourceCompiler javaSourceCompiler = new JavaSourceCompilerImpl();
        JavaSourceCompiler.CompilationUnit compilationUnit = javaSourceCompiler.createCompilationUnit();
        compilationUnit.addClassPathEntries(MacroUtil.substitute(macroRegistry, unitDescriptor.getClassPathEntries(), new ArrayList<String>()));
        Collection<Descriptor> descriptors = getDescriptor(unitDescriptor);
        if (unitDescriptor.getSourceDirectory() != null) {
            File sourceDirectory = new File(MacroUtil.substitute(macroRegistry, unitDescriptor.getSourceDirectory()));
            Map<String, String> javaSources = sourceLoader.load(sourceDirectory, descriptors);
            for (String className : javaSources.keySet()) {
                compilationUnit.addJavaSource(className, javaSources.get(className));
            }
            classLoader = javaSourceCompiler.compile(classLoader, compilationUnit);
        }

        File rootDirectory = new File(MacroUtil.substitute(macroRegistry, unitDescriptor.getTargetDirectory()));
        CodeHandler codeHandler = codeHandlerFactory.create(rootDirectory);
        Collection<SourcedJavaType> generatedTypeForThisUnit = codeGenerator.generate(descriptors, codeHandler, classLoader);
        classLoader = codeHandler.compile(classLoader);
        unitDescriptor.setClassLoader(classLoader);
        generatedTypes.addAll(generatedTypeForThisUnit);
        if (unitDescriptor.getPostDescriptor() != null) {
            postPlugins.add(unitDescriptor.getPostDescriptor());
        }
    }


    @Override
    public Collection<SourcedJavaType> generate(Iterable<UnitDescriptor> unitDescriptors) {
        return generate(unitDescriptors, this.getClass().getClassLoader());
    }

    @Override
    public Collection<SourcedJavaType> generate(Iterable<UnitDescriptor> unitDescriptors, ClassLoader classLoader) {
        List<SourcedJavaType> generatedTypes = new ArrayList<SourcedJavaType>();
           Set<Descriptor> postDescriptors = new HashSet<Descriptor>();
           for (UnitDescriptor descriptor : unitDescriptors) {
               generate(descriptor, generatedTypes, postDescriptors, classLoader);
           }
           for (UnitDescriptor unitDescriptor : unitDescriptors) {
               applyPostDescriptors(unitDescriptor, generatedTypes, classLoader);
           }
           return generatedTypes;
       }

    private void applyPostDescriptors(UnitDescriptor unitDescriptor, List<SourcedJavaType> sourcedJavaTypes, ClassLoader classLoader) {
        Descriptor postDescriptor = unitDescriptor.getPostDescriptor();
        if (postDescriptor == null) return;
        CodeGeneratorImpl codeGenerator = new CodeGeneratorImpl(macroRegistry);
        JavaTypeRegistry typeRegistry = codeGenerator.getRegistryProvider().get();
        for (SourcedJavaType sourcedJavaType : sourcedJavaTypes) {
            typeRegistry.register(sourcedJavaType.getType());
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

