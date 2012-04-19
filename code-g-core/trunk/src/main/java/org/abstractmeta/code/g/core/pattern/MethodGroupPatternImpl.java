package org.abstractmeta.code.g.core.pattern;

import org.abstractmeta.code.g.pattern.MethodGroupPattern;
import org.abstractmeta.code.g.pattern.MethodPattern;

import java.util.Iterator;
import java.util.List;

/**
 * Represents MethodGroupPattern.
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
 * </p>
 *
 * @author Adrian Witas
 */
public class MethodGroupPatternImpl implements MethodGroupPattern {

    private final List<MethodPattern> methodPatterns;

    public MethodGroupPatternImpl(List<MethodPattern> methodPatterns) {
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
