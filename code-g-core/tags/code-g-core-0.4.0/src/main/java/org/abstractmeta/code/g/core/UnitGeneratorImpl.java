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
import org.abstractmeta.code.g.config.Descriptor;
import org.abstractmeta.code.g.config.UnitDescriptor;
import org.abstractmeta.code.g.config.loader.JavaSourceLoader;
import org.abstractmeta.code.g.core.config.builder.DescriptorBuilder;
import org.abstractmeta.code.g.core.config.loader.JavaSourceLoaderImpl;
import org.abstractmeta.code.g.core.handler.PersistenceCodeHandler;
import org.abstractmeta.toolbox.compilation.compiler.JavaSourceCompiler;
import org.abstractmeta.toolbox.compilation.compiler.impl.JavaSourceCompilerImpl;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Default UnitGenerator implementation.
 *
 * @author Adrian Witas
 */
public class UnitGeneratorImpl implements UnitGenerator {

    private final JavaSourceLoader sourceLoader;

    public UnitGeneratorImpl() {
        this(new JavaSourceLoaderImpl());
    }

    protected UnitGeneratorImpl(JavaSourceLoader sourceLoader) {
        this.sourceLoader = sourceLoader;
    }

    @Override
    public Collection<File> generate(UnitDescriptor unitDescriptor) {
        JavaSourceCompiler javaSourceCompiler = new JavaSourceCompilerImpl();
        JavaSourceCompiler.CompilationUnit compilationUnit = javaSourceCompiler.createCompilationUnit();
        compilationUnit.addClassPathEntries(unitDescriptor.getClassPathEntries());
        File sourceDirectory = new File(unitDescriptor.getSourceDirectory());
        Collection<Descriptor> descriptors = getDescriptor(unitDescriptor);
        Map<String, String> javaSources = sourceLoader.load(sourceDirectory, descriptors);
        for (String className : javaSources.keySet()) {
            compilationUnit.addJavaSource(className, javaSources.get(className));
        }
        ClassLoader classLoader = javaSourceCompiler.compile(compilationUnit);
        PersistenceCodeHandler codeHandler = new PersistenceCodeHandler(new File(unitDescriptor.getTargetDirectory()));
        CodeGenerator codeGenerator = new CodeGeneratorImpl();
        codeGenerator.generate(descriptors, codeHandler, classLoader);
        return codeHandler.getGeneratedFiles();
    }

    protected List<Descriptor> getDescriptor(UnitDescriptor unitDescriptor) {
        List<Descriptor> result = new ArrayList<Descriptor>();
        String sourcePackage = unitDescriptor.getSourcePackage();
        for (Descriptor descriptor : unitDescriptor.getDescriptors()) {
            if (descriptor.getSourcePackage() == null && descriptor.getSourceClass() == null) {
                result.add(new DescriptorBuilder().merge(descriptor).setSourcePackage(sourcePackage).build());

            } else {
                result.add(new DescriptorBuilder().merge(descriptor).build());
            }

        }
        return result;
    }
}

