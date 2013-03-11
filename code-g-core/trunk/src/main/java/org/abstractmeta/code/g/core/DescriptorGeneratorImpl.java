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
 * Provides implementation of DescriptorGenerator
 */
public class DescriptorGeneratorImpl  {


    /**
    private final SourceLoader typeLoader;
    private final PropertyRegistry macroRegistry;
    private final Provider<JavaTypeRegistry> registryProvider;
    private final Provider<JavaTypeRenderer> rendererProvider;
    private final Map<String, Descriptor> templates;


    public DescriptorGeneratorImpl(PropertyRegistry macroRegistry) {
        this(macroRegistry, new DefaultTemplateProvider().get());
    }

    public DescriptorGeneratorImpl(PropertyRegistry macroRegistry, Map<String, Descriptor> templates) {
        this(new SourceLoaderImpl(), macroRegistry, new JavaTypeRegistryProvider(), new JavaTypeRendererProvider(), templates);
    }

    @Inject
    public DescriptorGeneratorImpl(SourceLoader typeLoader, PropertyRegistry macroRegistry, Provider<JavaTypeRegistry> registryProvider, Provider<JavaTypeRenderer> rendererProvider, Map<String, Descriptor> templates) {
        this.typeLoader = typeLoader;
        this.macroRegistry = macroRegistry;
        this.registryProvider = registryProvider;
        this.rendererProvider = rendererProvider;
        this.templates = templates;
    }


    public Collection<SourcedJavaType> generate(Iterable<Descriptor> descriptors, CodeHandler handler, @Nullable ClassLoader classLoader) {
        Collection<SourcedJavaType> result = new ArrayList<SourcedJavaType>();
        if (classLoader == null) {
            classLoader = handler.getClass().getClassLoader();
        }
        JavaTypeRegistry registry = registryProvider.get();
        for (Descriptor itemDescriptor : descriptors) {
            if(itemDescriptor.getCodeGenerator() == null) {
                throw new IllegalStateException("Plugin was null");
            }
            Descriptor descriptor = buildDescriptor(itemDescriptor, classLoader);
            Collection<String> loadedTypes = Collections.emptyList();
            if (!CollectionUtil.isEmpty(descriptor.getSourceClasses()) || !CollectionUtil.isEmpty(descriptor.getSourcePackages())) {
                SourceLoader typeLoader = getTypeLoader(descriptor);
                loadedTypes = typeLoader.load(registry, descriptor, classLoader);
            }
            Collection<SourcedJavaType> generatedTypesByThisDescriptor = applyPlugin(registry, loadedTypes, classLoader, descriptor, handler);
            result.addAll(generatedTypesByThisDescriptor);
        }
        return result;
    }

    protected SourceLoader getTypeLoader(Descriptor descriptor) {
        if (descriptor.getTypeLoaderClassName() == null
                || descriptor.getTypeLoaderClassName().isEmpty()
                || SourceLoaderImpl.class.getName().equals(descriptor.getTypeLoaderClassName())) {

            return typeLoader;
        }
        try {
            return (SourceLoader) Class.forName(descriptor.getTypeLoaderClassName()).newInstance();
        } catch (Exception e) {
            throw new IllegalStateException("Failed to createCommandInnerClass instance of " + descriptor.getTypeLoaderClassName(), e);
        }
    }

    public Collection<SourcedJavaType> applyPlugin(JavaTypeRegistry registry, Collection<String> typeCandidates, ClassLoader classLoader, Descriptor descriptor, CodeHandler handler) {
        if (classLoader == null) {
            throw new IllegalArgumentException("classLoader was null");
        }
        if (descriptor.getCodeGenerator() == null) {
            throw new IllegalArgumentException("descriptor.plugin was null");
        }

        CodeGeneratorPlugin plugin = ReflectUtil.newInstance(CodeGeneratorPlugin.class, descriptor.getCodeGenerator(), classLoader);
        List<String> generated = plugin.generate(typeCandidates, registry, descriptor);
        return renderCode(handler, generated, registry);
    }


    @SuppressWarnings("unchecked")
    public Descriptor buildDescriptor(Descriptor descriptor, ClassLoader classLoader) {
        CodeGeneratorPlugin plugin = ReflectUtil.newInstance(CodeGeneratorPlugin.class, descriptor.getCodeGenerator(), classLoader);
        Map<String, Descriptor> pluginTemplates = plugin.getTemplates();
        if (pluginTemplates == null) pluginTemplates = Collections.emptyMap();
        DescriptorBuilder descriptorBuilder = new DescriptorBuilder();
        if (!CollectionUtil.isEmpty(descriptor.getTemplateNames())) {
            for (String templateName : descriptor.getTemplateNames()) {
                if (templates.containsPath(templateName)) {
                    descriptorBuilder.merge(templates.get(templateName));
                } else if (pluginTemplates.containsPath(templateName)) {
                    descriptorBuilder.merge(pluginTemplates.get(templateName));
                } else if (pluginTemplates.containsPath(descriptor.getCodeGenerator() + "." + templateName)) {
                    descriptorBuilder.merge(pluginTemplates.get(descriptor.getCodeGenerator() + "." + templateName));
                }
            }
        } else if (pluginTemplates.containsPath(descriptor.getCodeGenerator())) {
            //applies default plugin for this plugin
            descriptorBuilder.merge(pluginTemplates.get(descriptor.getCodeGenerator()));
        }

        descriptorBuilder.merge(descriptor);
        descriptorBuilder.setSourcePackages(MacroUtil.substitute(macroRegistry, descriptorBuilder.getSourcePackages(), new ArrayList<String>()));
        descriptorBuilder.setSourceClasses(MacroUtil.substitute(macroRegistry, descriptorBuilder.getSourceClasses(), new ArrayList<String>()));
        descriptorBuilder.setTargetPackage(MacroUtil.substitute(macroRegistry, descriptorBuilder.getTargetPackage()));
        descriptorBuilder.setTargetPrefix(MacroUtil.substitute(macroRegistry, descriptorBuilder.getTargetPrefix()));
        descriptorBuilder.setTargetPostfix(MacroUtil.substitute(macroRegistry, descriptorBuilder.getTargetPostfix()));
        descriptorBuilder.setSuperType(MacroUtil.substitute(macroRegistry, descriptorBuilder.getSuperType()));
        descriptorBuilder.addInterfaces(MacroUtil.substitute(macroRegistry, descriptorBuilder.getInterfaces(), new ArrayList<String>()));
        descriptorBuilder.setCompilationSources(MacroUtil.substitute(macroRegistry, descriptorBuilder.getCompilationSources(), new ArrayList<String>()));
        descriptorBuilder.setOptions(MacroUtil.substitute(macroRegistry, descriptorBuilder.getOptions()));
        return descriptorBuilder.build();
    }

    public Collection<SourcedJavaType> generate(Iterable<Descriptor> descriptors, CodeHandler handler) {
        return generate(descriptors, handler, null);
    }

    protected Collection<SourcedJavaType> renderCode(CodeHandler codeHandler, Iterable<String> generatedTypes, JavaTypeRegistry registry) {
        Collection<SourcedJavaType> result = new ArrayList<SourcedJavaType>();
        for (String generatedType : generatedTypes) {
            JavaTypeRenderer renderer = rendererProvider.get();
            JavaType javaType = registry.get(generatedType);
            JavaTypeImporter importer = getJavaTypeImporter(javaType.getPackageName());
            importer.getGenericTypeVariables().putAll(javaType.getGenericTypeVariables());
            importer.addTypes(javaType.getImportTypes());
            String sourceCode = renderer.render(javaType, importer, 0);
            SourcedJavaTypeBuilder sourcedJavaTypeBuilder = new SourcedJavaTypeBuilder();
            sourcedJavaTypeBuilder.setType(javaType);
            sourcedJavaTypeBuilder.setSourceCode(sourceCode);
            sourcedJavaTypeBuilder.setFile(JavaTypeUtil.getFileName(javaType, codeHandler.getRootDirectory()));
            SourcedJavaType sourcedJavaType = sourcedJavaTypeBuilder.build();
            codeHandler.handle(sourcedJavaType);
            result.add(sourcedJavaType);
        }
        return result;
    }

    protected JavaTypeImporter getJavaTypeImporter(String packageName) {
        return new JavaTypeImporterImpl(packageName);
    }


    public Provider<JavaTypeRegistry> getRegistryProvider() {
        return registryProvider;
    }

    **/
}