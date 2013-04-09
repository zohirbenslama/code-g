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
package org.abstractmeta.code.g.core.builder;

import org.abstractmeta.code.g.code.CompiledJavaType;
import org.abstractmeta.code.g.code.JavaModifier;
import org.abstractmeta.code.g.code.JavaType;
import org.abstractmeta.code.g.code.JavaTypeBuilder;
import org.abstractmeta.code.g.core.builder.handler.type.EqualMethodHandler;
import org.abstractmeta.code.g.core.builder.handler.type.HashCodeMethodHandler;
import org.abstractmeta.code.g.core.code.builder.JavaFieldBuilder;
import org.abstractmeta.code.g.core.generator.ContextImpl;
import org.abstractmeta.code.g.core.provider.ClassTypeProvider;
import org.abstractmeta.code.g.core.util.JavaTypeCompiler;
import org.abstractmeta.code.g.core.util.JavaTypeUtil;
import org.abstractmeta.code.g.core.util.ReflectUtil;
import org.abstractmeta.code.g.generator.Context;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * SimpleClassBuilder use cases tests.
 *
 * @author Adrian Witas
 */
@Test
public class BuilderClassBuilderTest {

    private final Logger logger = Logger.getLogger(BuilderClassBuilderTest.class.getName());


    /**
     * This use case is to test setter, getter and constructor for immutable and non immutable fields
     *
     * @throws Exception
     */
    public void testSimpleClass() throws Exception {
        File compilationOutputFile = JavaTypeUtil.getCompiledFileOutputTempDirectory();
        JavaTypeBuilder sourceType = getSourceType();
        CompiledJavaType compiledSourceJavaType = JavaTypeCompiler.compile(sourceType, compilationOutputFile);
        compiledSourceJavaType.getClassLoader().loadClass("com.test.Foo");
        BuilderClassBuilder builder = new BuilderClassBuilder("com.test.builder.FooBuilder", sourceType.build(), new ContextImpl());
        builder.addModifiers(JavaModifier.PUBLIC);
        builder.addField(new JavaFieldBuilder().addModifiers(JavaModifier.PRIVATE).setType(int.class).setName("id"));
        builder.addField(new JavaFieldBuilder().addModifiers(JavaModifier.PRIVATE).setType(String.class).setName("name"));
        builder.addField(new JavaFieldBuilder().addModifiers(JavaModifier.PRIVATE).setType(boolean.class).setName("flagPresent"));
        builder.addField(new JavaFieldBuilder().addModifiers(JavaModifier.PRIVATE).setType(Boolean.class).setName("flag"));


        CompiledJavaType compiledJavaType = JavaTypeCompiler.compile(builder, compiledSourceJavaType.getClassLoader(), compilationOutputFile);

        logger.log(Level.FINE, "Generated " + compiledJavaType.getSourceCode());

        @SuppressWarnings("unchecked")
        Object instance = ReflectUtil.newInstance(compiledJavaType.getCompiledType());
        ReflectUtil.invokeMethod(instance, "setName", new Class[]{String.class}, "test");
        ReflectUtil.invokeMethod(instance, "setId", new Class[]{int.class}, 13);
        Object builtFoo = ReflectUtil.invokeMethod(instance, "build", new Class[]{});
        IFoo foo = IFoo.class.cast(builtFoo);
        Assert.assertEquals(foo.getName(), "test");
    }

    protected JavaTypeBuilder getSourceType() {
        JavaType result = new ClassTypeProvider(IFoo.class).get();
        SimpleClassBuilder builder = new SimpleClassBuilder("com.test.Foo", result, new ContextImpl());
        builder.addModifiers(JavaModifier.PUBLIC);
        builder.addField(new JavaFieldBuilder().addModifiers(JavaModifier.PRIVATE).setType(int.class).setImmutable(true).setName("id"));
        builder.addField(new JavaFieldBuilder().addModifiers(JavaModifier.PRIVATE).setType(String.class).setImmutable(true).setName("name"));
        builder.addField(new JavaFieldBuilder().addModifiers(JavaModifier.PRIVATE).setType(Boolean.class).setName("flag"));
        builder.addSuperInterfaces(IFoo.class);
        return builder;
    }


    public static interface IFoo {

        int getId();

        String getName();

        Boolean getFlag();

        void setFlag(Boolean flag);

    }


}
