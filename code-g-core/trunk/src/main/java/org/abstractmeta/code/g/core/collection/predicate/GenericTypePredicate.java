package org.abstractmeta.code.g.core.collection.predicate;

import com.google.common.base.Predicate;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

/**
 * Represents GenericTypePredicate
 * <p>
 * </p>
 *
 * @author Adrian Witas
 */
public class GenericTypePredicate implements Predicate<Type> {
    @Override
    public boolean apply(Type type) {
        return type instanceof TypeVariable || type instanceof ParameterizedType || type instanceof GenericArrayType;
    }
}
