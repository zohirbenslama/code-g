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
package org.abstractmeta.code.g.core.generator;

import org.abstractmeta.code.g.code.CompiledJavaType;
import org.abstractmeta.code.g.code.JavaTypeRegistry;
import org.abstractmeta.code.g.config.Descriptor;
import org.abstractmeta.code.g.core.code.JavaTypeRegistryImpl;
import org.abstractmeta.code.g.core.config.DescriptorImpl;
import org.abstractmeta.code.g.core.config.SourceMatcherImpl;
import org.abstractmeta.code.g.core.util.ReflectUtil;
import org.abstractmeta.code.g.generator.Context;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * @author Adrian Witas
 */
@Test
public class BuilderGeneratorTest {


    public void testSimpleClassWithBuilderGenerator() throws Exception {
        BuilderGenerator classGenerator = new BuilderGenerator();
        Context context = getContextForTestSimpleClassGenerator();
        List<CompiledJavaType> result = classGenerator.generate(context);
        Assert.assertEquals(result.size(), 1);
        Class builderType = result.get(0).getCompiledType();
        @SuppressWarnings("unchecked")
        Object builder = ReflectUtil.getInstance(builderType);
        ReflectUtil.setFieldValue(builder, "field1", 12);
        A a = (A) ReflectUtil.invokeMethod(builder, "build", new Class[0]);
        Assert.assertEquals(a.getField1(), 12);
    }


    protected Context getContextForTestSimpleClassGenerator() {
        DescriptorImpl descriptor = new DescriptorImpl();
        SourceMatcherImpl sourceMatcher = new SourceMatcherImpl();
        sourceMatcher.setSourceDirectory(new File("src/test/java").getAbsolutePath());
        sourceMatcher.setClassNames(Arrays.asList(A.class.getName()));
        descriptor.setSourceMatcher(sourceMatcher);
        Properties properties = new Properties();
        descriptor.setProperties(properties);
        Context context = new ContextImpl();
        context.put(Descriptor.class, descriptor);
        context.put(JavaTypeRegistry.class, new JavaTypeRegistryImpl());
        return context;
    }

    public static class A {

        private final int field1;

        private final String field2;

        private transient Long field3;

        public A(int field1, String field2) {
            this.field1 = field1;
            this.field2 = field2;
        }

        public Long getField3() {
            return field3;
        }

        public int getField1() {
            return field1;
        }

        public String getField2() {
            return field2;
        }

        public void setField3(Long field3) {
            this.field3 = field3;
        }
    }

}
