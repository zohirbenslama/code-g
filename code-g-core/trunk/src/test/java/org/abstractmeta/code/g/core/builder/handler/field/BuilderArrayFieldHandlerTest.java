package org.abstractmeta.code.g.core.builder.handler.field;

import org.abstractmeta.code.g.code.*;
import org.abstractmeta.code.g.core.annotation.SuppressWarningsBuilder;
import org.abstractmeta.code.g.core.builder.BuilderClassBuilder;
import org.abstractmeta.code.g.core.builder.BuilderClassBuilderTest;
import org.abstractmeta.code.g.core.builder.SimpleClassBuilder;
import org.abstractmeta.code.g.core.code.builder.JavaFieldBuilder;
import org.abstractmeta.code.g.core.extractor.AccessorFieldExtractor;
import org.abstractmeta.code.g.core.generator.ContextImpl;
import org.abstractmeta.code.g.core.internal.ParameterizedTypeImpl;
import org.abstractmeta.code.g.core.internal.TypeVariableImpl;
import org.abstractmeta.code.g.core.provider.ClassTypeProvider;
import org.abstractmeta.code.g.core.util.JavaTypeCompiler;
import org.abstractmeta.code.g.core.util.JavaTypeUtil;
import org.abstractmeta.code.g.core.util.ReflectUtil;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Provides test with handler test
 *
 * @author Adrian Witas
 */
@Test
public class BuilderArrayFieldHandlerTest {

    private final Logger logger = Logger.getLogger(BuilderClassBuilderTest.class.getName());




    /**
     * This use case is to test setter, getter and constructor for immutable and non immutable fields
     *
     * @throws Exception
     */
    public void testArrayFiledHandler() throws Exception {
        File compilationOutputFile = JavaTypeUtil.getCompiledFileOutputTempDirectory();
        JavaTypeBuilder sourceType = getSourceType();
        CompiledJavaType compiledSourceJavaType = JavaTypeCompiler.compile(sourceType, compilationOutputFile);
        compiledSourceJavaType.getClassLoader().loadClass("com.test.Foo");

        BuilderClassBuilder builder = new BuilderClassBuilder("com.test.builder.FooBuilder", sourceType.build(), new ContextImpl());

        builder.addAnnotations(new SuppressWarningsBuilder().addValue("unchecked").build());
        builder.addModifiers(JavaModifier.PUBLIC);

        builder.addField(new JavaFieldBuilder().addModifiers(JavaModifier.PRIVATE).setType(boolean.class).setName("idsPresent"));
        for(JavaField field: sourceType.getFields()) {
            builder.addField(new JavaFieldBuilder().addModifiers(JavaModifier.PRIVATE).setType(field.getType()).setName(field.getName()));

        }

        CompiledJavaType compiledJavaType = JavaTypeCompiler.compile(builder, compiledSourceJavaType.getClassLoader(), compilationOutputFile);

        logger.log(Level.FINE, "Generated " + compiledJavaType.getSourceCode());

        @SuppressWarnings("unchecked")
        Object instance = ReflectUtil.newInstance(compiledJavaType.getCompiledType());


        ReflectUtil.invokeMethod(instance, "addNames", new Class[]{String[].class},new Object[]{new String[]{"a1", "a2"}});
        ReflectUtil.invokeMethod(instance, "addIds", new Class[]{int[].class}, new Object[]{new int[]{1,2,3}});
        Object builtFoo = ReflectUtil.invokeMethod(instance, "build", new Class[]{});
        IFoo foo = IFoo.class.cast(builtFoo);
        Assert.assertEquals(foo.getNames(), new String[]{"a1", "a2"});
        Assert.assertTrue(Arrays.equals(foo.getIds(), new int[]{1, 2, 3}));

    }


    protected JavaTypeBuilder getSourceType() {
        JavaType sourceType = new ClassTypeProvider(IFoo.class).get();
        AccessorFieldExtractor fieldExtractor = new AccessorFieldExtractor();
        SimpleClassBuilder builder = new SimpleClassBuilder("com.test.Foo", sourceType, new ContextImpl());
       // builder.addGenericTypeVariable("T", String.class);
        builder.addModifiers(JavaModifier.PUBLIC);
        for(JavaField field: fieldExtractor.extract(sourceType, new ContextImpl())) {
            builder.addField(new JavaFieldBuilder().addModifiers(JavaModifier.PRIVATE).setImmutable(field.isImmutable()).setType(field.getType()).setName(field.getName()));
        }
        builder.addSuperInterfaces(new ParameterizedTypeImpl(null, IFoo.class, new TypeVariableImpl("T", IFoo.class)));
        return builder;
    }


    public static interface IFoo<T> {

        int [] getIds();

        String [] getNames();

        Entry<T> getEntries();


    }

    public static interface Entry<T>  {
        T getT();
    }

}
