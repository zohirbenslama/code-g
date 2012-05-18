package org.abstractmeta.code.g.core.expression;

import org.abstractmeta.code.g.expression.AbstractionPattern;
import org.abstractmeta.code.g.expression.MethodPattern;

import java.util.Iterator;
import java.util.List;

/**
 * Represents AbstractionPattern.
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
 * </p>
 *
 * @author Adrian Witas
 */
public class AbstractionPatternImpl implements AbstractionPattern {

    private final List<MethodPattern> methodPatterns;

    public AbstractionPatternImpl(List<MethodPattern> methodPatterns) {
        this.methodPatterns = methodPatterns;
    }

    @Override
    public void add(MethodPattern pattern) {
        methodPatterns.add(pattern);
    }

    @Override
    public int size() {
        return methodPatterns.size();
    }

    @Override
    public Iterator<MethodPattern> iterator() {
        return methodPatterns.iterator();
    }
}
