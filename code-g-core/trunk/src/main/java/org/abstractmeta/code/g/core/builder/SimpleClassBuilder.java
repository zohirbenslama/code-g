package org.abstractmeta.code.g.core.builder;

import org.abstractmeta.code.g.code.JavaKind;
import org.abstractmeta.code.g.code.JavaType;
import org.abstractmeta.code.g.core.code.builder.JavaTypeBuilderImpl;
import org.abstractmeta.code.g.core.code.handler.field.GetterFieldHandler;
import org.abstractmeta.code.g.core.code.handler.field.SetterFieldHandler;
import org.abstractmeta.code.g.core.code.handler.type.ClassHandler;
import org.abstractmeta.code.g.core.code.handler.type.EqualMethodHandler;
import org.abstractmeta.code.g.core.code.handler.type.HashCodeMethodHandler;
import org.abstractmeta.code.g.generator.Context;

/**
 * SimpleClassBuilder
 * <p>
 * This builder uses the following field handlers:
 * <ul>
 *     <li>{@link SetterFieldHandler}</li>
 *     <li>{@link GetterFieldHandler}</li>
 * </ul>
 * </p>
 *
 * <p>
 * This builder uses the following type handlers:
 * <ul>
 *     <li>{@link ClassHandler}</li>
 *     <li>{@link EqualMethodHandler}</li>
 *     <li>{@link HashCodeMethodHandler}</li>

 * </ul>
 * </p>

 * @author Adrian Witas
 */
public class SimpleClassBuilder extends JavaTypeBuilderImpl {

    public SimpleClassBuilder(String typeName, JavaType sourceType, Context context) {
        super(JavaKind.CLASS, typeName, sourceType, context);
        addFieldHandlers(
                new SetterFieldHandler(),
                new GetterFieldHandler()

        );
        addTypeHandlers(
                new ClassHandler(),
                new EqualMethodHandler(),
                new HashCodeMethodHandler()
        );



        //addFieldHandlers(new RegistryFieldHandler(this));
        //addFieldHandler(new CollectionFieldHandler(this, descriptor));
        //addMethodHandler(new SupperMethodHandler(this, descriptor));
        //addTypeHandler(new HashCodeMethodHandler(this, descriptor, javaTypeRegistry));
        //addTypeHandler(new EqualMethodHandler(this, descriptor, javaTypeRegistry));
        //addTypeHandler(new ClassHandler(this, descriptor, javaTypeRegistry));
    }
}
