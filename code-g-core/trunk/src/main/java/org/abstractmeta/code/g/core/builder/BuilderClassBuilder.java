package org.abstractmeta.code.g.core.builder;

import org.abstractmeta.code.g.code.JavaType;
import org.abstractmeta.code.g.core.code.builder.JavaTypeBuilder;
import org.abstractmeta.code.g.core.handler.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents BuilderClassBuilder
 * <p>
 * </p>
 *
 * @author Adrian Witas
 */
public class BuilderClassBuilder extends JavaTypeBuilder {

    private final Map<String, String> immutableImplementation;
    private final JavaType buildType;
    
    public BuilderClassBuilder(JavaType builtType) {
        super();
        this.buildType = builtType;
        this.immutableImplementation = new HashMap<String, String>();
        addFieldHandler(new BuilderSetterFieldHandler(this));
        addFieldHandler(new BuilderCollectionFieldHandler(this));
        addFieldHandler(new BuilderMapFieldHandler(this));
        addFieldHandler(new BuilderArrayFieldHandler(this));
        addFieldHandler(new GetterFieldHandler(this));
        addFieldHandler(new HasFieldHandler(this));

        addTypeHandler(new BuilderTypeHandler(this, immutableImplementation));
        addTypeHandler(new BuilderMergeHandler(this));
    }

    public JavaType getBuildType() {
        return buildType;
    }

    public void addImmutableImplementation(String source, String target) {
        immutableImplementation.put(source, target);
    }
}
