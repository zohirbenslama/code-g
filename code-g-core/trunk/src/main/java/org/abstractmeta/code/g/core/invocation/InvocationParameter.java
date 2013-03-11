package org.abstractmeta.code.g.core.invocation;

import java.lang.reflect.Type;

/**
 * Represents InvocationParameter
 *
 * @author Adrian Witas
 */
public class InvocationParameter {

    private final String name;
    private String value;
    private final String className;
    private final Type type;
    private final boolean dynamic;


    public InvocationParameter(boolean dynamic, Type type, String className, String value, String name) {
        this.dynamic = dynamic;
        this.type = type;
        this.className = className;
        this.value= value;
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getClassName() {
        return className;
    }

    public Type getType() {
        return type;
    }

    public boolean isDynamic() {
        return dynamic;
    }


}
