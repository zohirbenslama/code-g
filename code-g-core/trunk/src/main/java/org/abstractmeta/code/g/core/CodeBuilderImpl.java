package org.abstractmeta.code.g.core;


import org.abstractmeta.code.g.CodeBuilder;
import org.abstractmeta.code.g.CodeStorageHandler;
import org.abstractmeta.code.g.code.JavaType;
import org.abstractmeta.code.g.code.JavaTypeImporter;
import org.abstractmeta.code.g.code.JavaTypeRegistry;
import org.abstractmeta.code.g.core.code.JavaTypeImporterImpl;
import org.abstractmeta.code.g.core.code.JavaTypeRegistryImpl;
import org.abstractmeta.code.g.core.provider.ClassTypeProvider;
import org.abstractmeta.code.g.core.renderer.TypeRenderer;
import org.abstractmeta.code.g.core.util.LoaderUtil;
import org.abstractmeta.code.g.config.Descriptor;
import org.abstractmeta.code.g.plugin.CodeGeneratorPlugin;
import org.abstractmeta.code.g.renderer.JavaTypeRenderer;

import java.util.*;


public class CodeBuilderImpl implements CodeBuilder {

    private final Collection<Descriptor> descriptors;

    public CodeBuilderImpl() {
        this(new ArrayList<Descriptor>());
    }

    protected CodeBuilderImpl(Collection<Descriptor> descriptors) {
        this.descriptors = descriptors;
    }


    public Collection<Descriptor> getDescriptors() {
        return descriptors;
    }


    public CodeBuilder addDescriptors(Collection<Descriptor> descriptors) {
        this.descriptors.addAll(descriptors);
        return this;
    }


    public CodeBuilder addDescriptor(Descriptor implementationDescriptor) {
        descriptors.add(implementationDescriptor);
        return this;
    }


    public void build(CodeStorageHandler codeHandler) {
        build(codeHandler, getClass().getClassLoader());
    }

    public void build(CodeStorageHandler codeHandler, ClassLoader classLoader) {
        if (descriptors.size() == 0) {
            throw new IllegalStateException("No descriptor found");
        }
        JavaTypeRegistry javaTypeRegistry = new JavaTypeRegistryImpl();
        loadClasses(javaTypeRegistry, classLoader);
        List<String> generatedTypes = new ArrayList<String>();
        for (Descriptor descriptor : descriptors) {
            build(generatedTypes, codeHandler, descriptor, javaTypeRegistry);
        }
    }


    protected void loadClasses(JavaTypeRegistry registry, ClassLoader classLoader) {
        for (Descriptor descriptor : descriptors) {
            List<Class> classes = LoaderUtil.getClasses(descriptor.getSourcePackage(), classLoader);
            if (classes == null || classes.size() == 0) {
                throw new IllegalStateException("Failed to load classes for " + descriptor.getSourcePackage());
            }
            for (Class clazz : classes) {
                registry.register(new ClassTypeProvider(clazz).get());
            }
        }
    }

    public void build(List<String> generatedTypes, CodeStorageHandler codeHandler, Descriptor descriptor, JavaTypeRegistry registry) {
        CodeGeneratorPlugin plugin = loadPlugin(descriptor.getPlugin());
        Set<String> inclusions = getInclusions(descriptor, generatedTypes);
        List<String> matchedSourceTypes = matchTypeNames(registry, descriptor.getSourcePackage(), descriptor.getExclusions(), inclusions);
        List<String> pluginResult = plugin.generate(matchedSourceTypes, registry, descriptor);
        if (pluginResult != null && pluginResult.size() > 0) {
            generatedTypes.addAll(pluginResult);
        }
        renderCode(codeHandler, generatedTypes, registry);
    }

    protected Set<String> getInclusions(Descriptor descriptor, List<String> generatedTypes) {
        Set<String> result = new HashSet<String>();
        if (descriptor.getInclusions() != null) {
            result.addAll(descriptor.getInclusions());
        }
        String sourceClass = descriptor.getSourceClass();
        if (sourceClass != null && !sourceClass.isEmpty()) {
            String simpleClassName = LoaderUtil.extractSimpleClassName(sourceClass);
            result.add(simpleClassName);
        }
        if (result.size() > 0) {
            result.addAll(generatedTypes);
        }
        return result;
    }

    protected CodeGeneratorPlugin loadPlugin(String pluginName) {
        try {
            return CodeGeneratorPlugin.class.cast(Class.forName(pluginName).newInstance());
        } catch (Exception e) {
            throw new IllegalStateException("Failed to load plugin " + pluginName, e);
        }
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