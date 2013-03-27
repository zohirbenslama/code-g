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
import org.abstractmeta.code.g.core.code.builder.JavaFieldBuilder;
import org.abstractmeta.code.g.core.builder.handler.type.EqualMethodHandler;
import org.abstractmeta.code.g.core.builder.handler.type.HashCodeMethodHandler;
import org.abstractmeta.code.g.core.generator.ContextImpl;
import org.abstractmeta.code.g.core.provider.ClassTypeProvider;
import org.abstractmeta.code.g.core.util.JavaTypeCompiler;
import org.abstractmeta.code.g.core.util.ReflectUtil;
import org.abstractmeta.code.g.generator.Context;
import org.testng.Assert;
import org.testng.annotations.Test;

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
public class SimpleClassBuilderTest {

    private final Logger logger = Logger.getLogger(SimpleClassBuilderTest.class.getName());


    /**
     * This use case is to test setter, getter and constructor for immutable and non immutable fields
     * @throws Exception
     */
    public void testSimpleClass() throws Exception {

        JavaType iFoo  = new ClassTypeProvider(IFoo.class).get();
        SimpleClassBuilder builder = new SimpleClassBuilder("com.test.Foo", iFoo, new ContextImpl());
        builder.addModifiers(JavaModifier.PUBLIC);
        builder.addField(new JavaFieldBuilder().addModifiers(JavaModifier.PRIVATE).setType(int.class).setImmutable(true).setName("id"));
        builder.addField(new JavaFieldBuilder().addModifiers(JavaModifier.PRIVATE).setType(String.class).setName("name"));
        builder.addSuperInterfaces(IFoo.class);
        CompiledJavaType compiledJavaType = JavaTypeCompiler.compile(builder);
        logger.log(Level.FINE, "Generated " + compiledJavaType.getSourceCode());
        @SuppressWarnings("unchecked")
        Constructor constructor = compiledJavaType.getCompiledType().getConstructor(int.class);

        Object instance = constructor.newInstance(13);
        ReflectUtil.invokeMethod(instance, "setName", new Class[]{String.class}, "test");
        IFoo foo = IFoo.class.cast(instance);
        Assert.assertEquals(foo.getId(), 13);
        Assert.assertEquals(foo.getName(), "test");

    }


    /**
     * This use case is to test equals method
     * @throws Exception
     */
    public void testEqualMethodClass() throws Exception {

        JavaType iFoo  = new ClassTypeProvider(IFoo.class).get();
        Context context = new ContextImpl();
        EqualMethodHandler.Config config = new EqualMethodHandler.Config() {

            @Override
            public boolean isGenerateEqualMethod() {
                return true;
            }
        };
        context.put(EqualMethodHandler.Config.class, config);

        SimpleClassBuilder builder = new SimpleClassBuilder("com.test.Foo", iFoo, context);
        builder.addModifiers(JavaModifier.PUBLIC);
        builder.addField(new JavaFieldBuilder().addModifiers(JavaModifier.PRIVATE).setType(int.class).setImmutable(true).setName("id"));
        builder.addField(new JavaFieldBuilder().addModifiers(JavaModifier.PRIVATE).setType(String.class).setName("name"));
        builder.addSuperInterfaces(IFoo.class);
        CompiledJavaType compiledJavaType = JavaTypeCompiler.compile(builder);
        logger.log(Level.FINE, "Generated equal method" + compiledJavaType.getSourceCode());
        Assert.assertTrue(("" + compiledJavaType.getSourceCode()).contains("equals"));

        @SuppressWarnings("unchecked")
        Constructor constructor = compiledJavaType.getCompiledType().getConstructor(int.class);
        Object instance1 = constructor.newInstance(13);
        Object instance2 = constructor.newInstance(14);
        Object instance3 = constructor.newInstance(13);
        Assert.assertTrue(instance1.equals(instance3));
        Assert.assertFalse(instance1.equals(instance2));

    }


    /**
     * This use case is to test hashCode method
     * @throws Exception
     */
    public void testHashCodeMethodClass() throws Exception {
        JavaType iFoo  = new ClassTypeProvider(IFoo.class).get();
        Context context = new ContextImpl();
        HashCodeMethodHandler.Config config = new HashCodeMethodHandler.Config() {


            @Override
            public boolean isGenerateHashMethod() {
                return true;
            }

            @Override
            public int getHashMultiplier() {
                return 50;
            }

            @Override
            public String getIncludeInHashAnnotation() {
                return Id.class.getName();
            }
        };
        context.put(HashCodeMethodHandler.Config.class, config);

        SimpleClassBuilder builder = new SimpleClassBuilder("com.test.Foo", iFoo, context);
        builder.addModifiers(JavaModifier.PUBLIC);
        builder.addField(new JavaFieldBuilder().addModifiers(JavaModifier.PRIVATE).addAnnotations(new Id() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Id.class;
            }
        }).setType(int.class).setImmutable(true).setName("id"));
        builder.addField(new JavaFieldBuilder().addModifiers(JavaModifier.PRIVATE).setType(String.class).setName("name"));
        builder.addSuperInterfaces(IFoo.class);
        CompiledJavaType compiledJavaType = JavaTypeCompiler.compile(builder);
        logger.log(Level.FINE, "Generated hashCode method" + compiledJavaType.getSourceCode());
        Assert.assertTrue(("" + compiledJavaType.getSourceCode()).contains("hashCode"));
        Assert.assertTrue(("" + compiledJavaType.getSourceCode()).contains("result += 50 * this.id"));
        @SuppressWarnings("unchecked")
        Constructor constructor = compiledJavaType.getCompiledType().getConstructor(int.class);

        {
            IFoo foo = IFoo.class.cast(constructor.newInstance(1));
            Assert.assertEquals(foo.hashCode(), 50);
        }

        {
            IFoo foo = IFoo.class.cast(constructor.newInstance(2));
            Assert.assertEquals(foo.hashCode(), 100);
        }

    }






    public static interface IFoo {
        int getId();

        String getName();
    }


    public static @interface Id {

    }




}
