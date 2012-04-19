package org.abstractmeta.code.g.core.pattern.builder;

import org.abstractmeta.code.g.core.pattern.MethodPatternImpl;
import org.abstractmeta.code.g.pattern.MethodPattern;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

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

        return this;
    }

    public MethodPattern build() {
        return new MethodPatternImpl(operationNames, baseParameterTypes, modifiers, baseResultType, singularNameMatching);
    }
}
