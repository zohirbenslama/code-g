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
package org.abstractmeta.code.g.core.collection.predicate;


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
