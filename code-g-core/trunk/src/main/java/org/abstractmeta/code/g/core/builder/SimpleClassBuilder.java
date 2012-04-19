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
        addFieldListener(new RegistryFieldHandler(this));
        addFieldListener(new CollectionFieldHandler(this));
        addFieldListener(new SetterFieldHandler(this));
        addFieldListener(new GetterFieldHandler(this));
        addTypeListener(new SimpleTypeHandler(this));
    }

}
