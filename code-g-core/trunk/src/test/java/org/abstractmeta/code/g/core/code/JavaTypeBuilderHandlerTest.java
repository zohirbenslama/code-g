package org.abstractmeta.code.g.core.code;

import org.abstractmeta.code.g.code.JavaModifier;
import org.abstractmeta.code.g.code.JavaTypeBuilder;
import org.abstractmeta.code.g.core.code.builder.JavaFieldBuilder;
import org.abstractmeta.code.g.core.code.builder.JavaTypeBuilderImpl;
import org.abstractmeta.code.g.core.builder.handler.field.SetterFieldHandler;
import org.abstractmeta.code.g.core.renderer.TypeRenderer;
import org.abstractmeta.code.g.core.util.ReflectUtil;
import org.abstractmeta.code.g.renderer.JavaTypeRenderer;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Represents JavaTypeBuilderHandlerTest
 *
 * @author Adrian Witas
 */
@Test
public class JavaTypeBuilderHandlerTest {




    @SuppressWarnings("unchecked")
    /**
     * This test is to check a builder with field handler.
     * After adding a field a field handler is notified, so it can generate a setter method on a given field.
     */
    public void testJavaTypeBuilderWithFieldHandler() {

        JavaTypeBuilder builder = new JavaTypeBuilderImpl("com.test.Foo");
        builder.addModifiers(JavaModifier.PUBLIC);
        builder.addFieldHandlers(new SetterFieldHandler());
        builder.addField(new JavaFieldBuilder().setName("field1").setType(String.class).addModifiers(JavaModifier.PRIVATE).build());
        JavaTypeRenderer renderer = new TypeRenderer();
        String code = renderer.render(builder.build(), builder.getImporter(), 4);
        Class fooClass = ReflectUtil.compileSource("com.test.Foo", code);
        Assert.assertEquals(fooClass.getName(), "com.test.Foo");
        Object fooInstance = ReflectUtil.getInstance(fooClass);
        ReflectUtil.invokeMethod(fooInstance, "setField1", new Class[]{String.class}, "abc");
        Assert.assertEquals(ReflectUtil.getFieldValue(String.class, fooInstance, "field1"), "abc");
    }




}
