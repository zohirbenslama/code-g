package org.abstractmeta.code.g.core.generator;

import org.abstractmeta.code.g.code.JavaType;
import org.abstractmeta.code.g.code.SourcedJavaType;
import org.abstractmeta.code.g.config.NamingConvention;
import org.abstractmeta.code.g.config.loader.SourceLoader;
import org.abstractmeta.code.g.generator.CodeGenerator;
import org.abstractmeta.code.g.generator.Context;
import org.abstractmeta.code.g.property.PropertyRegistry;
import org.abstractmeta.code.g.renderer.JavaTypeRenderer;

import javax.inject.Provider;
import java.util.Collection;

/**
 * Represents BuilderGenerator
 *
 * @author Adrian Witas
 */
public class BuilderGenerator extends AbstractGenerator<BuilderGeneratorConfig> implements CodeGenerator<BuilderGeneratorConfig> {


    public BuilderGenerator(SourceLoader sourceLoader, PropertyRegistry propertyRegistry, Provider<JavaTypeRenderer> rendererProvider) {
        super(sourceLoader, propertyRegistry, rendererProvider);
    }

    @Override
    protected Collection<SourcedJavaType> generate(JavaType sourceType, Context context) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected boolean isApplicable(JavaType javaType, Context context) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public NamingConvention getNamingConvention() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Class<BuilderGeneratorConfig> getSettingClass() {
        return BuilderGeneratorConfig.class;
    }


}
