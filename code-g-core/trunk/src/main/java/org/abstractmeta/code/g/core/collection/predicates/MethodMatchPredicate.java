package org.abstractmeta.code.g.core.collection.predicates;


import org.abstractmeta.code.g.code.JavaMethod;
import org.abstractmeta.code.g.expression.MethodPattern;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import java.lang.reflect.Type;

/**
 * Method match predicate.
 * It applies {@link MethodPattern} rules to a given java method.
 *
 */
public class MethodMatchPredicate implements Predicate<JavaMethod> {

    private final MethodPattern pattern;
    private final Predicate<Type> resultPredicate;

    public MethodMatchPredicate(MethodPattern pattern) {
        this.pattern = pattern;
        this.resultPredicate = new TypeMatchPredicate(pattern.getBaseResultType());
    }

    @Override
    public boolean apply(JavaMethod candidate) {
        return Iterables.indexOf(pattern.getOperationNames(), new OperationMatchPredicate(candidate.getName())) != -1
                && matchesParameterTypes(candidate)
                && matchesResultType(candidate)
                && matchesModifiers(candidate);
    }


    protected boolean matchesResultType(JavaMethod candidate) {
        return resultPredicate.apply(candidate.getResultType());
    }

    protected boolean matchesParameterTypes(JavaMethod candidate) {
        if(pattern.getBaseParameterTypes().size() != candidate.getParameterTypes().size())  {
            return false;
        }
        for (int i = 0; i < pattern.getBaseParameterTypes().size(); i++) {
            Predicate<Type> argumentPredicate = new TypeMatchPredicate(pattern.getBaseParameterTypes().get(i));
            if (!argumentPredicate.apply(candidate.getParameterTypes().get(i))) {
                return false;
            }
        }
        return true;
    }


    protected boolean matchesModifiers(JavaMethod candidate) {
        if (pattern.getModifiers().size() > 0) {
            for (String modifier : pattern.getModifiers()) {
                if (!candidate.getModifiers().contains(modifier)) {
                    return false;
                }
            }
        }
        return true;

    }

}
