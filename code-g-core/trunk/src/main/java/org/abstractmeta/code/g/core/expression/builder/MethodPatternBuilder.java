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
package org.abstractmeta.code.g.core.expression.builder;

import org.abstractmeta.code.g.core.expression.MethodPatternImpl;
import org.abstractmeta.code.g.expression.AbstractionMatch;
import org.abstractmeta.code.g.expression.AbstractionPattern;
import org.abstractmeta.code.g.expression.MethodPattern;

import java.util.*;

/**
 * Represents MethodPatternBuilder
 *
 * @author Adrian Witas
 */
public class MethodPatternBuilder {

    private List<String> operationNames = new ArrayList<String>();
    private List<Class> baseParameterTypes = new ArrayList<Class>();
    private List<String> modifiers = new ArrayList<String>();
    private Class baseResultType;
    private boolean singularNameMatching;
    private Map<Integer, AbstractionPattern> parametersAbstractionPattern = new HashMap<Integer, AbstractionPattern>();
    private AbstractionMatch resultAbstractionPattern;

    public List<String> getOperationNames() {
        return operationNames;
    }

    public MethodPatternBuilder setOperationNames(List<String> operationNames) {
        this.operationNames = operationNames;
        return this;
    }

    public MethodPatternBuilder addOperationNames(String... operationNames) {
        Collections.addAll(this.operationNames, operationNames);
        return this;
    }

    public MethodPatternBuilder addOperationNames(Collection<String> operationNames) {
        this.operationNames.addAll(operationNames);
        return this;
    }

    public List<Class> getBaseParameterTypes() {
        return baseParameterTypes;
    }

    public MethodPatternBuilder setBaseArgumentTypes(List<Class> baseParameterTypes) {
        this.baseParameterTypes = baseParameterTypes;
        return this;
    }


    public MethodPatternBuilder addBaseParameterTypes(Class... baseParameterTypes) {
        Collections.addAll(this.baseParameterTypes, baseParameterTypes);
        return this;
    }

    public MethodPatternBuilder addBaseParameterTypes(Collection<Class> baseParameterTypes) {
        this.baseParameterTypes.addAll(baseParameterTypes);
        return this;
    }


    public List<String> getModifiers() {
        return modifiers;
    }

    public MethodPatternBuilder setModifiers(List<String> modifiers) {
        this.modifiers = modifiers;
        return this;
    }


    public MethodPatternBuilder addModifiers(String... modifiers) {
        Collections.addAll(this.modifiers, modifiers);
        return this;
    }

    public MethodPatternBuilder addModifiers(Collection<String> modifiers) {
        this.modifiers.addAll(modifiers);
        return this;
    }


    public Class getBaseResultType() {
        return baseResultType;
    }

    public MethodPatternBuilder setBaseResultType(Class baseResultType) {
        this.baseResultType = baseResultType;
        return this;
    }

    public MethodPatternBuilder setSingularNameMatching(boolean singularNameMatching) {
        this.singularNameMatching = singularNameMatching;
        return this;
    }

    public MethodPatternBuilder addParametersAbstractionPattern(Integer parameterIndex, AbstractionPattern parametersAbstractionPattern) {
        this.parametersAbstractionPattern.put(parameterIndex, parametersAbstractionPattern);
        return this;
    }

    public MethodPatternBuilder setResultAbstractionPattern(AbstractionMatch resultAbstractionPattern) {
        this.resultAbstractionPattern = resultAbstractionPattern;
        return this;
    }

    public MethodPatternBuilder merge(MethodPattern instance) {
        if (instance.getBaseParameterTypes() != null) {
            baseParameterTypes.addAll(instance.getBaseParameterTypes());
        }

        if (instance.getOperationNames() != null) {
            operationNames.addAll(instance.getOperationNames());
        }

        if (instance.getModifiers() != null) {
            modifiers.addAll(instance.getModifiers());
        }

        if (instance.getBaseResultType() != null) {
            baseResultType = instance.getBaseResultType();
        }
        if (instance.getResultAbstractionPattern() != null) {
            resultAbstractionPattern = instance.getResultAbstractionPattern();
        }
        return this;
   }

    public Map<Integer, AbstractionPattern> getParametersAbstractionPattern() {
        return parametersAbstractionPattern;
    }

    public MethodPattern build() {
        return new MethodPatternImpl(operationNames, baseParameterTypes, modifiers, baseResultType, singularNameMatching, parametersAbstractionPattern, resultAbstractionPattern);
    }
}
