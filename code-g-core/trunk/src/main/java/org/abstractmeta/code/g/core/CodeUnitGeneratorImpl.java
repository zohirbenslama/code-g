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

/**
 * Default CodeUnitGenerator implementation.
 *
 * @author Adrian Witas
 */
public class CodeUnitGeneratorImpl {

    /**
    private final JavaSourceLoader sourceLoader;
    private final PropertyRegistry macroRegistry;
    private final CodeHandlerFactory codeHandlerFactory;


    public CodeUnitGeneratorImpl(PropertyRegistry macroRegistry, CodeHandlerFactory codeHandlerFactory) {
        this(new JavaSourceLoaderImpl(), macroRegistry, codeHandlerFactory);
    }

    protected CodeUnitGeneratorImpl(JavaSourceLoader sourceLoader, PropertyRegistry macroRegistry, CodeHandlerFactory codeHandlerFactory) {
        this.sourceLoader = sourceLoader;
        this.macroRegistry = macroRegistry;
        this.codeHandlerFactory = codeHandlerFactory;
    }


    public ClassLoader generate(UnitDescriptor unitDescriptor, List<SourcedJavaType> generatedTypes, Set<Descriptor> postPlugins, ClassLoader classLoader) {
        DescriptorGenerator codeGenerator = new DescriptorGeneratorImpl(macroRegistry);
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
        return classLoader;
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
            classLoader = generate(descriptor, generatedTypes, postDescriptors, classLoader);
        }
        for (UnitDescriptor unitDescriptor : unitDescriptors) {
            classLoader = applyPostDescriptors(unitDescriptor, generatedTypes, classLoader);
            unitDescriptor.setClassLoader(classLoader);
        }
        return generatedTypes;
    }

    private ClassLoader applyPostDescriptors(UnitDescriptor unitDescriptor, List<SourcedJavaType> sourcedJavaTypes, ClassLoader classLoader) {
        Descriptor postDescriptor = unitDescriptor.getPostDescriptor();
        if (postDescriptor == null) return classLoader;
        DescriptorGeneratorImpl codeGenerator = new DescriptorGeneratorImpl(macroRegistry);
        JavaTypeRegistry typeRegistry = codeGenerator.getRegistryProvider().get();
        for (SourcedJavaType sourcedJavaType : sourcedJavaTypes) {
            typeRegistry.register(sourcedJavaType.getType());
        }
        CodeHandler codeHandler = codeHandlerFactory.create(new File(unitDescriptor.getTargetDirectory()));
        Descriptor descriptor = codeGenerator.buildDescriptor(postDescriptor, classLoader);
        codeGenerator.applyPlugin(typeRegistry, Collections.<String>emptyList(), this.getClass().getClassLoader(), descriptor, codeHandler);
        return codeHandler.compile(classLoader);
    }


    @SuppressWarnings("unchecked")
    protected List<Descriptor> getDescriptor(UnitDescriptor unitDescriptor) {
        List<Descriptor> result = new ArrayList<Descriptor>();
        Collection<String> sourcePackage = MacroUtil.substitute(macroRegistry, unitDescriptor.getSourcePackages() ,new ArrayList<String>());
        for (Descriptor descriptor : unitDescriptor.getDescriptors()) {
            DescriptorBuilder descriptorBuilder = new DescriptorBuilder();
            descriptorBuilder.merge(descriptor);
            if (descriptor.getSourcePackages() == null && descriptor.getSourcePackages().isEmpty() && descriptor.getSourceClasses() == null &&descriptor.getSourceClasses().isEmpty())  {
                descriptorBuilder.setSourcePackages(sourcePackage);
            }
            result.add(descriptorBuilder.build());
        }
        return result;
    }


    **/
}

