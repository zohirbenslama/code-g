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
package org.abstractmeta.code.g.core.expression;

import org.abstractmeta.code.g.expression.AbstractionMatch;
import org.abstractmeta.code.g.expression.MethodMatch;

import java.util.List;


/**
 * Method Group Match.
 * All method matched with a given expression.
 *
 * @author Adrian Witas
 */
public class AbstractionMatchImpl implements AbstractionMatch {

    private final String name;
    private final List<MethodMatch> matches;

    public AbstractionMatchImpl(String name, List<MethodMatch> matches) {
        this.name = name;
        this.matches = matches;
    }

    public String getName() {
        return name;
    }

    public List<MethodMatch> getMatches() {
        return matches;
    }

    @Override
    public boolean containsMatch(String operationName, Class... superParameterTypes) {
        return getMatch(operationName, superParameterTypes) != null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public MethodMatch getMatch(String operationName, Class... superParameterTypes) {
        for (MethodMatch match : matches) {
            if (match.getPattern().getOperationNames().contains(operationName)) {
                List<Class> patternBaseParameterTypes = match.getPattern().getBaseParameterTypes();
                if (superParameterTypes.length != patternBaseParameterTypes.size()) {
                    //Object[] means any number or type of argument types
                    if(superParameterTypes.length == 1 && Object[].class.equals(superParameterTypes[0])) {
                        return match;
                    }
                    continue;
                }
                for (int i = 0; i < superParameterTypes.length; i++) {
                    Class superParameterType = superParameterTypes[0];
                    if (!superParameterType.isAssignableFrom(patternBaseParameterTypes.get(i))) {
                        continue;
                    }
                }
                return match;
            }
        }
        return null;
    }

}
