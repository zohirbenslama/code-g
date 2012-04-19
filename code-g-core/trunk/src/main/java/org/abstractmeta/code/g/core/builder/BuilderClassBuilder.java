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
        addFieldListener(new BuilderSetterFieldHandler(this));
        addFieldListener(new BuilderCollectionFieldHandler(this));
        addFieldListener(new BuilderMapFieldHandler(this));
        addFieldListener(new BuilderArrayFieldHandler(this));
        addFieldListener(new GetterFieldHandler(this));
        addFieldListener(new HasFieldHandler(this));

        addTypeListener(new BuilderTypeHandler(this, immutableImplementation));
        addTypeListener(new BuilderMergeHandler(this));
    }

    public JavaType getBuildType() {
        return buildType;
    }

    public void addImmutableImplementation(String source, String target) {
        immutableImplementation.put(source, target);
    }
}
