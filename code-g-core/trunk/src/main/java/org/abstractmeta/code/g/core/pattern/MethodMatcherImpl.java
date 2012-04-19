package org.abstractmeta.code.g.core.pattern;

import org.abstractmeta.code.g.code.JavaMethod;
import org.abstractmeta.code.g.core.collection.function.OperationNameMatchFunction;
import org.abstractmeta.code.g.core.collection.predicates.MethodMatchPredicate;
import org.abstractmeta.code.g.core.collection.predicates.OperationMatchPredicate;
import org.abstractmeta.code.g.core.pattern.builder.MethodGroupMatchBuilder;
import org.abstractmeta.code.g.core.util.StringUtil;
import com.google.common.collect.Iterables;
import org.abstractmeta.code.g.pattern.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Matches methods with given method pattern group.
 * <p><b>Usage</b>
 * <code><pre>
 *     MethodMatcher matcher = new MethodMatcherImpl();
 *     JavaType javaType = new ClassTypeProvider(Foo.class).get();
 *     List&lt;MethodGroupMatch> matches = matcher.match(javaType.getMethods(), MethodGroupPatterns.ACCESSOR_MUTATOR_PATTERN);
 * </pre></code>
 * </p>
 *
 * @author Adrian Witas
 */
public class MethodMatcherImpl implements MethodMatcher {


    /**
     * Matches a given method list with a given method group pattern
     *
     * @param methods      java method
     * @param groupPattern group pattern.
     * @return
     */
    @Override
    public List<MethodGroupMatch> match(List<JavaMethod> methods, MethodGroupPattern groupPattern) {
        List<MethodGroupMatch> result = new ArrayList<MethodGroupMatch>();
        Map<String, MethodGroupMatchBuilder> methodMatchBuilders = new HashMap<String, MethodGroupMatchBuilder>();
        for (MethodPattern pattern : groupPattern) {
            for (JavaMethod matchedMethod : Iterables.filter(methods, new MethodMatchPredicate(pattern))) {
                buildMatchDetails(pattern, matchedMethod, methodMatchBuilders);
            }
        }
        matchSingularGroups(methodMatchBuilders);

        for (String name : methodMatchBuilders.keySet()) {
            MethodGroupMatchBuilder builder = methodMatchBuilders.get(name);
            result.add(builder.build());
        }
        return result;
    }


    /**
     * Builds match details
     *
     * @param pattern
     * @param methodCandidate
     * @param methodMatchBuilders
     */
    protected void buildMatchDetails(MethodPattern pattern, JavaMethod methodCandidate, Map<String, MethodGroupMatchBuilder> methodMatchBuilders) {
        String methodName = methodCandidate.getName();
        List<String> operationName = pattern.getOperationNames();
        int indexOf = Iterables.indexOf(operationName, new OperationMatchPredicate(methodName));
        String groupName = new OperationNameMatchFunction(methodName).apply(operationName.get(indexOf));
        MethodGroupMatchBuilder builder = getBuilder(methodMatchBuilders, groupName);
        builder.setName(groupName);
        builder.addMatches().setMethod(methodCandidate).setPattern(pattern);
    }



    protected MethodGroupMatchBuilder getBuilder(Map<String, MethodGroupMatchBuilder> builders, String name) {
        MethodGroupMatchBuilder builder = builders.get(name);
        if (builder == null) {
            builder = new MethodGroupMatchBuilder();
            builders.put(name, builder);
        }
        return builder;
    }


    protected void matchSingularGroups(Map<String, MethodGroupMatchBuilder> methodMatchBuilders) {
        List<String> singularNames = new ArrayList<String>();
        for (String matchName : methodMatchBuilders.keySet()) {
            MethodGroupMatchBuilder builder = methodMatchBuilders.get(matchName);
            for (MethodMatch methodMatch : builder.build().getMatches()) {
                if (methodMatch.getPattern().isSingularNameMatching()) {
                    String pluralName = StringUtil.getPlural(matchName);
                    MethodGroupMatchBuilder pluralMatch = methodMatchBuilders.get(pluralName);
                    if (pluralMatch == null) continue;
                    pluralMatch.addMatches(builder.build().getMatches());
                    singularNames.add(matchName);
                }
            }
        }
        for (String singularName : singularNames) {
            methodMatchBuilders.remove(singularName);
        }
    }

    @Override
    public Map<String, MethodGroupMatch> indexByName(List<MethodGroupMatch> methodGroupMatch) {
        Map<String, MethodGroupMatch> result = new HashMap<String, MethodGroupMatch>();
        for (MethodGroupMatch groupMatch : methodGroupMatch) {
            result.put(groupMatch.getName(), groupMatch);
        }
        return result;
    }


}
