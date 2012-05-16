package org.abstractmeta.code.g.core.collection.predicates;



import org.abstractmeta.code.g.core.util.ReflectUtil;
import com.google.common.base.Predicate;

import java.lang.reflect.Type;


/**
 * Type match predicate.
 * MethodPattern defines superType of the type to be match with method candidate
 * to be eligible.
 * Object.class is used to match ayn type including primitives.
 *
 * <p>See more at {@link org.abstractmeta.code.g.expression.MethodPattern}.
 * </p>
 */
public class TypeMatchPredicate implements Predicate<Type> {

    private final Class baseType;

    public TypeMatchPredicate(Class baseType) {
        this.baseType = baseType;
    }

    @Override
    public boolean apply(Type rawTypeCandidate) {
        Class candidate = ReflectUtil.getRawClass(rawTypeCandidate);
        return baseType == null
            || Object.class.equals(baseType)
            || baseType.isAssignableFrom(candidate);
    }
}
