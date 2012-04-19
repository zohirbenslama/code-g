package org.abstractmeta.code.g.pattern;

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
     * Matches given java methods with supplied method group pattern.
     * @param methods java method
     * @param groupPattern group pattern.
     * @return list of method group match
     */
    List<MethodGroupMatch> match(List<JavaMethod> methods, MethodGroupPattern groupPattern);

    /**
     * Indexes MethodGroupMatch by name.
     * @param methodGroupMatch method group match
     * @return key value pair of group name, method group match
     */
    Map<String, MethodGroupMatch> indexByName(List<MethodGroupMatch> methodGroupMatch);
}
