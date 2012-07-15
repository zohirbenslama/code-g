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
            || Object[].class.equals(baseType)
            || baseType.isAssignableFrom(candidate);
    }
}
