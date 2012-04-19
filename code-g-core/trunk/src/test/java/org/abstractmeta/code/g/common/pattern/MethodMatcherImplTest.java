package org.abstractmeta.code.g.common.pattern;


import org.abstractmeta.code.g.code.JavaMethod;
import org.abstractmeta.code.g.code.JavaType;
import org.abstractmeta.code.g.common.generator.test.CollectionTest;
import org.abstractmeta.code.g.core.pattern.MethodGroupPatterns;
import org.abstractmeta.code.g.core.pattern.MethodMatcherImpl;
import org.abstractmeta.code.g.core.provider.ClassTypeProvider;
import org.abstractmeta.code.g.pattern.MethodGroupMatch;
import org.abstractmeta.code.g.pattern.MethodMatch;
import org.abstractmeta.code.g.pattern.MethodMatcher;
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
        List<MethodGroupMatch> matches = matcher.match(javaType.getMethods(), MethodGroupPatterns.ACCESSOR_MUTATOR_PATTERN);
        Map<String, List<JavaMethod>> namedMatches = new HashMap<String, List<JavaMethod>>();
        for (MethodGroupMatch match : matches) {
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
        JavaType javaType = new ClassTypeProvider(CollectionTest.class).get();
        List<MethodGroupMatch> matches = matcher.match(javaType.getMethods(), MethodGroupPatterns.ACCESSOR_MUTATOR_PATTERN);
        Map<String, MethodGroupMatch> namedMatches = new HashMap<String, MethodGroupMatch>();
        for (MethodGroupMatch match : matches) {
            namedMatches.put(match.getName(), match);
         
        }
        Assert.assertTrue(namedMatches.containsKey("Bars"));
        Assert.assertTrue(namedMatches.containsKey("Foos"));
        MethodGroupMatch groupMatch = namedMatches.get("Bars");
        Assert.assertTrue(groupMatch.containsMatch("add", Object.class));
        Assert.assertTrue(groupMatch.containsMatch("get"));
        
    }


    public void testMethodGroupNameMatcherImpl() {
        MethodMatcher matcher = new MethodMatcherImpl();
        JavaType javaType = new ClassTypeProvider(Bar.class).get();
        List<MethodGroupMatch> matches = matcher.match(javaType.getMethods(), MethodGroupPatterns.REGISTRY_PATTERN);
        Map<String, List<JavaMethod>> namedMatches = new HashMap<String, List<JavaMethod>>();
        for (MethodGroupMatch match : matches) {
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
