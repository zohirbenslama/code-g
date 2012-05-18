package org.abstractmeta.code.g.core.expression.builder;

import org.abstractmeta.code.g.code.JavaMethod;
import org.abstractmeta.code.g.core.expression.MethodMatchImpl;
import org.abstractmeta.code.g.expression.MethodMatch;
import org.abstractmeta.code.g.expression.MethodPattern;

/**
 * Represents MethodMatchBuilder
 *
 * @author Adrian Witas
 */
public class MethodMatchBuilder {

    private  JavaMethod method;
    private MethodPattern pattern;

    public JavaMethod getMethod() {
        return method;
    }

    public MethodMatchBuilder setMethod(JavaMethod method) {
        this.method = method;
        return this;
    }

    public MethodPattern getPattern() {
        return pattern;
    }

    public MethodMatchBuilder setPattern(MethodPattern pattern) {
        this.pattern = pattern;
        return this;
    }

    public MethodMatch build() {
        return new MethodMatchImpl(method, pattern);
    }
}
