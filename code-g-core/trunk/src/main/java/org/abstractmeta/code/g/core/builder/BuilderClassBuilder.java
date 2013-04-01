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
import org.abstractmeta.code.g.core.builder.handler.field.*;
import org.abstractmeta.code.g.core.builder.handler.type.BuilderMergeHandler;
import org.abstractmeta.code.g.core.builder.handler.type.BuilderTypeHandler;
import org.abstractmeta.code.g.core.code.builder.JavaTypeBuilderImpl;
import org.abstractmeta.code.g.generator.Context;

/**
 * BuilderClassBuilder
 *
 * @author Adrian Witas
 */
public class BuilderClassBuilder extends JavaTypeBuilderImpl {

    public BuilderClassBuilder(String typeName, JavaType sourceType, Context context) {
        super(JavaKind.CLASS, typeName, sourceType, context);
        addFieldHandlers(
           new BuilderArrayFieldHandler(),
           new BuilderCollectionFieldHandler(),
           new BuilderMapFieldHandler(),
           new BuilderSetterFieldHandler(),
           new GetterFieldHandler()
        );
        addTypeHandlers(
                new BuilderTypeHandler(),
                new BuilderMergeHandler()
        );

    }
}
