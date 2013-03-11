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
package org.abstractmeta.code.g.core.extractor;

import org.abstractmeta.code.g.code.JavaField;
import org.abstractmeta.code.g.core.generator.ContextImpl;
import org.abstractmeta.code.g.core.provider.ClassTypeProvider;
import org.abstractmeta.code.g.generator.Context;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;

/**
 * RegistryFieldExtractorTest
 */
@Test
public class RegistryFieldExtractorTest {

    private Context context = new ContextImpl();

    public void testRegistryFieldExtractor() {

        RegistryFieldExtractor extractor = new RegistryFieldExtractor();

        {
            List<JavaField> fields = extractor.extract(new ClassTypeProvider(Registry1.class).get(), context);
            JavaField field = fields.get(0);
            ParameterizedType fieldType = ParameterizedType.class.cast(field.getType());
            Assert.assertEquals(fieldType.getActualTypeArguments()[0], String.class, "invalid registry type " + field.getType());
            Assert.assertEquals(fieldType.getActualTypeArguments()[1], Foo.class, "invalid registry type " + field.getType());
        }


        {
            List<JavaField> fields = extractor.extract(new ClassTypeProvider(Registry2.class).get(), context);
            JavaField field = fields.get(0);
            ParameterizedType fieldType = ParameterizedType.class.cast(field.getType());
            Assert.assertEquals(fieldType.getActualTypeArguments()[0], String.class, "invalid registry type " + field.getType());
            Assert.assertTrue(fieldType.getActualTypeArguments()[1].toString().contains(Map.class.getSimpleName()), "invalid registry type " + field.getType());
        }

        {
            List<JavaField> fields = extractor.extract(new ClassTypeProvider(Registry3.class).get(), context);
            JavaField field = fields.get(0);
            ParameterizedType fieldType = ParameterizedType.class.cast(field.getType());
            Assert.assertEquals(fieldType.getActualTypeArguments()[0], String.class, "invalid registry type " + field.getType());
            Assert.assertEquals(fieldType.getActualTypeArguments()[1], Foo.class, "invalid registry type " + field.getType());
        }


    }

    public static interface Registry1 {
        void register(String key, Foo value);

        Foo get(String key);
    }

    public static interface Registry2 {
        void register(String key, int subKey, Foo value);

        Foo get(String key, int subKey);
    }

    public static interface Registry3 {
        void register(Foo value);

        Foo get(String key);
    }


    public static class Foo {

    }
}
