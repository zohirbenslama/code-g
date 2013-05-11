package org.abstractmeta.code.g.core.generator;

import org.abstractmeta.code.g.code.*;
import org.abstractmeta.code.g.config.Descriptor;
import org.abstractmeta.code.g.config.NamingConvention;
import org.abstractmeta.code.g.core.builder.SimpleClassBuilder;
import org.abstractmeta.code.g.core.config.DescriptorImpl;
import org.abstractmeta.code.g.core.config.NamingConventionImpl;
import org.abstractmeta.code.g.core.config.SourceMatcherImpl;
import org.abstractmeta.code.g.core.config.loader.JavaSourceLoaderImpl;
import org.abstractmeta.code.g.core.diconfig.JavaTypeRendererProvider;
import org.abstractmeta.code.g.core.extractor.AccessorFieldExtractor;
import org.abstractmeta.code.g.core.extractor.RegistryFieldExtractor;
import org.abstractmeta.code.g.core.extractor.SuperMethodExtractor;
import org.abstractmeta.code.g.core.internal.TypeNameWrapper;
import org.abstractmeta.code.g.core.property.PropertyRegistryImpl;
import org.abstractmeta.code.g.extractor.FieldExtractor;
import org.abstractmeta.code.g.extractor.MethodExtractor;
import org.abstractmeta.code.g.generator.CodeGenerator;
import org.abstractmeta.code.g.generator.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Represents Basic Class generator.
 * This generator extracts fields from interface or superclass
 * with {@link AccessorFieldExtractor}  and {@link RegistryFieldExtractor}
 * and generates implementation using {@link SimpleClassBuilder}
 * see {@link ClassGeneratorConfig} and {@link org.abstractmeta.code.g.config.Descriptor} for configuration details.
 *
 *
 * @author Adrian Witas
 */
public class ClassGenerator extends AbstractGenerator<ClassGeneratorConfig> implements CodeGenerator<ClassGeneratorConfig> {

    private final NamingConvention DEFAULT_NAMING_CONVENTION = new NamingConventionImpl("", "Impl", "impl");
    static private final Collection<FieldExtractor> FIELD_EXTRACTORS = Arrays.asList(new RegistryFieldExtractor(), new AccessorFieldExtractor());
    static private final Collection<MethodExtractor> METHOD_EXTRACTORS = Arrays.<MethodExtractor>asList(new SuperMethodExtractor());

    private final BuilderGenerator builderGenerator;

    public ClassGenerator() {
        super(new JavaSourceLoaderImpl(), new PropertyRegistryImpl(), new JavaTypeRendererProvider());
        this.builderGenerator = new BuilderGenerator(sourceLoader, propertyRegistry, rendererProvider);
    }


    @Override
    protected Collection<SourcedJavaType> generate(JavaType sourceType, Context context) {
        String targetName = formatTargetClassName(context, sourceType);
        SimpleClassBuilder simpleClassBuilder = new SimpleClassBuilder(targetName, sourceType, context);
        simpleClassBuilder.addModifiers(JavaModifier.PUBLIC);
        if(JavaKind.CLASS.equals(sourceType.getKind())) {
            simpleClassBuilder.setSuperType(new TypeNameWrapper(sourceType.getName()));
        } else if(JavaKind.INTERFACE.equals(sourceType.getKind())) {
            simpleClassBuilder.addSuperInterfaces(new TypeNameWrapper(sourceType.getName()));
        }
        addExtractableFields(simpleClassBuilder, sourceType, context);
        addExtractableMethods(simpleClassBuilder, sourceType, context);
        SourcedJavaType result = renderCode(simpleClassBuilder);
        return Arrays.asList(result);
    }


    @Override
    public List<CompiledJavaType> generate(Context context) {
        List<CompiledJavaType> generatedClasses =  super.generate(context);
        ClassGeneratorConfig config = context.get(ClassGeneratorConfig.class);
        if(config.isGenerateBuilder()) {
            Descriptor classGeneratorDescriptor = context.remove(Descriptor.class);
            Descriptor builderDescriptor = getBuilderDescriptor(generatedClasses, classGeneratorDescriptor);
            context.put(Descriptor.class, builderDescriptor);
            Collection<CompiledJavaType> builderClasses = builderGenerator.generate(context);
            List<CompiledJavaType> result = new ArrayList<CompiledJavaType>(builderClasses.size() + generatedClasses.size());
            result.addAll(generatedClasses);
            result.addAll(builderClasses);
            context.replace(Descriptor.class, classGeneratorDescriptor);
            return result;
        }
        return generatedClasses;
    }

    protected Descriptor getBuilderDescriptor(Collection<CompiledJavaType> compiledJavaTypes, Descriptor descriptor) {
        DescriptorImpl result = new DescriptorImpl();
        SourceMatcherImpl sourceMatcher = new SourceMatcherImpl();
        Collection<String> classNames = new ArrayList<String>() ;
        for(CompiledJavaType compiledJavaType: compiledJavaTypes) {
            classNames.add(compiledJavaType.getType().getName());
        }
        sourceMatcher.setClassNames(classNames);
        result.setSourceMatcher(sourceMatcher);
        result.setProperties(descriptor.getProperties());
        return result;
    }


    @Override
    protected boolean isApplicable(JavaType javaType, Context context) {
        return isExtractable(javaType, context);
    }


    @Override
    protected Collection<FieldExtractor> getFieldExtractors() {
        return FIELD_EXTRACTORS;
    }

    @Override
    protected Collection<MethodExtractor> getMethodExtractors() {
        return METHOD_EXTRACTORS;
    }

    @Override
    public NamingConvention getNamingConvention(Context context) {
        return DEFAULT_NAMING_CONVENTION;
    }

    @Override
    public Class<ClassGeneratorConfig> getSettingClass() {
        return ClassGeneratorConfig.class;
    }
}
