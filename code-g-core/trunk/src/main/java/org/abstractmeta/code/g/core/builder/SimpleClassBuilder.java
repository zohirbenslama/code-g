package org.abstractmeta.code.g.core.builder;

import org.abstractmeta.code.g.code.JavaKind;
import org.abstractmeta.code.g.code.JavaType;
import org.abstractmeta.code.g.core.builder.handler.field.IndexedCollectionFieldHandler;
import org.abstractmeta.code.g.core.builder.handler.field.RegistryFieldHandler;
import org.abstractmeta.code.g.core.code.builder.JavaTypeBuilderImpl;
import org.abstractmeta.code.g.core.builder.handler.field.GetterFieldHandler;
import org.abstractmeta.code.g.core.builder.handler.field.SetterFieldHandler;
import org.abstractmeta.code.g.core.builder.handler.type.ClassHandler;
import org.abstractmeta.code.g.core.builder.handler.type.EqualMethodHandler;
import org.abstractmeta.code.g.core.builder.handler.type.HashCodeMethodHandler;
import org.abstractmeta.code.g.core.expression.MethodMatcherImpl;
import org.abstractmeta.code.g.expression.MethodMatcher;
import org.abstractmeta.code.g.generator.Context;

/**
 * SimpleClassBuilder
 * <p>
 * This builder uses the following field handlers:
 * <ul>
 *     <li>{@link SetterFieldHandler}</li>
 *     <li>{@link GetterFieldHandler}</li>
 *     <li>{@link RegistryFieldHandler}</li>
 *     <li>{@link IndexedCollectionFieldHandler}</li>
 *
 *
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
                new GetterFieldHandler(),
                new RegistryFieldHandler(new MethodMatcherImpl()),
                new IndexedCollectionFieldHandler(new MethodMatcherImpl())

        );
        addTypeHandlers(
                new ClassHandler(),
                new EqualMethodHandler(),
                new HashCodeMethodHandler()
        );
    }



    public static interface Config {

        boolean addBuilder();

    }


}
