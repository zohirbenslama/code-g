package org.abstractmeta.code.g.core;


import org.abstractmeta.code.g.CodeBuilder;
import org.abstractmeta.code.g.CodeStorageHandler;
import org.abstractmeta.code.g.code.JavaType;
import org.abstractmeta.code.g.code.JavaTypeImporter;
import org.abstractmeta.code.g.code.JavaTypeRegistry;
import org.abstractmeta.code.g.core.code.JavaTypeImporterImpl;
import org.abstractmeta.code.g.core.code.JavaTypeRegistryImpl;
import org.abstractmeta.code.g.core.plugin.ClassGeneratorPlugin;
import org.abstractmeta.code.g.core.plugin.AbstractGeneratorPlugin;
import org.abstractmeta.code.g.core.plugin.CodeGeneratorPluginLoaderImpl;
import org.abstractmeta.code.g.core.provider.ClassTypeProvider;
import org.abstractmeta.code.g.core.renderer.TypeRenderer;
import org.abstractmeta.code.g.core.util.LoaderUtil;
import org.abstractmeta.code.g.config.Descriptor;
import org.abstractmeta.code.g.plugin.CodeGeneratorPlugin;
import org.abstractmeta.code.g.plugin.CodeGeneratorPluginLoader;
import org.abstractmeta.code.g.renderer.JavaTypeRenderer;

import java.util.*;


public class CodeBuilderImpl implements CodeBuilder {

    private final Collection<Descriptor> descriptors;
    private final CodeGeneratorPluginLoader pluginLoader;


    public CodeBuilderImpl() {
        this(new ArrayList<Descriptor>(), new CodeGeneratorPluginLoaderImpl());
    }

    protected CodeBuilderImpl(Collection<Descriptor> descriptors, CodeGeneratorPluginLoader pluginLoader) {
        this.descriptors = descriptors;
        this.pluginLoader = pluginLoader;
        this.pluginLoader.addPluginPackageName(ClassGeneratorPlugin.class.getPackage().getName());
    }

    @Override
    public CodeGeneratorPluginLoader getPluginLoader() {
        return pluginLoader;
    }

    @Override
    public Collection<Descriptor> getDescriptors() {
        return descriptors;
    }

    @Override
    public CodeBuilder addDescriptors(Collection<Descriptor> descriptors) {
        this.descriptors.addAll(descriptors);
        return this;
    }

    @Override
    public CodeBuilder addDescriptor(Descriptor implementationDescriptor) {
        descriptors.add(implementationDescriptor);
        return this;
    }

    @Override
    public void build(CodeStorageHandler codeHandler) {
        build(codeHandler, null);
    }

    @Override
    public void build(CodeStorageHandler codeHandler, ClassLoader classLoader) {
        JavaTypeRegistry javaTypeRegistry = new JavaTypeRegistryImpl();
        Map<String, CodeGeneratorPlugin> plugins = pluginLoader.loadPlugins(descriptors, classLoader);
        loadClasses(javaTypeRegistry, classLoader);
        for (Descriptor descriptor : descriptors) {
            build(codeHandler, descriptor, javaTypeRegistry, plugins);
        }
    }

    protected void loadClasses(JavaTypeRegistry registry, ClassLoader classLoader) {
        for (Descriptor descriptor : descriptors) {
            List<Class> classes = LoaderUtil.getClasses(descriptor.getSource(), classLoader);
            for (Class clazz : classes) {
                registry.register(new ClassTypeProvider(clazz).get());
            }
        }
    }

    protected void build(CodeStorageHandler codeHandler, Descriptor descriptor, JavaTypeRegistry registry, Map<String, CodeGeneratorPlugin> plugins) {
        List<String> generatedTypes = new ArrayList<String>();
        for (String pluginName : descriptor.getPlugins()) {
            CodeGeneratorPlugin plugin = plugins.get(pluginName);
            String source = plugin.getRequiredOption(descriptor, AbstractGeneratorPlugin.SOURCE_KEY);
            List<String> matchedSourceTypes = matchTypeNames(registry, source, descriptor.getExclusions(), descriptor.getInclusions());
            List<String> pluginResult = plugin.generate(matchedSourceTypes, registry, descriptor);
            if (pluginResult != null && pluginResult.size() > 0) {
                generatedTypes.addAll(pluginResult);
            }
        }
        renderCode(codeHandler, generatedTypes, registry);
    }

    protected void renderCode(CodeStorageHandler codeHandler, List<String> generatedTypes, JavaTypeRegistry registry) {
        for (String generatedType : generatedTypes) {
            JavaTypeRenderer renderer = new TypeRenderer();
            JavaType javaType = registry.get(generatedType);
            JavaTypeImporter importer = new JavaTypeImporterImpl(javaType.getPackageName());
            importer.addTypes(javaType.getImportTypes());
            String sourceCode = renderer.render(javaType, importer, 0);
            codeHandler.handle(javaType, sourceCode);
        }
    }


    protected List<String> matchTypeNames(JavaTypeRegistry registry, String source, Set<String> exclusion, Set<String> inclusions) {
        List<String> result = new ArrayList<String>();
        boolean wildCard = source.endsWith(".*");
        source = source.replace(".*", "");
        for (JavaType type : registry.get()) {
            String typeName = type.getName();
            if (exclusion != null && exclusion.size() > 0) {
                if (exclusion.contains(type.getSimpleName())) {
                    continue;
                }
            }
            if (inclusions != null && inclusions.size() > 0) {
                if (!inclusions.contains(type.getSimpleName())) {
                    continue;
                }
            }

            String packageName = type.getPackageName();
            if (type.getName().equals(source)) {
                result.add(source);

            } else if (wildCard) {
                if (packageName.startsWith(source)) {
                    result.add(typeName);
                }
            } else {
                if (packageName.equals(source)) {
                    result.add(typeName);
                }
            }
        }
        return result;
    }


}