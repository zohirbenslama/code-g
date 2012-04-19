package org.abstractmeta.code.g.core.pattern.builder;

import org.abstractmeta.code.g.core.pattern.MethodGroupPatternImpl;
import org.abstractmeta.code.g.pattern.MethodGroupPattern;
import org.abstractmeta.code.g.pattern.MethodPattern;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Represents MethodGroupPatternBuilder
 * <p>
 * </p>
 *
 * @author Adrian Witas
 */
public class MethodGroupPatternBuilder {

    private final List<MethodPattern> methodPatterns = new ArrayList<MethodPattern>();
    private final List<MethodPatternBuilder> methodPatternBuilders = new ArrayList<MethodPatternBuilder>();


    public MethodGroupPatternBuilder add(MethodPattern ... pattern) {
        Collections.addAll(methodPatterns, pattern);
        return this;
    }

    public MethodGroupPatternBuilder add(Collection<MethodPattern> pattern) {
        methodPatterns.addAll(pattern);
        return this;
    }

    
    public MethodPatternBuilder add() {
        MethodPatternBuilder builder = new MethodPatternBuilder();
        methodPatternBuilders.add(builder);
        return builder;
    }

    public MethodGroupPattern build() {
        for(MethodPatternBuilder builder: methodPatternBuilders) {
            methodPatterns.add(builder.build());
        }
        methodPatternBuilders.clear();
        return new MethodGroupPatternImpl(methodPatterns);
    }

}
