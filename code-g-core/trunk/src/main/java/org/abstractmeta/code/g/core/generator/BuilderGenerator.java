package org.abstractmeta.code.g.core.generator;

import org.abstractmeta.code.g.code.*;
import org.abstractmeta.code.g.config.NamingConvention;
import org.abstractmeta.code.g.config.loader.SourceLoader;
import org.abstractmeta.code.g.core.builder.BuilderClassBuilder;
import org.abstractmeta.code.g.core.code.builder.JavaFieldBuilder;
import org.abstractmeta.code.g.core.config.NamingConventionImpl;
import org.abstractmeta.code.g.core.config.loader.JavaSourceLoaderImpl;
import org.abstractmeta.code.g.core.diconfig.JavaTypeRendererProvider;
import org.abstractmeta.code.g.core.property.PropertyRegistryImpl;
import org.abstractmeta.code.g.core.util.CodeGeneratorUtil;
import org.abstractmeta.code.g.generator.CodeGenerator;
import org.abstractmeta.code.g.generator.Context;
import org.abstractmeta.code.g.property.PropertyRegistry;
import org.abstractmeta.code.g.renderer.JavaTypeRenderer;

import javax.inject.Provider;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Represents BuilderGenerator
 *
 * @author Adrian Witas
 */
public class BuilderGenerator extends AbstractGenerator<BuilderGeneratorConfig> implements CodeGenerator<BuilderGeneratorConfig> {

    private final NamingConvention DEFAULT_NAMING_CONVENTION = new NamingConventionImpl("", "Builder", "builder");

    public  BuilderGenerator() {
        this(new JavaSourceLoaderImpl(), new PropertyRegistryImpl(), new JavaTypeRendererProvider());
    }

    public BuilderGenerator(SourceLoader sourceLoader, PropertyRegistry propertyRegistry, Provider<JavaTypeRenderer> rendererProvider) {
        super(sourceLoader, propertyRegistry, rendererProvider);
    }

    @Override
    protected Collection<SourcedJavaType> generate(JavaType sourceType, Context context) {
        String targetName = formatTargetClassName(context, sourceType);
        BuilderClassBuilder classBuilder = new BuilderClassBuilder(targetName, sourceType, context);
        classBuilder.addModifiers(JavaModifier.PUBLIC);
        boolean isIncludePresentFiled = isIncludeIsPresentField(context);
        for(JavaField field: sourceType.getFields()) {
            JavaFieldBuilder fieldBuilder = new JavaFieldBuilder();
            fieldBuilder.setModifiers(new ArrayList<JavaModifier>());
            fieldBuilder.addModifier(JavaModifier.PRIVATE);
            fieldBuilder.setName(field.getName());
            fieldBuilder.setType(field.getType());
            addFiled(classBuilder, fieldBuilder);
            if(isIncludePresentFiled) {
                JavaFieldBuilder presentFiledBuilder = new JavaFieldBuilder();
                presentFiledBuilder.addModifier(JavaModifier.PRIVATE).setType(boolean.class)
                        .setName(CodeGeneratorUtil.getPresentFieldName(field.getName()));
                addFiled(classBuilder, presentFiledBuilder);
            }
        }
        SourcedJavaType result = renderCode(classBuilder);
        return Arrays.asList(result);
    }

    protected void addFiled( BuilderClassBuilder classBuilder,  JavaFieldBuilder fieldBuilder) {
        if(! classBuilder.containsField(fieldBuilder.getName()))  {
            classBuilder.addField(fieldBuilder.build());
        }
    }


    @Override
    protected boolean isApplicable(JavaType javaType, Context context) {
        return !javaType.getFields().isEmpty() && javaType.getKind().equals(JavaKind.CLASS) && ! javaType.getModifiers().contains(JavaModifier.ABSTRACT);
    }

    protected boolean isIncludeIsPresentField(Context context) {
        BuilderGeneratorConfig config = context.getOptional(BuilderGeneratorConfig.class);
        return (config != null && config.isIncludeIsPresentField());
    }


    @Override
    public NamingConvention getNamingConvention(Context context) {
        return DEFAULT_NAMING_CONVENTION;
    }

    protected String formatTargetClassName(Context context, JavaType sourceType) {
        String sourcePackage = sourceType.getPackageName();
        if (sourcePackage.endsWith(".impl")) {
            sourcePackage = sourcePackage.replace(".impl", "");
        }
        return CodeGeneratorUtil.formatTargetClassName(context, sourcePackage, sourceType.getSimpleName(), getNamingConvention(context));

    }



    @Override
    public Class<BuilderGeneratorConfig> getSettingClass() {
        return BuilderGeneratorConfig.class;
    }


}
