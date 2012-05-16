package org.abstractmeta.code.g.core.collection.predicates;


import org.abstractmeta.code.g.expression.AbstractionMatch;
import com.google.common.base.Predicate;


/**
 * Group operation match predicate.
 * It matches method name with expression operation names.
 * <p>See more at {@link org.abstractmeta.code.g.expression.MethodPattern}.
 * </p>
 * @author Adrian
 */
public class OperationMatchPredicate implements Predicate<String> {

    private final String methodName;

    public OperationMatchPredicate(String methodName) {
        this.methodName = methodName;
    }

    @Override
    public boolean apply(String operationName) {
         if (methodName.startsWith(operationName)) {
            return true;
        }
        if (operationName.contains(AbstractionMatch.GROUP_NAME_PLACEHOLDER)) {
            int groupNameIndexOf = operationName.indexOf(AbstractionMatch.GROUP_NAME_PLACEHOLDER);
            String operationNamePrefix = operationName.substring(0, groupNameIndexOf);
            String operationNamePostfix = operationName.substring(groupNameIndexOf + AbstractionMatch.GROUP_NAME_PLACEHOLDER.length());
            return methodName.startsWith(operationNamePrefix)
                    && methodName.endsWith(operationNamePostfix)
                    && methodName.length() > operationNamePostfix.length() + operationNamePrefix.length();
        }
        return false;
    }
}
