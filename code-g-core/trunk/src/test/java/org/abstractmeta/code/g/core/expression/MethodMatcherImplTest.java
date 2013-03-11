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
import org.abstractmeta.code.g.code.JavaType;
import org.abstractmeta.code.g.core.helper.iface.MessageWithCollection;
import org.abstractmeta.code.g.core.provider.ClassTypeProvider;
import org.abstractmeta.code.g.expression.AbstractionMatch;
import org.abstractmeta.code.g.expression.MethodMatch;
import org.abstractmeta.code.g.expression.MethodMatcher;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Test
public class MethodMatcherImplTest {


    public void testMethodMatcherImpl() {

        MethodMatcher matcher = new MethodMatcherImpl();
        JavaType javaType = new ClassTypeProvider(Foo.class).get();
        List<AbstractionMatch> matches = matcher.match(javaType.getMethods(), AbstractionPatterns.ACCESSOR_MUTATOR_PATTERN);
        Map<String, List<JavaMethod>> namedMatches = new HashMap<String, List<JavaMethod>>();
        for (AbstractionMatch match : matches) {
            List<JavaMethod> methods = new ArrayList<JavaMethod>();
            namedMatches.put(match.getName(), methods);
            for (MethodMatch methodMatch : match.getMatches()) {
                methods.add(methodMatch.getMethod());
            }
        }
        Assert.assertTrue(matches.get(0).containsMatch("get"));
        Assert.assertEquals(namedMatches.size(), 4);
        Assert.assertEquals(namedMatches.get("Id").size(), 2);
        Assert.assertEquals(namedMatches.get("Name").size(), 2);
        Assert.assertEquals(namedMatches.get("Enabled").size(), 2);
        Assert.assertEquals(namedMatches.get("Flags").size(), 1);

    }


    public void testMethodMatcherWithSingularNameMatchingImpl() {

        MethodMatcher matcher = new MethodMatcherImpl();
        JavaType javaType = new ClassTypeProvider(MessageWithCollection.class).get();
        List<AbstractionMatch> matches = matcher.match(javaType.getMethods(), AbstractionPatterns.ACCESSOR_MUTATOR_PATTERN);
        Map<String, AbstractionMatch> namedMatches = new HashMap<String, AbstractionMatch>();
        for (AbstractionMatch match : matches) {
            namedMatches.put(match.getName(), match);
         
        }
        Assert.assertTrue(namedMatches.containsKey("Bars"));
        Assert.assertTrue(namedMatches.containsKey("Foos"));
        AbstractionMatch groupMatch = namedMatches.get("Bars");
        Assert.assertTrue(groupMatch.containsMatch("add", Object.class));
        Assert.assertTrue(groupMatch.containsMatch("get"));
        
    }


    public void testMethodGroupNameMatcherImpl() {
        MethodMatcher matcher = new MethodMatcherImpl();
        JavaType javaType = new ClassTypeProvider(Bar.class).get();
        List<AbstractionMatch> matches = matcher.match(javaType.getMethods(), AbstractionPatterns.REGISTRY_PATTERN);
        Map<String, List<JavaMethod>> namedMatches = new HashMap<String, List<JavaMethod>>();
        for (AbstractionMatch match : matches) {
            List<JavaMethod> methods = new ArrayList<JavaMethod>();
            namedMatches.put(match.getName(), methods);
            for (MethodMatch methodMatch : match.getMatches()) {
                methods.add(methodMatch.getMethod());
            }
        }

        Assert.assertEquals(matches.size(), 1);
        Assert.assertEquals(matches.get(0).getMatches().size(), 3);

    }


    public static class Bar {
        private Map<String, Foo> fooRegistry;

        public void registerFoo(Foo foo) {
            fooRegistry.put(foo.getName(), foo);
        }

        public Foo getFoo(String name) {
            return fooRegistry.get(name);
        }

        public boolean isFooRegistered(String name) {
            return fooRegistry.containsKey(name);
        }

    }

    public static interface Foo {



        public int getId();

        public void setId(int id);

        public String getName();


        public void setName(String name);

        public int[] getFlags() ;

        public boolean isEnabled();

        public void setEnabled(boolean enabled) ;

        public void getF(String i);
    }
}
