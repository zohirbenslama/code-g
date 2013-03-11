package org.abstractmeta.code.g.core.invocation.builder;

import org.abstractmeta.code.g.core.invocation.InvocationParameter;

import java.lang.reflect.Type;

/**
 * InvocationParameter builder.
 * @author Adrian Witas
 */
public class InvocationParameterBuilder {
    private String name;
    private String value;
    private String className;
    private Type type;
    private boolean dynamic;

    public String getName() {
        return name;
    }

    public InvocationParameterBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public String getValue() {
        return value;
    }

    public InvocationParameterBuilder setValue(String value) {
        this.value = value;
        return this;
    }

    public String getClassName() {
        return className;
    }

    public InvocationParameterBuilder setClassName(String className) {
        this.className = className;
        return this;
    }

    public Type getType() {
        return type;
    }

    public InvocationParameterBuilder setType(Type type) {
        this.type = type;
        return this;
    }

    public boolean isDynamic() {
        return dynamic;
    }

    public InvocationParameterBuilder setDynamic(boolean dynamic) {
        this.dynamic = dynamic;
        return this;
    }

    public InvocationParameter build() {
        return new InvocationParameter(dynamic, type, className, value, name);
    }


}
