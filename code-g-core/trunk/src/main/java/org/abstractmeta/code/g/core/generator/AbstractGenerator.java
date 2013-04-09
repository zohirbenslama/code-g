package org.abstractmeta.code.g.core.generator;

import com.google.common.base.Strings;
import org.abstractmeta.code.g.code.*;
import org.abstractmeta.code.g.config.Descriptor;
import org.abstractmeta.code.g.config.NamingConvention;
import org.abstractmeta.code.g.config.SourceMatcher;
import org.abstractmeta.code.g.config.loader.LoadedSource;
import org.abstractmeta.code.g.config.loader.SourceLoader;
import org.abstractmeta.code.g.core.code.CompiledJavaTypeImpl;
import org.abstractmeta.code.g.core.code.builder.SourcedJavaTypeBuilder;
import org.abstractmeta.code.g.core.config.SourceMatcherImpl;
import org.abstractmeta.code.g.core.config.provider.ObjectProvider;
import org.abstractmeta.code.g.extractor.FieldExtractor;
import org.abstractmeta.code.g.extractor.MethodExtractor;
import org.abstractmeta.code.g.generator.Context;
import org.abstractmeta.code.g.property.PropertyRegistry;
import org.abstractmeta.code.g.renderer.JavaTypeRenderer;
import org.abstractmeta.toolbox.compilation.compiler.JavaSourceCompiler;
import org.abstractmeta.toolbox.compilation.compiler.impl.JavaSourceCompilerImpl;

import javax.inject.Provider;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Properties;

/**
 * This AbstractGenerator provides a scaffolding building target classes from defined in descriptor sources.
 *
 * @author Adrian Witas
 */
public abstract class AbstractGenerator<T> {

    private final SourceLoader sourceLoader;
    private final PropertyRegistry propertyRegistry;
    private final Provider<JavaTypeRenderer> rendererProvider;

    public AbstractGenerator(SourceLoader sourceLoader, PropertyRegistry propertyRegistry, Provider<JavaTypeRenderer> rendererProvider) {
        this.sourceLoader = sourceLoader;
        this.propertyRegistry = propertyRegistry;
        this.rendererProvider = rendererProvider;
    }


    public Collection<CompiledJavaType> generate(Context context) {
        Descriptor descriptor = context.get(Descriptor.class);
        Properties properties = applyProperties(descriptor.getProperties());
        context.replace(getSettingClass(), getSetting(properties));
        LoadedSource loadedSource = loadSource(context);
        context.replace(LoadedSource.class, loadedSource);
        Collection<SourcedJavaType> sourceTypes = new ArrayList<SourcedJavaType>();
        for (JavaType sourceType : loadedSource.getJavaTypes()) {
            if (isApplicable(sourceType, context)) {
                Collection<SourcedJavaType> generatedSourcedTypes = generate(sourceType, context);
                sourceTypes.addAll(generatedSourcedTypes);
            }
        }
        return compileGeneratedTypes(sourceTypes, context);
    }


    protected LoadedSource loadSource(Context context) {
        Descriptor descriptor = context.get(Descriptor.class);
        SourceMatcher sourceMatcher = applyProperties(descriptor.getSourceMatcher());
        JavaTypeRegistry typeRegistry = context.get(JavaTypeRegistry.class);
        ClassLoader classLoader = context.getOptional(ClassLoader.class, AbstractGenerator.class.getClassLoader());
        return sourceLoader.load(sourceMatcher, typeRegistry, classLoader);
    }

    protected Collection<CompiledJavaType> compileGeneratedTypes(Collection<SourcedJavaType> sourceTypes, Context context) {
        Collection<CompiledJavaType> result = new ArrayList<CompiledJavaType>();
        ClassLoader classLoader = context.getOptional(ClassLoader.class, AbstractGenerator.class.getClassLoader());
        JavaSourceCompiler javaSourceCompiler = new JavaSourceCompilerImpl();
        JavaSourceCompiler.CompilationUnit compilationUnit = javaSourceCompiler.createCompilationUnit();
        for (SourcedJavaType sourcedType : sourceTypes) {
            String source = "" + sourcedType.getSourceCode();
            compilationUnit.addJavaSource(sourcedType.getType().getName(), source);
        }
        try {
            ClassLoader compilationClassLoader = javaSourceCompiler.compile(classLoader, compilationUnit);
            for (SourcedJavaType sourcedType : sourceTypes) {
                Class compiledType = null;
                try {
                    compiledType = compilationClassLoader.loadClass(sourcedType.getType().getName());
                } catch (ClassNotFoundException e) {
                    throw new IllegalStateException("Missing compiled type " + sourcedType.getType().getName() + " please check package name", e);
                }
                result.add(new CompiledJavaTypeImpl(sourcedType.getType(), sourcedType.getSourceCode(), compilationClassLoader, compiledType));
            }

        } catch (RuntimeException e) {
            StringBuilder exceptionMessageBuilder = new StringBuilder();
            exceptionMessageBuilder.append("Failed to compile: ");
            for (SourcedJavaType sourcedType : sourceTypes) {
                if (e.getMessage().contains(sourcedType.getType().getSimpleName())) {
                    exceptionMessageBuilder.append(sourcedType.getType().getSimpleName())
                            .append("\n")
                            .append(sourcedType.getSourceCode()).append("\n");

                }
            }
            throw new IllegalStateException(exceptionMessageBuilder.toString(), e);
        }
        return result;
    }


    protected SourcedJavaType renderCode(JavaTypeBuilder javaTypeBuilder) {
        JavaTypeRenderer renderer = rendererProvider.get();
        JavaTypeImporter importer = javaTypeBuilder.getImporter();
        JavaType javaType = javaTypeBuilder.build();
        importer.getGenericTypeVariables().putAll(javaType.getGenericTypeVariables());
        importer.addTypes(javaTypeBuilder.getImportTypes());
        String sourceCode = renderer.render(javaType, importer, 0);
        SourcedJavaTypeBuilder sourcedJavaTypeBuilder = new SourcedJavaTypeBuilder();
        sourcedJavaTypeBuilder.setType(javaType);
        sourcedJavaTypeBuilder.setSourceCode(sourceCode);
        return sourcedJavaTypeBuilder.build();
    }

    abstract protected Collection<SourcedJavaType> generate(JavaType sourceType, Context context);


    /**
     * Formats target class name for a given NamingConvention
     * NamingConvention is read first from context->descriptor->NamingConvention or from the getNamingConvention method
     *
     * @param context    context
     * @param sourceType source type
     * @return target class name
     */
    protected String formatTargetClassName(Context context, JavaType sourceType) {
        Descriptor descriptor = context.get(Descriptor.class);
        NamingConvention namingConvention = getNamingConvention();
        if (descriptor.getNamingConvention() != null) {
            namingConvention = descriptor.getNamingConvention();
        }
        StringBuilder result = new StringBuilder(sourceType.getPackageName()).append(".");
        if (!Strings.isNullOrEmpty(namingConvention.getPackagePostfix())) {
            result.append(namingConvention.getPackagePostfix()).append(".");
        }
        if (!Strings.isNullOrEmpty(namingConvention.getClassPrefix())) {
            result.append(namingConvention.getClassPrefix());
        }
        result.append(sourceType.getSimpleName());
        if (!Strings.isNullOrEmpty(namingConvention.getClassPostfix())) {
            result.append(namingConvention.getClassPostfix());
        }
        return result.toString();
    }

    protected T getSetting(Properties properties) {
        return new ObjectProvider<T>(getSettingClass(), properties).get();
    }


    abstract protected boolean isApplicable(JavaType javaType, Context context);


    private SourceMatcher applyProperties(SourceMatcher sourceMatcher) {
        SourceMatcherImpl result = new SourceMatcherImpl();
        result.setSourceDirectory(expandProperty(sourceMatcher.getSourceDirectory()));
        result.setIncludeSubpackages(sourceMatcher.isIncludeSubpackages());
        result.setPackageNames(sourceMatcher.getPackageNames());
        result.setClassNames(sourceMatcher.getClassNames());
        result.setDependencyPackages(sourceMatcher.getDependencyPackages());
        result.setExclusionPatterns(sourceMatcher.getExclusionPatterns());
        result.setInclusionPatterns(sourceMatcher.getInclusionPatterns());
        return result;
    }

    private Properties applyProperties(Properties properties) {
        Properties result = new Properties();
        if (properties == null) return result;
        for (String key : properties.stringPropertyNames()) {
            result.setProperty(key, expandProperty(properties.getProperty(key)));
        }
        return result;
    }


    protected String expandProperty(String text) {
        if (text == null) return null;
        for (String key : propertyRegistry.getRegistry().keySet()) {
            if (text.contains(key)) {
                text = text.replace(key, propertyRegistry.get(key));
            }
        }
        return text;
    }


    protected Collection<FieldExtractor> getFieldExtractors() {
        return Collections.emptySet();
    }

    protected Collection<MethodExtractor> getMethodExtractors() {
        return Collections.emptySet();
    }


    protected void addExtractableFields(JavaTypeBuilder builder, JavaType sourceType, Context context) {
        for (FieldExtractor extractor : getFieldExtractors()) {
            builder.addFields(extractor.extract(sourceType, context));
        }
    }


    protected void addExtractableMethods(JavaTypeBuilder builder, JavaType sourceType, Context context) {
        for (MethodExtractor extractor : getMethodExtractors()) {
            builder.addMethods(extractor.extract(sourceType, context));
        }
    }


    protected boolean isExtractable(JavaType sourceType, Context context) {
        for (FieldExtractor extractor : getFieldExtractors()) {
            if (extractor.extract(sourceType, context).size() > 0) {
                return true;
            }
        }
        for (MethodExtractor extractor : getMethodExtractors()) {
            if (extractor.extract(sourceType, context).size() > 0) {
                return true;
            }
        }
        return false;
    }


    public PropertyRegistry getPropertyRegistry() {
        return propertyRegistry;
    }

    abstract public NamingConvention getNamingConvention();


    abstract public Class<T> getSettingClass();
}
