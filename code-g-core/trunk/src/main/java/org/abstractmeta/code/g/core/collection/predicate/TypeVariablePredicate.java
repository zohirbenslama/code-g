package org.abstractmeta.code.g.core.collection.predicate;

import com.google.common.base.Predicate;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Map;

/**
 * Represents TypeVariablePredicate
 * <p>
 * </p>
 *
 * @author Adrian Witas
 */
public class TypeVariablePredicate implements Predicate<Type> {

    private final Map<String, Type> genericTypeVariables;
    public TypeVariablePredicate(Map<String, Type> genericTypeVariables) {
        this.genericTypeVariables = genericTypeVariables;
    }

    @Override
    public boolean apply(Type type) {

        if(type instanceof TypeVariable) {
            return ! genericTypeVariables.containsKey(TypeVariable.class.cast(type).getName());
        } else if(type instanceof ParameterizedType) {
            for(Type argumentType: ParameterizedType.class.cast(type).getActualTypeArguments()) {
                if(apply(argumentType))  {
                    return true;
                }
            }

        } else if(type instanceof GenericArrayType) {
            return apply(GenericArrayType.class.cast(type).getGenericComponentType());

        }
        return false;
    }
}
