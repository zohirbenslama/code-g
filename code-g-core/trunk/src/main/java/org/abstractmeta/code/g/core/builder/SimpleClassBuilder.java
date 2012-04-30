package org.abstractmeta.code.g.core.builder;

import org.abstractmeta.code.g.code.JavaType;
import org.abstractmeta.code.g.core.code.builder.JavaTypeBuilder;
import org.abstractmeta.code.g.core.handler.*;

/**
 * Represents SimpleClassBuilder
 * <p>
 * </p>
 *
 * @author Adrian Witas
 */
public class SimpleClassBuilder extends JavaTypeBuilder {

    public SimpleClassBuilder(JavaType sourceType) {
        super();
        setSourceType(sourceType);
        addFieldHandler(new RegistryFieldHandler(this));
        addFieldHandler(new CollectionFieldHandler(this));
        addFieldHandler(new SetterFieldHandler(this));
        addFieldHandler(new GetterFieldHandler(this));
        addTypeHandler(new SimpleTypeHandler(this));
    }

}
