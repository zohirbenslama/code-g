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
import org.abstractmeta.code.g.config.SourceMatcher;
import org.abstractmeta.code.g.core.code.JavaTypeRegistryImpl;
import org.abstractmeta.code.g.core.config.DescriptorImpl;
import org.abstractmeta.code.g.core.config.SourceMatcherImpl;
import org.abstractmeta.code.g.core.util.ReflectUtil;
import org.abstractmeta.code.g.generator.CodeGenerator;
import org.abstractmeta.code.g.generator.Context;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author Adrian Witas
 */
@Test
public class ClassGeneratorTest {



    public void testSimpleClassGenerator() throws Exception {
        CodeGenerator classGenerator = new ClassGenerator();
        Context context = getContextForTestSimpleClassGenerator();
        List<CompiledJavaType> result = classGenerator.generate(context);
        Assert.assertEquals(result.size(), 1);
        Class type = result.get(0).getCompiledType();
        @SuppressWarnings("unchecked")
        Foo foo = (Foo)type.getConstructor(int.class).newInstance(12);
        Assert.assertEquals(foo.getId(), 12);
    }


    public void testSimpleClassWithBuilderGenerator() throws Exception {
        CodeGenerator classGenerator = new ClassGenerator();
        Context context = getContextForTestSimpleClassGenerator();
        context.get(Descriptor.class).getProperties().setProperty("generateBuilder", "true");
        List<CompiledJavaType> result = classGenerator.generate(context);
        Assert.assertEquals(result.size(), 2);
        Class builderType = result.get(1).getCompiledType();
        @SuppressWarnings("unchecked")
        Object builder = ReflectUtil.getInstance(builderType);
        ReflectUtil.setFieldValue(builder, "id", 12);
        Foo foo = (Foo)ReflectUtil.invokeMethod(builder, "build", new Class[0]);
        Assert.assertEquals(foo.getId(), 12);
    }


    protected  Context  getContextForTestSimpleClassGenerator(){
        DescriptorImpl descriptor = new DescriptorImpl();
        SourceMatcherImpl sourceMatcher = new SourceMatcherImpl();
        sourceMatcher.setSourceDirectory(new File("src/test/java").getAbsolutePath());
        sourceMatcher.setClassNames(Arrays.asList(Foo.class.getName()));
        descriptor.setSourceMatcher(sourceMatcher);
        Properties properties = new Properties();
        descriptor.setProperties(properties);
        Context context = new ContextImpl();
        context.put(Descriptor.class, descriptor);
        context.put(JavaTypeRegistry.class, new JavaTypeRegistryImpl());
        return context;
    }

    public static interface Foo {

        int getId();

    }

}
