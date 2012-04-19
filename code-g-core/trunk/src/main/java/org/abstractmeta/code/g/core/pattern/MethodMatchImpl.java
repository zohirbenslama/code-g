package org.abstractmeta.code.g.core.pattern;

import org.abstractmeta.code.g.code.JavaMethod;
import org.abstractmeta.code.g.pattern.MethodMatch;
import org.abstractmeta.code.g.pattern.MethodPattern;

/**
 * Represents MethodMatch.
 *
 * @author Adrian Witas
 */
public class MethodMatchImpl implements MethodMatch {

    private final JavaMethod method;
    private final MethodPattern pattern;

    public MethodMatchImpl(JavaMethod method, MethodPattern pattern) {
        this.method = method;
        this.pattern = pattern;
    }

    public JavaMethod getMethod() {
        return method;
    }

    public MethodPattern getPattern() {
        return pattern;
    }

}
