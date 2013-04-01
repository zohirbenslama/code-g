package org.abstractmeta.code.g.core.generator;

import org.abstractmeta.code.g.code.JavaType;
import org.abstractmeta.code.g.code.SourcedJavaType;
import org.abstractmeta.code.g.config.NamingConvention;
import org.abstractmeta.code.g.core.builder.SimpleClassBuilder;
import org.abstractmeta.code.g.core.config.NamingConventionImpl;
import org.abstractmeta.code.g.core.config.loader.JavaSourceLoaderImpl;
import org.abstractmeta.code.g.core.diconfig.JavaTypeRendererProvider;
import org.abstractmeta.code.g.core.extractor.AccessorFieldExtractor;
import org.abstractmeta.code.g.core.extractor.RegistryFieldExtractor;
import org.abstractmeta.code.g.core.extractor.SuperMethodExtractor;
import org.abstractmeta.code.g.core.property.PropertyRegistryImpl;
import org.abstractmeta.code.g.extractor.FieldExtractor;
import org.abstractmeta.code.g.extractor.MethodExtractor;
import org.abstractmeta.code.g.generator.CodeGenerator;
import org.abstractmeta.code.g.generator.Context;

import java.util.Arrays;
import java.util.Collection;

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

    public ClassGenerator() {
        super(new JavaSourceLoaderImpl(), new PropertyRegistryImpl(), new JavaTypeRendererProvider());
    }


    @Override
    protected Collection<SourcedJavaType> generate(JavaType sourceType, Context context) {
        String targetName = formatTargetClassName(context, sourceType);
        SimpleClassBuilder simpleClassBuilder = new SimpleClassBuilder(targetName, sourceType, context);
        addExtractableFields(simpleClassBuilder, sourceType, context);
        addExtractableMethods(simpleClassBuilder, sourceType, context);
        SourcedJavaType result = renderCode(simpleClassBuilder);
        ClassGeneratorConfig config = context.get(ClassGeneratorConfig.class);
        if(config.isGenerateBuilder()) {

        }
        return Arrays.asList(result);
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
    public NamingConvention getNamingConvention() {
        return DEFAULT_NAMING_CONVENTION;
    }

    @Override
    public Class<ClassGeneratorConfig> getSettingClass() {
        return ClassGeneratorConfig.class;
    }
}
