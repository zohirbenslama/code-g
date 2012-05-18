package org.abstractmeta.code.g.core.expression;

import org.abstractmeta.code.g.expression.AbstractionMatch;
import org.abstractmeta.code.g.expression.MethodMatch;

import java.util.List;


/**
 * Method Group Match.
 * All method matched with a given expression.
 *
 * @author Adrian Witas
 */
public class AbstractionMatchImpl implements AbstractionMatch {

    private final String name;
    private final List<MethodMatch> matches;

    public AbstractionMatchImpl(String name, List<MethodMatch> matches) {
        this.name = name;
        this.matches = matches;
    }

    public String getName() {
        return name;
    }

    public List<MethodMatch> getMatches() {
        return matches;
    }

    @Override
    public boolean containsMatch(String operationName, Class... superParameterTypes) {
        return getMatch(operationName, superParameterTypes) != null;
    }

    @Override
    public MethodMatch getMatch(String operationName, Class... superParameterTypes) {
        for (MethodMatch match : matches) {
            if (match.getPattern().getOperationNames().contains(operationName)) {
                List<Class> patternBaseParameterTypes = match.getPattern().getBaseParameterTypes();
                if (superParameterTypes.length != patternBaseParameterTypes.size()) {
                    continue;
                }
                for (int i = 0; i < superParameterTypes.length; i++) {
                    Class superParameterType = superParameterTypes[0];
                    if (!superParameterType.isAssignableFrom(patternBaseParameterTypes.get(i))) {
                        continue;
                    }
                }
                return match;
            }
        }
        return null;
    }

}
