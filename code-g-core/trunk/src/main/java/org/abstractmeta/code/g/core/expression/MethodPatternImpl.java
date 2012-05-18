package org.abstractmeta.code.g.core.expression;

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
    private final Map<Integer, AbstractionPattern> parametersAbstractionPattern;
    private final AbstractionMatch resultAbstractionPattern;

    public MethodPatternImpl(List<String> operationNames, List<Class> baseParameterTypes, List<String> modifiers, Class baseResultType, boolean singularNameMatching, Map<Integer, AbstractionPattern> parametersAbstractionPattern, AbstractionMatch resultAbstractionPattern) {
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

    public List<String> getModifiers() {
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
