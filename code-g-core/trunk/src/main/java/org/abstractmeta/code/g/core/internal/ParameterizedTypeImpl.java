package org.abstractmeta.code.g.core.internal;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ParameterizedTypeImpl implements ParameterizedType {
    
    private final Type ownerType;
    private final Type rawType;
    private final Type [] actualTypeArguments;

    public ParameterizedTypeImpl(Type ownerType, Type rawType, Type ... actualTypeArguments) {
        this.ownerType = ownerType;
        this.rawType = rawType;
        this.actualTypeArguments = actualTypeArguments;
    }

    public Type getOwnerType() {
        return ownerType;
    }

    public Type getRawType() {
        return rawType;
    }

    public Type[] getActualTypeArguments() {
        return actualTypeArguments;
    }
}
