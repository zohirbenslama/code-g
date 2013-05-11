package org.abstractmeta.code.g.core.generator;

import com.google.common.base.Strings;
import org.abstractmeta.code.g.code.*;
import org.abstractmeta.code.g.config.Descriptor;
import org.abstractmeta.code.g.config.NamingConvention;
import org.abstractmeta.code.g.config.SourceMatcher;
import org.abstractmeta.code.g.config.UnitDescriptor;
import org.abstractmeta.code.g.config.loader.LoadedSource;
import org.abstractmeta.code.g.config.loader.SourceLoader;
import org.abstractmeta.code.g.core.code.CompiledJavaTypeImpl;
import org.abstractmeta.code.g.core.code.JavaTypeRegistryImpl;
import org.abstractmeta.code.g.core.config.SourceMatcherImpl;
import org.abstractmeta.code.g.core.config.provider.ObjectProvider;
import org.abstractmeta.code.g.core.util.CodeGeneratorUtil;
import org.abstractmeta.code.g.extractor.FieldExtractor;
import org.abstractmeta.code.g.extractor.MethodExtractor;
import org.abstractmeta.code.g.generator.Context;
import org.abstractmeta.code.g.property.PropertyRegistry;
import org.abstractmeta.code.g.renderer.JavaTypeRenderer;
import org.abstractmeta.toolbox.compilation.compiler.JavaSourceCompiler;
import org.abstractmeta.toolbox.compilation.compiler.impl.JavaSourceCompilerImpl;
import org.abstractmeta.toolbox.compilation.compiler.util.ClassPathUtil;

import javax.inject.Provider;
import java.io.File;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This AbstractGenerator provides a scaffolding building target classes from defined in descriptor sources.
 *
 * @author Adrian Witas
 */
public abstract class AbstractGenerator<T> {

    protected final Logger logger = Logger.getLogger(AbstractGenerator.class.getName());
    protected final SourceLoader sourceLoader;
    protected final PropertyRegistry propertyRegistry;
    protected final Provider<JavaTypeRenderer> rendererProvider;

    public AbstractGenerator(SourceLoader sourceLoader, PropertyRegistry propertyRegistry, Provider<JavaTypeRenderer> rendererProvider) {
        this.sourceLoader = sourceLoader;
        this.propertyRegistry = propertyRegistry;
        this.rendererProvider = rendererProvider;
    }


    public List<CompiledJavaType> generate(Context context) {
        Descriptor descriptor = context.get(Descriptor.class);
        Properties properties = applyProperties(descriptor.getProperties());
        context.replace(getSettingClass(), getSetting(properties));

        LoadedSource loadedSource = loadSource(context);
        //persistLoadedSource(loadedSource);
        context.replace(LoadedSource.class, loadedSource);
        if(loadedSource.getClassLoader() != null) {
            context.replace(ClassLoader.class, loadedSource.getClassLoader());
        }
        List<SourcedJavaType> sourceTypes = new ArrayList<SourcedJavaType>();
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
        SourceMatcher sourceMatcher = applyProperties(descriptor.getSourceMatcher(), context);
        JavaTypeRegistry typeRegistry = getJavaTypeRegistry(context);
        ClassLoader classLoader = context.getOptional(ClassLoader.class, AbstractGenerator.class.getClassLoader());
        File targetCompilationDirectory = getCompilationDirectory(context);
        return sourceLoader.load(sourceMatcher, typeRegistry, classLoader, targetCompilationDirectory);
    }

    protected File getCompilationDirectory(Context context) {
        if(context.contains(UnitDescriptor.class)) {
            UnitDescriptor unitDescriptor  = context.get(UnitDescriptor.class);
            if(unitDescriptor.getTargetCompilationDirectory() != null) {
                return new File(unitDescriptor.getTargetCompilationDirectory());
            }
        }
        return null;
    }

    protected JavaTypeRegistry getJavaTypeRegistry(Context context) {
        if (context.contains(JavaTypeRegistry.class)) {
            return context.get(JavaTypeRegistry.class);
        }
        JavaTypeRegistry registry = new JavaTypeRegistryImpl();
        context.put(JavaTypeRegistry.class, registry);
        return registry;

    }

    protected List<CompiledJavaType> compileGeneratedTypes(Collection<SourcedJavaType> sourceTypes, Context context) {
        List<CompiledJavaType> result = new ArrayList<CompiledJavaType>();
        if(sourceTypes.isEmpty())  return result;
        ClassLoader classLoader = context.getOptional(ClassLoader.class, AbstractGenerator.class.getClassLoader());
        JavaSourceCompiler javaSourceCompiler = new JavaSourceCompilerImpl();
        JavaSourceCompiler.CompilationUnit compilationUnit = getCompilationUnit(context, javaSourceCompiler);
        for (SourcedJavaType sourcedType : sourceTypes) {
            String source = "" + sourcedType.getSourceCode();
            logger.log(Level.FINE, "Adding compilation class " + sourcedType.getType().getName());
            compilationUnit.addJavaSource(sourcedType.getType().getName(), source);
        }
        try {
            ClassLoader compilationClassLoader = javaSourceCompiler.compile(classLoader, compilationUnit);
            javaSourceCompiler.persistCompiledClasses(compilationUnit);
            context.replace(ClassLoader.class, compilationClassLoader);
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

    protected JavaSourceCompiler.CompilationUnit getCompilationUnit(Context context, JavaSourceCompiler javaSourceCompiler) {
        File compilationTargetDirectory = getCompilationTargetDirectory(context);
        JavaSourceCompiler.CompilationUnit result;
        if (compilationTargetDirectory != null) {
            result = javaSourceCompiler.createCompilationUnit(compilationTargetDirectory);
            result.addClassPathEntries(ClassPathUtil.getClassPathEntries());
            result.addClassPathEntry(compilationTargetDirectory.getAbsolutePath());
        } else {
            result = javaSourceCompiler.createCompilationUnit();
        }
        context.replace(JavaSourceCompiler.CompilationUnit.class, result);
        return result;
    }

    protected File getCompilationTargetDirectory(Context context) {
        if (context.contains(UnitDescriptor.class)  && ! Strings.isNullOrEmpty(context.get(UnitDescriptor.class).getTargetCompilationDirectory())) {
            return new File(context.get(UnitDescriptor.class).getTargetCompilationDirectory());
        } else if (context.contains(JavaSourceCompiler.CompilationUnit.class)) {
            return context.get(JavaSourceCompiler.CompilationUnit.class).getOutputClassDirectory();
        }
        return null;
    }


    protected SourcedJavaType renderCode(JavaTypeBuilder javaTypeBuilder) {
        return CodeGeneratorUtil.renderCode(javaTypeBuilder, rendererProvider);
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
        return CodeGeneratorUtil.formatTargetClassName(context, sourceType.getPackageName(), sourceType.getSimpleName(), getNamingConvention(context));
    }


    protected T getSetting(Properties properties) {
        return new ObjectProvider<T>(getSettingClass(), properties).get();
    }


    abstract protected boolean isApplicable(JavaType javaType, Context context);


    protected SourceMatcher applyProperties(SourceMatcher sourceMatcher, Context context) {
        SourceMatcherImpl result = new SourceMatcherImpl();
        if (sourceMatcher == null) return null;
        String sourceDirectory = sourceMatcher.getSourceDirectory();

        if (Strings.isNullOrEmpty(sourceDirectory) && context.contains(UnitDescriptor.class)) {
            sourceDirectory = context.get(UnitDescriptor.class).getSourceDirectory();
        }

        result.setSourceDirectory(expandProperty(sourceDirectory));
        result.setIncludeSubpackages(sourceMatcher.isIncludeSubpackages());
        result.setPackageNames(sourceMatcher.getPackageNames());
        result.setClassNames(sourceMatcher.getClassNames());
        result.setDependencyPackages(sourceMatcher.getDependencyPackages());
        result.setExclusionPatterns(sourceMatcher.getExclusionPatterns());
        result.setInclusionPatterns(sourceMatcher.getInclusionPatterns());
        return result;
    }

    protected Properties applyProperties(Properties properties) {
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

            List<JavaField> fields = extractor.extract(sourceType, context);
            for(JavaField field: fields) {
                if(builder.containsField(field.getName())) continue;
                builder.addField(field);
            }
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

    abstract public NamingConvention getNamingConvention(Context context);


    abstract public Class<T> getSettingClass();
}
