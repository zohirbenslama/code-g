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
