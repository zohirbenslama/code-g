/**
 * Copyright 2011 Adrian Witas
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
