package org.abstractmeta.code.g.expression;

import org.abstractmeta.code.g.code.JavaMethod;

import java.util.List;
import java.util.Map;

/**
 * Matches all supplied method with the method group patterns
 *
 * @author Adrian Witas
 */
public interface MethodMatcher {

    /**
     * Matches given java methods with supplied method group expression.
     * @param methods java method
     * @param groupPattern group expression.
     * @return list of method group match
     */
    List<AbstractionMatch> match(List<JavaMethod> methods, AbstractionPattern groupPattern);

    /**
     * Indexes AbstractionMatch by name.
     * @param methodGroupMatch method group match
     * @return key value pair of group name, method group match
     */
    Map<String, AbstractionMatch> indexByName(List<AbstractionMatch> methodGroupMatch);
}
