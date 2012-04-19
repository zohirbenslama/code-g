package org.abstractmeta.code.g.core.collection.function;


import org.abstractmeta.code.g.pattern.MethodGroupMatch;
import com.google.common.base.Function;

import javax.annotation.Nullable;

public class OperationNameMatchFunction implements Function<String, String> {

    private final String methodName;

    public OperationNameMatchFunction(String methodName) {
        this.methodName = methodName;
    }


    @Override
    public String apply(@Nullable String operationCandidate) {
        if (operationCandidate.contains(MethodGroupMatch.GROUP_NAME_PLACEHOLDER)) {
            int groupNameIndexOf = operationCandidate.indexOf(MethodGroupMatch.GROUP_NAME_PLACEHOLDER);
            String prefix = operationCandidate.substring(0, groupNameIndexOf);
            String postfix = operationCandidate.substring(groupNameIndexOf + MethodGroupMatch.GROUP_NAME_PLACEHOLDER.length());
            if (methodName.startsWith(prefix) && methodName.endsWith(postfix)) {
                return methodName.substring(groupNameIndexOf, methodName.length() - postfix.length());
            }

        } else if (methodName.startsWith(operationCandidate)) {
            if (operationCandidate.length() < methodName.length()) {
                return methodName.substring(operationCandidate.length());
            }
        }
        return MethodGroupMatch.DEFAULT_GROUP_NAME;
    }
}
