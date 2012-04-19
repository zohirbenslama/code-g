package org.abstractmeta.code.g.common.provider;

import org.abstractmeta.code.g.code.JavaMethod;
import org.abstractmeta.code.g.code.JavaType;
import org.abstractmeta.code.g.core.provider.ClassTypeProvider;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;


@Test
public class ClassTypeProviderTest {


    public void testClassTypeProvider() {

        JavaType type = new ClassTypeProvider(Foo.class).get();
        Assert.assertEquals(type.getMethods().size(), 3);
        Map<String, JavaMethod> methods = new HashMap<String, JavaMethod>();
        for (JavaMethod method : type.getMethods()) {
            methods.put(method.getName(), method);
        }
        Assert.assertTrue(methods.containsKey("getName"));
        Assert.assertTrue(methods.containsKey("setName"));
        Assert.assertTrue(methods.containsKey("getId"));
    }

    static interface Foo {

        int getId();

        String getName();

        void setName(String name);

    }

}
