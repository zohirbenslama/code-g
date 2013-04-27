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
package org.abstractmeta.code.g.core.code;

import org.abstractmeta.code.g.code.JavaModifier;
import org.abstractmeta.code.g.code.JavaTypeBuilder;
import org.abstractmeta.code.g.core.code.builder.JavaFieldBuilder;
import org.abstractmeta.code.g.core.code.builder.JavaTypeBuilderImpl;
import org.abstractmeta.code.g.core.renderer.TypeRenderer;
import org.abstractmeta.code.g.core.util.ReflectUtil;
import org.abstractmeta.code.g.renderer.JavaTypeRenderer;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Represents JavaTypeTest
 *
 * @author Adrian Witas
 */
@Test
public class JavaTypeTest {

    @SuppressWarnings("unchecked")
    public void testJavaType() {

        JavaTypeBuilder builder = new JavaTypeBuilderImpl("com.test.Foo");
        builder.addModifiers(JavaModifier.PUBLIC);
        builder.addField(new JavaFieldBuilder()
                .setName("field1")
                .setType(String.class)
                .addModifiers(JavaModifier.PRIVATE).build());
        JavaTypeRenderer renderer = new TypeRenderer();
        String code = renderer.render(builder.build(), builder.getImporter(), 4);
        Class fooClass = ReflectUtil.compileSource("com.test.Foo", code);
        Assert.assertEquals(fooClass.getName(), "com.test.Foo");
        Object fooInstance = ReflectUtil.getInstance(fooClass);
        ReflectUtil.setFieldValue(fooInstance, "field1", "abc");
        Assert.assertEquals(ReflectUtil.getFieldValue(String.class, fooInstance, "field1"), "abc");

    }

}
