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
package org.abstractmeta.code.g.core.provider;

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
