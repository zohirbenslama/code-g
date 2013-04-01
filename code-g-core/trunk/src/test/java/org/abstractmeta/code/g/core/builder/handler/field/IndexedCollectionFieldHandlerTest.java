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
package org.abstractmeta.code.g.core.builder.handler.field;

import org.abstractmeta.code.g.code.CompiledJavaType;
import org.abstractmeta.code.g.code.JavaModifier;
import org.abstractmeta.code.g.code.JavaType;
import org.abstractmeta.code.g.code.JavaTypeBuilder;
import org.abstractmeta.code.g.core.builder.SimpleClassBuilder;
import org.abstractmeta.code.g.core.code.builder.JavaFieldBuilder;
import org.abstractmeta.code.g.core.generator.ClassGeneratorConfig;
import org.abstractmeta.code.g.core.generator.ContextImpl;
import org.abstractmeta.code.g.core.internal.ParameterizedTypeImpl;
import org.abstractmeta.code.g.core.provider.ClassTypeProvider;
import org.abstractmeta.code.g.core.util.JavaTypeCompiler;
import org.abstractmeta.code.g.generator.Context;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.persistence.Id;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

/**
 * @author Adrian Witas
 */

@Test
@SuppressWarnings("unchecked")
public class IndexedCollectionFieldHandlerTest {


    /**
     * Test a simple indexed collection
     */
    public void testSimpleIndexedCollection() throws Exception {
        JavaType simpleRegistry = new ClassTypeProvider(IMessage.class).get();
        Context context = new ContextImpl();
        ClassGeneratorConfig config = new ClassGeneratorConfig();
        context.put(ClassGeneratorConfig.class, config);
        JavaTypeBuilder builder = new SimpleClassBuilder("com.test.Message", simpleRegistry, context)
                .addSuperInterfaces(IMessage.class)
                .addModifiers(JavaModifier.PUBLIC);

        builder.addField(new JavaFieldBuilder().addModifiers(JavaModifier.PRIVATE).setType(new ParameterizedTypeImpl(null, Collection.class, IEntry.class)).setImmutable(true).setName("entries"));
        CompiledJavaType compiledJavaType = JavaTypeCompiler.compile(builder);

        IMessage iMessage = (IMessage) compiledJavaType.getCompiledType().getConstructor(Collection.class).newInstance(new ArrayList());
        Assert.assertFalse(iMessage.containsEntry("name 1"));

        iMessage.addEntries(new Entry(1, "key 1", 100, "name 1"));
        Assert.assertNotNull(iMessage.getEntry("name 1"));
        Assert.assertTrue(iMessage.containsEntry("name 1"));
        Assert.assertFalse(iMessage.removeEntry("name 2"));
        Assert.assertTrue(iMessage.removeEntry("name 1"));
    }


    /**
     * Test a simple registry implementation
     */
    public void testIndexedCollectionWithKeyProvider() throws Exception {
        JavaType simpleRegistry = new ClassTypeProvider(IMessage.class).get();
        Context context = new ContextImpl();
        ClassGeneratorConfig config = new ClassGeneratorConfig();
        config.setRegistryKeyAnnotation(Id.class.getName());
        config.setIndexCollectionUseKeyProvider(true);

        context.put(ClassGeneratorConfig.class, config);
        JavaTypeBuilder builder = new SimpleClassBuilder("com.test.Message", simpleRegistry, context)
                .addSuperInterfaces(IMessage.class)
                .addModifiers(JavaModifier.PUBLIC);

        builder.addField(new JavaFieldBuilder().addModifiers(JavaModifier.PRIVATE).setType(new ParameterizedTypeImpl(null, Collection.class, IEntry.class)).setImmutable(true).setName("entries"));
        CompiledJavaType compiledJavaType = JavaTypeCompiler.compile(builder);

        IMessage iMessage = (IMessage) compiledJavaType.getCompiledType().getConstructor(Collection.class).newInstance(new ArrayList());
        Assert.assertFalse(iMessage.containsEntry("name 1"));

        iMessage.addEntries(new Entry(1, "key 1", 100, "name 1"));
        Assert.assertNotNull(iMessage.getEntry("name 1"));
        Assert.assertTrue(iMessage.containsEntry("name 1"));
        Assert.assertFalse(iMessage.removeEntry("name 2"));
        Assert.assertTrue(iMessage.removeEntry("name 1"));
    }




    public static class Entry implements IEntry {
        private int id;
        private String key;

        private Integer value;
        private String name;

        public Entry() {
        }

        public Entry(int id, String key, Integer value, String name) {
            this.id = id;
            this.key = key;
            this.value = value;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }


        public Integer getValue() {
            return value;
        }

        public void setValue(Integer value) {
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }


    public static interface IMessage {

        public IEntry getEntry(String key);

        public void addEntries(IEntry... entries);

        public boolean containsEntry(String key);

        public boolean removeEntry(String key);

    }


    public static interface IEntry {

        int getId();

        String getName();

        @Id
        String getKey();

        Integer getValue();


    }


}
