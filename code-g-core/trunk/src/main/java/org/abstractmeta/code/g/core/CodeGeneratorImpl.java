package org.abstractmeta.code.g.core;


import org.abstractmeta.code.g.CodeGenerator;
import org.abstractmeta.code.g.code.JavaType;
import org.abstractmeta.code.g.code.JavaTypeImporter;
import org.abstractmeta.code.g.code.JavaTypeRegistry;
import org.abstractmeta.code.g.config.Descriptor;
import org.abstractmeta.code.g.config.loader.JavaTypeLoader;
import org.abstractmeta.code.g.core.code.JavaTypeImporterImpl;
import org.abstractmeta.code.g.core.code.JavaTypeRegistryImpl;
import org.abstractmeta.code.g.core.config.loader.JavaTypeLoaderImpl;
import org.abstractmeta.code.g.core.plugin.BuilderGeneratorPlugin;
import org.abstractmeta.code.g.core.renderer.TypeRenderer;
import org.abstractmeta.code.g.handler.CodeHandler;
import org.abstractmeta.code.g.plugin.CodeGeneratorPlugin;
import org.abstractmeta.code.g.renderer.JavaTypeRenderer;

import javax.inject.Provider;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Provides implementation of CodeGenerator
 */
public class CodeGeneratorImpl implements CodeGenerator {

    private final List<String> pluginPackages;
    private final JavaTypeLoader typeLoader;
    private final Provider<JavaTypeRegistry> registryProvider;
    private final Provider<JavaTypeRenderer> rendererProvider;

    public CodeGeneratorImpl() {
        this(new JavaTypeLoaderImpl(), new Provider<JavaTypeRegistry>() {
                    @Override
                    public JavaTypeRegistry get() {
                        return new JavaTypeRegistryImpl();
                    }
                }, new Provider<JavaTypeRenderer>() {
                    @Override
                    public JavaTypeRenderer get() {
                        return new TypeRenderer();
                    }
                },
                new ArrayList<String>(Arrays.asList(BuilderGeneratorPlugin.class.getPackage().getName()))
        );
    }

    protected CodeGeneratorImpl(JavaTypeLoader typeLoader, Provider<JavaTypeRegistry> registryProvider, Provider<JavaTypeRenderer> rendererProvider, List<String> pluginNamespaces) {
        this.pluginPackages = pluginNamespaces;
        this.typeLoader = typeLoader;
        this.registryProvider = registryProvider;
        this.rendererProvider = rendererProvider;
    }


    @Override
    public void generate(Iterable<Descriptor> descriptors, CodeHandler handler, ClassLoader classLoader) {
        if (classLoader == null) {
            classLoader = handler.getClass().getClassLoader();
        }
        JavaTypeRegistry registry = registryProvider.get();
        for (Descriptor descriptor : descriptors) {
            Collection<String> loadedTypes = typeLoader.load(registry, descriptor, classLoader);
            CodeGeneratorPlugin plugin = loadPlugin(descriptor.getPlugin(), classLoader);
            List<String> generated = plugin.generate(loadedTypes, registry, descriptor);
            renderCode(handler, generated, registry);
        }
    }

    @Override
    public void generate(Iterable<Descriptor> descriptors, CodeHandler handler) {
        generate(descriptors, handler, null);
    }

    protected void renderCode(CodeHandler codeHandler, Iterable<String> generatedTypes, JavaTypeRegistry registry) {
        for (String generatedType : generatedTypes) {
            JavaTypeRenderer renderer = rendererProvider.get();
            JavaType javaType = registry.get(generatedType);
            JavaTypeImporter importer = getJavaTypeImporter(javaType.getPackageName());
            importer.getGenericTypeVariables().putAll(javaType.getGenericTypeVariables());
            importer.addTypes(javaType.getImportTypes());
            String sourceCode = renderer.render(javaType, importer, 0);
            codeHandler.handle(javaType, sourceCode);
        }
    }

    protected JavaTypeImporter getJavaTypeImporter(String packageName) {
        return new JavaTypeImporterImpl(packageName);
    }

    protected CodeGeneratorPlugin loadPlugin(String pluginName, ClassLoader classLoader) {
        Class pluginClass = null;
        try {
            pluginClass = loadPluginClass(pluginName, classLoader);

            return CodeGeneratorPlugin.class.cast(pluginClass.newInstance());
        } catch (ClassCastException e) {
            throw new ClassCastException("Failed to case " + pluginClass + " " + CodeGeneratorPlugin.class.getName());
        } catch (Exception e) {
            throw new IllegalStateException("Failed to load plugin " + pluginName, e);
        }
    }

    protected Class loadPluginClass(String className, ClassLoader classLoader) throws ClassNotFoundException {
        if (className.indexOf('.') != -1) {
            return classLoader.loadClass(className);
        }
        for (String pluginPackage : pluginPackages) {
            try {
                return classLoader.loadClass(pluginPackage + "." + className);
            } catch (ClassNotFoundException e) {
                // ignore
            }
        }
        throw new IllegalStateException("Invalid class name " + className);
    }
}