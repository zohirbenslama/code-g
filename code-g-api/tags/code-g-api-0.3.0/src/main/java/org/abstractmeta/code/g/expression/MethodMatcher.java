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
package org.abstractmeta.code.g.expression;

import org.abstractmeta.code.g.code.JavaMethod;

import java.util.List;
import java.util.Map;

/**
 * Matches all supplied method with the method group patterns
 *
 * @author Adrian Witas
 */
public interface MethodMatcher {

    /**
     * Matches given java methods with supplied method group expression.
     * @param methods java method
     * @param groupPattern group expression.
     * @return list of method group match
     */
    List<AbstractionMatch> match(List<JavaMethod> methods, AbstractionPattern groupPattern);

    /**
     * Indexes AbstractionMatch by name.
     * @param methodGroupMatch method group match
     * @return key value pair of group name, method group match
     */
    Map<String, AbstractionMatch> indexByName(List<AbstractionMatch> methodGroupMatch);
}
