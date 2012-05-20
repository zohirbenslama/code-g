/**
 * Copyright 2011 Adrian Witas
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.abstractmeta.code.g.core.collection.function;


import org.abstractmeta.code.g.expression.AbstractionMatch;
import com.google.common.base.Function;

import javax.annotation.Nullable;

public class OperationNameMatchFunction implements Function<String, String> {

    private final String methodName;

    public OperationNameMatchFunction(String methodName) {
        this.methodName = methodName;
    }


    @Override
    public String apply(@Nullable String operationCandidate) {
        if (operationCandidate.contains(AbstractionMatch.GROUP_NAME_PLACEHOLDER)) {
            int groupNameIndexOf = operationCandidate.indexOf(AbstractionMatch.GROUP_NAME_PLACEHOLDER);
            String prefix = operationCandidate.substring(0, groupNameIndexOf);
            String postfix = operationCandidate.substring(groupNameIndexOf + AbstractionMatch.GROUP_NAME_PLACEHOLDER.length());
            if (methodName.startsWith(prefix) && methodName.endsWith(postfix)) {
                return methodName.substring(groupNameIndexOf, methodName.length() - postfix.length());
            }

        } else if (methodName.startsWith(operationCandidate)) {
            if (operationCandidate.length() < methodName.length()) {
                return methodName.substring(operationCandidate.length());
            }
        }
        return AbstractionMatch.DEFAULT_GROUP_NAME;
    }
}
