package org.abstractmeta.code.g.core.collection.predicates;


import org.abstractmeta.code.g.code.JavaMethod;
import com.google.common.base.Predicate;

/**
 * Method name predicate
 *
 * @author Adrian Witas
 */
public class MethodNamePredicate implements Predicate<JavaMethod> {

    private final String methodName;

    public MethodNamePredicate(String methodName) {
        this.methodName = methodName;
    }

    @Override
    public boolean apply(JavaMethod method) {
        return methodName.equals(method.getName());
    }
}
