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
package org.abstractmeta.code.g.core.invocation;

import org.abstractmeta.code.g.core.invocation.builder.InvocationParameterBuilder;
import org.abstractmeta.code.g.core.util.ReflectUtil;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Test
public class InvocationParserTest {


    public void testParse() {
        InvocationParser parser = new InvocationParser();
        List<InvocationParameter> parameters = Arrays.asList(
                new InvocationParameterBuilder()
                        .setName("ownerTypeLiteral")
                        .setValue(InvocationParserTest.class.getName() + ".class")
                        .setType(InvocationParserTest.class)
                        .build()
        );
        InvocationMeta invocationMeta = parser.parse("org.abstract.Foo<java.util.Map<java.lang.String, java.lang.Integer>>(ownerTypeLiteral)", parameters);
        Assert.assertEquals(invocationMeta.getInvocationType(), InvocationMeta.InvocationType.CONSTRUCTOR);
        Assert.assertEquals(invocationMeta.getConstructorName(), "Foo");
        Assert.assertEquals(invocationMeta.getOwnerType(), "org.abstract.Foo");

        Assert.assertEquals(invocationMeta.getParameters(), Arrays.asList("org.abstractmeta.code.g.core.invocation.InvocationParserTest.class"));
        Type mapType = invocationMeta.getParameterizedTypeArguments().get(0);
        Assert.assertEquals(ReflectUtil.getRawClass(mapType), Map.class);

    }

    public void testParseStaticMethod() {
        InvocationParser parser = new InvocationParser();
        List<InvocationParameter> parameters = Arrays.asList(
                new InvocationParameterBuilder()
                        .setName("ownerTypeLiteral")
                        .setValue(InvocationParserTest.class.getName() + ".class")
                        .setType(InvocationParserTest.class)
                        .build()
        );
        {
            InvocationMeta invocationMeta = parser.parse("org.abstract.Foo.call(ownerTypeLiteral)", parameters);
            Assert.assertEquals(invocationMeta.getInvocationType(), InvocationMeta.InvocationType.METHOD);
            Assert.assertEquals(invocationMeta.getParameters(), Arrays.asList("org.abstractmeta.code.g.core.invocation.InvocationParserTest.class"));
            Assert.assertEquals(invocationMeta.getMethodName(), "org.abstract.Foo.call");
        }
        {
            InvocationMeta invocationMeta = parser.parse("call(ownerTypeLiteral)", parameters);
            Assert.assertEquals(invocationMeta.getInvocationType(), InvocationMeta.InvocationType.METHOD);
            Assert.assertEquals(invocationMeta.getParameters(), Arrays.asList("org.abstractmeta.code.g.core.invocation.InvocationParserTest.class"));
            Assert.assertEquals(invocationMeta.getMethodName(), "call");
        }
    }

}
