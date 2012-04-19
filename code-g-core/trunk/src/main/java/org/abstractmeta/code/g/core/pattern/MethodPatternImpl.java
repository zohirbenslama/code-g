package org.abstractmeta.code.g.core.pattern;

import org.abstractmeta.code.g.pattern.MethodPattern;

import java.util.List;

/**
 * Represents MethodPattern.
 * <p><b>Usage</b>
 * <code><pre>
 * MethodGroupPattern pattern  = new MethodGroupPatternBuilder()
 * .add(new MethodPatternBuilder()
 * .addOperationNames("get", "is")
 * .setBaseResultType(Object.class).build())
 * .add(new MethodPatternBuilder()
 * .addOperationNames("set")
 * .setBaseResultType(void.class)
 * .addBaseParameterTypes(Object.class).build()).build();
 * <p/>
 * </pre></code></p>
 *
 * @author Adrian Witas
 */
public class MethodPatternImpl implements MethodPattern {

    private final List<String> operationNames;
    private final List<Class> baseParameterTypes;
    private final List<String> modifiers;
    private final Class baseResultType;
    private final boolean singularNameMatching;

    public MethodPatternImpl(List<String> operationNames, List<Class> baseParameterTypes,  List<String> modifiers, Class baseResultType, boolean singularNameMatching) {
        this.operationNames = operationNames;
        this.baseParameterTypes = baseParameterTypes;
        this.modifiers = modifiers;
        this.baseResultType = baseResultType;
        this.singularNameMatching = singularNameMatching;
    }

    public List<String> getOperationNames() {
        return operationNames;
    }

    public List<Class> getBaseParameterTypes() {
        return baseParameterTypes;
    }

    public List<String> getModifiers() {
        return modifiers;
    }

    public Class getBaseResultType() {
        return baseResultType;
    }

    public boolean isSingularNameMatching() {
        return singularNameMatching;
    }


}
