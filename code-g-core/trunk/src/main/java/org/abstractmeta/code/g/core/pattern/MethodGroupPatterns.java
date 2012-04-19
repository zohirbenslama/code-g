package org.abstractmeta.code.g.core.pattern;

import org.abstractmeta.code.g.core.pattern.builder.MethodGroupPatternBuilder;
import org.abstractmeta.code.g.core.pattern.builder.MethodPatternBuilder;
import org.abstractmeta.code.g.pattern.MethodGroupPattern;

/**
 * Command method group patterns.
 *
 * @author Adrian Witas
 */
public class MethodGroupPatterns {

    public static final MethodGroupPattern ACCESSOR_MUTATOR_PATTERN = new MethodGroupPatternBuilder()
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


    public static final MethodGroupPattern REGISTRY_PATTERN = new MethodGroupPatternBuilder()
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
                    .addOperationNames("unregisterAll")
                    .setBaseResultType(Object.class)
                    .build())
            .add(new MethodPatternBuilder()
                    .addOperationNames("registerAll")
                    .setBaseResultType(Object.class)
                    .addBaseParameterTypes(Iterable.class).build())
            .build();



}
