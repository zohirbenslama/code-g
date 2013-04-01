package org.abstractmeta.code.g.core.internal;

import com.google.common.base.Preconditions;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;

/**
 * Represents GenericArrayTypeImpl
 * <p>
 * </p>
 *
 * @author Adrian Witas
 */
public class GenericArrayTypeImpl implements GenericArrayType {

    private final Type genericComponentType;

    public GenericArrayTypeImpl(Type genericComponentType) {
        Preconditions.checkNotNull(genericComponentType, "genericComponentType was null");
        this.genericComponentType = genericComponentType;
    }

    @Override
    public Type getGenericComponentType() {
        return genericComponentType;
    }
}
