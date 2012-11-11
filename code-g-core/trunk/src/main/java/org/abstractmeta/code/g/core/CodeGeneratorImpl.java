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
import org.abstractmeta.code.g.code.JavaType;
import org.abstractmeta.code.g.code.JavaTypeImporter;
import org.abstractmeta.code.g.code.JavaTypeRegistry;
import org.abstractmeta.code.g.config.Descriptor;
import org.abstractmeta.code.g.config.loader.JavaTypeLoader;
import org.abstractmeta.code.g.core.code.JavaTypeImporterImpl;
import org.abstractmeta.code.g.core.code.JavaTypeRegistryImpl;
import org.abstractmeta.code.g.core.config.builder.DescriptorBuilder;
import org.abstractmeta.code.g.core.config.loader.JavaTypeLoaderImpl;
import org.abstractmeta.code.g.core.renderer.TypeRenderer;
import org.abstractmeta.code.g.core.util.MacroUtil;
import org.abstractmeta.code.g.core.util.ReflectUtil;
import org.abstractmeta.code.g.handler.CodeHandler;
import org.abstractmeta.code.g.macros.MacroRegistry;
import org.abstractmeta.code.g.plugin.CodeGeneratorPlugin;
import org.abstractmeta.code.g.renderer.JavaTypeRenderer;

import javax.inject.Provider;
import java.util.*;

/**
 * Provides implementation of CodeGenerator
 */
public class CodeGeneratorImpl implements CodeGenerator {

    private final JavaTypeLoader typeLoader;
    private final MacroRegistry macroRegistry;
    private final Provider<JavaTypeRegistry> registryProvider;
    private final Provider<JavaTypeRenderer> rendererProvider;


    public CodeGeneratorImpl(MacroRegistry macroRegistry) {
        this(new JavaTypeLoaderImpl(), macroRegistry, new Provider<JavaTypeRegistry>() {
                    @Override
                    public JavaTypeRegistry get() {
                        return new JavaTypeRegistryImpl();
                    }
                }, new Provider<JavaTypeRenderer>() {
                    @Override
                    public JavaTypeRenderer get() {
                        return new TypeRenderer();
                    }
                }
        );
    }

    protected CodeGeneratorImpl(JavaTypeLoader typeLoader, MacroRegistry macroRegistry, Provider<JavaTypeRegistry> registryProvider, Provider<JavaTypeRenderer> rendererProvider) {
        this.typeLoader = typeLoader;
        this.macroRegistry = macroRegistry;
        this.registryProvider = registryProvider;
        this.rendererProvider = rendererProvider;
    }


    @Override
    public Collection<JavaType> generate(Iterable<Descriptor> descriptors, CodeHandler handler, ClassLoader classLoader) {
        Collection<JavaType> result = new ArrayList<JavaType>();
        if (classLoader == null) {
            classLoader = handler.getClass().getClassLoader();
        }
        JavaTypeRegistry registry = registryProvider.get();
        for (Descriptor itemDescriptor : descriptors) {
            Descriptor descriptor = substitute(itemDescriptor);
            JavaTypeLoader typeLoader = getTypeLoader(descriptor);
            Collection<String> loadedTypes = typeLoader.load(registry, descriptor, classLoader);
            Collection<JavaType> generatedTypesByThisDescriptor = runPlugin(registry, loadedTypes, classLoader, descriptor, handler);
            result.addAll(generatedTypesByThisDescriptor);
        }
        return result;
    }

    protected JavaTypeLoader getTypeLoader(Descriptor descriptor) {
        if (descriptor.getTypeLoaderClassName() == null
                || descriptor.getTypeLoaderClassName().isEmpty()
                || JavaTypeLoaderImpl.class.getName().equals(descriptor.getTypeLoaderClassName())) {

            return typeLoader;
        }
        try {
            return (JavaTypeLoader) Class.forName(descriptor.getTypeLoaderClassName()).newInstance();
        } catch (Exception e) {
            throw new IllegalStateException("Failed to create instance of " + descriptor.getTypeLoaderClassName(), e);
        }
    }

    public Collection<JavaType> runPlugin(JavaTypeRegistry registry, Collection<String> typeCandidates, ClassLoader classLoader, Descriptor descriptor, CodeHandler handler) {
        if(classLoader == null) {
            throw new IllegalArgumentException("classLoader was null");
        }
        if(descriptor.getPlugin() == null) {
            throw new IllegalArgumentException("descriptor.plugin was null");
        }

        CodeGeneratorPlugin plugin = ReflectUtil.loadInstance(CodeGeneratorPlugin.class, descriptor.getPlugin(), classLoader);
        List<String> generated = plugin.generate(typeCandidates, registry, descriptor);
        return renderCode(handler, generated, registry);
    }


    public Descriptor substitute(Descriptor descriptor) {
        DescriptorBuilder descriptorBuilder = new DescriptorBuilder();
        descriptorBuilder.merge(descriptor);
        descriptorBuilder.setSourcePackage(MacroUtil.substitute(macroRegistry, descriptor.getSourcePackage()));
        descriptorBuilder.setSourceClass(MacroUtil.substitute(macroRegistry, descriptor.getSourceClass()));
        descriptorBuilder.setTargetPackage(MacroUtil.substitute(macroRegistry, descriptor.getTargetPackage()));
        descriptorBuilder.setTargetPrefix(MacroUtil.substitute(macroRegistry, descriptor.getTargetPrefix()));
        descriptorBuilder.setTargetPostfix(MacroUtil.substitute(macroRegistry, descriptor.getTargetPostfix()));
        descriptorBuilder.setSuperType(MacroUtil.substitute(macroRegistry, descriptor.getSuperType()));
        descriptorBuilder.setInterfaces(MacroUtil.substitute(macroRegistry, descriptor.getInterfaces()));
        descriptorBuilder.setCompilationSources(MacroUtil.substitute(macroRegistry, descriptor.getCompilationSources(), new ArrayList<String>()));
        descriptorBuilder.setOptions(MacroUtil.substitute(macroRegistry, descriptor.getOptions()));
        descriptorBuilder.setPlugin(descriptor.getPlugin());
        return descriptorBuilder.build();
    }

    @Override
    public Collection<JavaType> generate(Iterable<Descriptor> descriptors, CodeHandler handler) {
        return generate(descriptors, handler, null);
    }

    protected Collection<JavaType> renderCode(CodeHandler codeHandler, Iterable<String> generatedTypes, JavaTypeRegistry registry) {
        Collection<JavaType> result = new ArrayList<JavaType>();
        for (String generatedType : generatedTypes) {
            JavaTypeRenderer renderer = rendererProvider.get();
            JavaType javaType = registry.get(generatedType);
            JavaTypeImporter importer = getJavaTypeImporter(javaType.getPackageName());
            importer.getGenericTypeVariables().putAll(javaType.getGenericTypeVariables());
            importer.addTypes(javaType.getImportTypes());
            String sourceCode = renderer.render(javaType, importer, 0);
            codeHandler.handle(javaType, sourceCode);
            result.add(javaType);
        }
        return result;
    }

    protected JavaTypeImporter getJavaTypeImporter(String packageName) {
        return new JavaTypeImporterImpl(packageName);
    }


    public Provider<JavaTypeRegistry> getRegistryProvider() {
        return registryProvider;
    }
}