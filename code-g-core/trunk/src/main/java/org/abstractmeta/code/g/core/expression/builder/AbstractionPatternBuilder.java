package org.abstractmeta.code.g.core.expression.builder;

import org.abstractmeta.code.g.core.expression.AbstractionPatternImpl;
import org.abstractmeta.code.g.expression.AbstractionPattern;
import org.abstractmeta.code.g.expression.MethodPattern;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Represents AbstractionPatternBuilder
 * <p>
 * </p>
 *
 * @author Adrian Witas
 */
public class AbstractionPatternBuilder {

    private final List<MethodPattern> methodPatterns = new ArrayList<MethodPattern>();
    private final List<MethodPatternBuilder> methodPatternBuilders = new ArrayList<MethodPatternBuilder>();


    public AbstractionPatternBuilder add(MethodPattern ... pattern) {
        Collections.addAll(methodPatterns, pattern);
        return this;
    }

    public AbstractionPatternBuilder add(Collection<MethodPattern> pattern) {
        methodPatterns.addAll(pattern);
        return this;
    }

    
    public MethodPatternBuilder add() {
        MethodPatternBuilder builder = new MethodPatternBuilder();
        methodPatternBuilders.add(builder);
        return builder;
    }

    public AbstractionPattern build() {
        for(MethodPatternBuilder builder: methodPatternBuilders) {
            methodPatterns.add(builder.build());
        }
        methodPatternBuilders.clear();
        return new AbstractionPatternImpl(methodPatterns);
    }

}
