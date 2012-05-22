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
package org.abstractmeta.code.g.core.expression;

import org.abstractmeta.code.g.core.expression.builder.AbstractionPatternBuilder;
import org.abstractmeta.code.g.core.expression.builder.MethodPatternBuilder;
import org.abstractmeta.code.g.expression.AbstractionPattern;

/**
 * Command method group patterns.
 *
 * @author Adrian Witas
 */
public class AbstractionPatterns {

    public static final AbstractionPattern ACCESSOR_MUTATOR_PATTERN = new AbstractionPatternBuilder()
            .add(new MethodPatternBuilder()
                    .addOperationNames("get", "is")
                    .addModifiers("abstract")
                    .setBaseResultType(Object.class).build())
            .add(new MethodPatternBuilder()
                    .addOperationNames("set")
                    .addModifiers("abstract")
                    .setBaseResultType(void.class)
                    .addBaseParameterTypes(Object.class).build())
            .add(new MethodPatternBuilder()
                    .addOperationNames("add")
                     .addModifiers("abstract")
                    .setSingularNameMatching(true)
                    .setBaseResultType(void.class)
                    .addBaseParameterTypes(Object.class).build()).build();


    public static final AbstractionPattern REGISTRY_PATTERN = new AbstractionPatternBuilder()
            .add(new MethodPatternBuilder()
                    .addOperationNames("register")
                    .setBaseResultType(Object.class)
                    .addBaseParameterTypes(Object.class).build())
            .add(new MethodPatternBuilder()
                    .addOperationNames("unregister")
                    .setBaseResultType(Object.class)
                    .addBaseParameterTypes(Object.class).build())
            .add(new MethodPatternBuilder()
                    .addOperationNames("isRegistered")
                    .setBaseResultType(boolean.class)
                    .addBaseParameterTypes(Object.class).build())
            .add(new MethodPatternBuilder()
                    .addOperationNames("is<groupName>Registered")
                    .setBaseResultType(boolean.class)
                    .addBaseParameterTypes(Object.class).build())
            .add(new MethodPatternBuilder()
                    .addOperationNames("get")
                    .setBaseResultType(Object.class)
                    .addBaseParameterTypes(Object.class).build())
            .add(new MethodPatternBuilder()
                    .addOperationNames("getAll")
                    .setBaseResultType(Iterable.class)
                    .addBaseParameterTypes().build())
            .add(new MethodPatternBuilder()
                    .addOperationNames("unregisterAll")
                    .setBaseResultType(Object.class)
                    .build())
            .add(new MethodPatternBuilder()
                    .addOperationNames("registerAll")
                    .setBaseResultType(Object.class)
                    .addBaseParameterTypes(Iterable.class).build())
            .build();



}
