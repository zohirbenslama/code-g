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

import org.abstractmeta.code.g.code.JavaModifier;
import org.abstractmeta.code.g.expression.AbstractionMatch;
import org.abstractmeta.code.g.expression.AbstractionPattern;
import org.abstractmeta.code.g.expression.MethodPattern;

import java.util.List;
import java.util.Map;

/**
 * Represents MethodPattern.
 * <p><b>Usage</b>
 * <code><pre>
 * AbstractionPattern expression  = new AbstractionPatternBuilder()
 * .add(new MethodPatternBuilder()
 * .addOperationNames("get", "is")
 * .setBaseResultType(Object.class).generate())
 * .add(new MethodPatternBuilder()
 * .addOperationNames("set")
 * .setBaseResultType(void.class)
 * .addBaseParameterTypes(Object.class).generate()).generate();
 * <p/>
 * </pre></code></p>
 *
 * @author Adrian Witas
 */
public class MethodPatternImpl implements MethodPattern {

    private final List<String> operationNames;
    private final List<Class> baseParameterTypes;
    private final List<JavaModifier> modifiers;
    private final Class baseResultType;
    private final boolean singularNameMatching;
    private final Map<Integer, AbstractionPattern> parametersAbstractionPattern;
    private final AbstractionMatch resultAbstractionPattern;

    public MethodPatternImpl(List<String> operationNames, List<Class> baseParameterTypes, List<JavaModifier> modifiers, Class baseResultType, boolean singularNameMatching, Map<Integer, AbstractionPattern> parametersAbstractionPattern, AbstractionMatch resultAbstractionPattern) {
        this.operationNames = operationNames;
        this.baseParameterTypes = baseParameterTypes;
        this.modifiers = modifiers;
        this.baseResultType = baseResultType;
        this.singularNameMatching = singularNameMatching;
        this.parametersAbstractionPattern = parametersAbstractionPattern;
        this.resultAbstractionPattern = resultAbstractionPattern;
    }

    public List<String> getOperationNames() {
        return operationNames;
    }

    public List<Class> getBaseParameterTypes() {
        return baseParameterTypes;
    }

    public List<JavaModifier> getModifiers() {
        return modifiers;
    }

    public Class getBaseResultType() {
        return baseResultType;
    }

    @Override
    public boolean hasParameterAbstractionPattern(int indexParameter) {
        return parametersAbstractionPattern.containsKey(indexParameter);
    }

    @Override
    public AbstractionPattern gatParameterAbstractionPattern(int indexParameter) {
        return parametersAbstractionPattern.get(indexParameter);
    }

    @Override
    public AbstractionMatch getResultAbstractionPattern() {
        return resultAbstractionPattern;
    }

    public boolean isSingularNameMatching() {
        return singularNameMatching;
    }


}
