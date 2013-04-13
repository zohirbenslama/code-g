/**
 * Copyright 2011 Adrian Witas
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.abstractmeta.code.g.core.expression;

import org.abstractmeta.code.g.code.JavaMethod;
import org.abstractmeta.code.g.core.collection.function.OperationNameMatchFunction;
import org.abstractmeta.code.g.core.collection.predicate.MethodMatchPredicate;
import org.abstractmeta.code.g.core.collection.predicate.OperationMatchPredicate;
import org.abstractmeta.code.g.core.expression.builder.AbstractionMatchBuilder;
import org.abstractmeta.code.g.core.util.CodeGeneratorUtil;
import com.google.common.collect.Iterables;
import org.abstractmeta.code.g.expression.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Matches methods with given method expression group.
 * <p><b>Usage</b>
 * <code><pre>
 *     MethodMatcher matcher = new MethodMatcherImpl();
 *     JavaType javaType = new ClassTypeProvider(Bar.class).get();
 *     List&lt;AbstractionMatch> matches = matcher.match(javaType.getMethods(), AbstractionPatterns.ACCESSOR_MUTATOR_PATTERN);
 * </pre></code>
 * </p>
 *
 * @author Adrian Witas
 */
public class MethodMatcherImpl implements MethodMatcher {


    /**
     * Matches a given method list with a given method group expression
     *
     * @param methods      java method
     * @param groupPattern group expression.
     * @return
     */
    @Override
    public List<AbstractionMatch> match(List<JavaMethod> methods, AbstractionPattern groupPattern) {
        List<AbstractionMatch> result = new ArrayList<AbstractionMatch>();
        Map<String, AbstractionMatchBuilder> methodMatchBuilders = new HashMap<String, AbstractionMatchBuilder>();
        for (MethodPattern pattern : groupPattern) {
            for (JavaMethod matchedMethod : Iterables.filter(methods, new MethodMatchPredicate(pattern))) {
                buildMatchDetails(pattern, matchedMethod, methodMatchBuilders);
            }
        }
        matchSingularGroups(methodMatchBuilders);

        for (String name : methodMatchBuilders.keySet()) {
            AbstractionMatchBuilder builder = methodMatchBuilders.get(name);
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
    protected void buildMatchDetails(MethodPattern pattern, JavaMethod methodCandidate, Map<String, AbstractionMatchBuilder> methodMatchBuilders) {
        String methodName = methodCandidate.getName();
        List<String> operationName = pattern.getOperationNames();
        int indexOf = Iterables.indexOf(operationName, new OperationMatchPredicate(methodName));
        String groupName = new OperationNameMatchFunction(methodName).apply(operationName.get(indexOf));
        AbstractionMatchBuilder builder = getBuilder(methodMatchBuilders, groupName);
        builder.setName(groupName);
        builder.addMatches().setMethod(methodCandidate).setPattern(pattern);
    }



    protected AbstractionMatchBuilder getBuilder(Map<String, AbstractionMatchBuilder> builders, String name) {
        AbstractionMatchBuilder builder = builders.get(name);
        if (builder == null) {
            builder = new AbstractionMatchBuilder();
            builders.put(name, builder);
        }
        return builder;
    }


    protected void matchSingularGroups(Map<String, AbstractionMatchBuilder> methodMatchBuilders) {
        List<String> singularNames = new ArrayList<String>();
        for (String matchName : methodMatchBuilders.keySet()) {
            AbstractionMatchBuilder builder = methodMatchBuilders.get(matchName);
            for (MethodMatch methodMatch : builder.build().getMatches()) {
                if (methodMatch.getPattern().isSingularNameMatching()) {
                    String pluralName = CodeGeneratorUtil.getPlural(matchName);
                    AbstractionMatchBuilder pluralMatch = methodMatchBuilders.get(pluralName);
                    if (pluralMatch == null) continue;
                    pluralMatch.addMatches(builder.getMatches());
                    singularNames.add(matchName);
                }
            }
        }
        for (String singularName : singularNames) {
            methodMatchBuilders.remove(singularName);
        }
    }

    @Override
    public Map<String, AbstractionMatch> indexByName(List<AbstractionMatch> methodGroupMatch) {
        Map<String, AbstractionMatch> result = new HashMap<String, AbstractionMatch>();
        for (AbstractionMatch groupMatch : methodGroupMatch) {
            result.put(groupMatch.getName(), groupMatch);
        }
        return result;
    }


}
