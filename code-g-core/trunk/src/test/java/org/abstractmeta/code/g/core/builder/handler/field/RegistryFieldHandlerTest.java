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

import com.google.common.base.Function;
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
import org.abstractmeta.code.g.core.util.ReflectUtil;
import org.abstractmeta.code.g.generator.Context;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.annotation.Nullable;
import javax.persistence.Id;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @author Adrian Witas
 */

@Test
public class RegistryFieldHandlerTest {

    private final Logger logger = Logger.getLogger(RegistryFieldHandlerTest.class.getName());

    /**
     * Test a simple registry implementation
     */
    public void testSimpleRegistry() throws Exception {
        JavaType simpleRegistry = new ClassTypeProvider(ISimpleRegistry.class).get();
        Context context = new ContextImpl();
        ClassGeneratorConfig config = new ClassGeneratorConfig();
        context.put(ClassGeneratorConfig.class, config);
        JavaTypeBuilder builder = new SimpleClassBuilder("com.test.SimpleRegistry", simpleRegistry, context)
                .addSuperInterfaces(ISimpleRegistry.class)
                .addModifiers(JavaModifier.PUBLIC);

        builder.addField(new JavaFieldBuilder().addModifiers(JavaModifier.PRIVATE).setType(new ParameterizedTypeImpl(null, Map.class, Integer.class, IEntry.class)).setImmutable(true).setName("registry"));
        CompiledJavaType compiledJavaType = JavaTypeCompiler.compile(builder);
        ISimpleRegistry registry = (ISimpleRegistry) compiledJavaType.getCompiledType().getConstructor(Map.class).newInstance(new HashMap());
        for (int i = 0; i < 3; i++) {
            Assert.assertFalse(registry.isRegistered(i));
            IEntry entry = new Entry(i, "k1", i, "n1");
            registry.register(entry);
            Assert.assertTrue(registry.isRegistered(i));
            registry.unregister(entry);
            Assert.assertFalse(registry.isRegistered(i));
        }
    }


    /**
     * Test a simple registry implementation with key provider annotation
     */
    public void testSimpleRegistryWithAnnotation() throws Exception {
        JavaType simpleRegistry = new ClassTypeProvider(IRegistry.class).get();
        Context context = new ContextImpl();
        ClassGeneratorConfig config = new ClassGeneratorConfig();
        config.setRegistryKeyAnnotation(Id.class.getName());
        context.put(ClassGeneratorConfig.class, config);
        JavaTypeBuilder builder = new SimpleClassBuilder("com.test.SimpleRegistry", simpleRegistry, context)
                .addSuperInterfaces(IRegistry.class)
                .addModifiers(JavaModifier.PUBLIC);

        builder.addField(new JavaFieldBuilder().addModifiers(JavaModifier.PRIVATE).setType(new ParameterizedTypeImpl(null, Map.class, String.class, IEntry.class)).setImmutable(true).setName("registry"));
        CompiledJavaType compiledJavaType = JavaTypeCompiler.compile(builder);
        IRegistry registry = (IRegistry) compiledJavaType.getCompiledType().getConstructor(Map.class).newInstance(new HashMap());
        for (int i = 0; i < 3; i++) {
            Assert.assertFalse(registry.isRegistered(i + ""));
            IEntry entry = new Entry(i, "" + i, i,  "Name " +i);
            registry.register(entry);
            Assert.assertTrue(registry.isRegistered(i + ""));
            registry.unregister(entry);
            Assert.assertFalse(registry.isRegistered(i + ""));
        }
    }


    /**
     * Test a simple registry implementation wih a custom key provider
     */
    public void testSimpleRegistryWithCustomKeyProvider() throws Exception {
        JavaType simpleRegistry = new ClassTypeProvider(ISimpleRegistry.class).get();
        Context context = new ContextImpl();
        ClassGeneratorConfig config = new ClassGeneratorConfig();

        //THIS ENABLES A KEY PROVIDER
        config.setRegistryItemUseKeyProvider(true);

        context.put(ClassGeneratorConfig.class, config);
        JavaTypeBuilder builder = new SimpleClassBuilder("com.test.SimpleRegistry", simpleRegistry, context)
                .addSuperInterfaces(ISimpleRegistry.class)
                .addModifiers(JavaModifier.PUBLIC);

        builder.addField(new JavaFieldBuilder().addModifiers(JavaModifier.PRIVATE).setType(new ParameterizedTypeImpl(null, Map.class, Integer.class, IEntry.class)).setImmutable(true).setName("registry"));
        CompiledJavaType compiledJavaType = JavaTypeCompiler.compile(builder);
        ISimpleRegistry registry = (ISimpleRegistry) compiledJavaType.getCompiledType().getConstructor(Map.class).newInstance(new HashMap());
        for (int i = 0; i < 3; i++) {
            Assert.assertFalse(registry.isRegistered(i));
            IEntry entry = new Entry(i, "k1", i, "n1");
            registry.register(entry);
            Assert.assertTrue(registry.isRegistered(i));
            registry.unregister(entry);
            Assert.assertFalse(registry.isRegistered(i));
        }

        //Test key provider - in this test uses value
        //
        //  public void register(RegistryFieldHandlerTest.IEntry argument0) {
        //      registry.put(keyProvider.apply(argument0), argument0);
        //  }
        //
        ReflectUtil.invokeMethod(registry, "setRegistryKeyProvider", new Class[]{Function.class}, new ValueProvider());
        for (int i = 0; i < 3; i++) {
            Integer value = i + 10;
            Assert.assertFalse(registry.isRegistered(value));
            IEntry entry = new Entry(i, "k1", value, "n1");
            registry.register(entry);
            Assert.assertTrue(registry.isRegistered(value));
            registry.unregister(entry);
            Assert.assertFalse(registry.isRegistered(value));
        }

    }

    /**
     * This test uses Multi level registry
     *
     * @throws Exception
     */
    public void testMultiLevelRegistry() throws Exception {
        JavaType simpleRegistry = new ClassTypeProvider(IMultiLevelRegistry.class).get();
        Context context = new ContextImpl();
        ClassGeneratorConfig config = new ClassGeneratorConfig();
        context.put(ClassGeneratorConfig.class, config);
        JavaTypeBuilder builder = new SimpleClassBuilder("com.test.MultiLevelRegistry", simpleRegistry, context)
                .addSuperInterfaces(IMultiLevelRegistry.class)
                .addModifiers(JavaModifier.PUBLIC);

        builder.addField(new JavaFieldBuilder().addModifiers(JavaModifier.PRIVATE).setType(new ParameterizedTypeImpl(null, Map.class, Integer.class, new ParameterizedTypeImpl(null, Map.class, String.class, IEntry.class))).setImmutable(true).setName("registry"));
        CompiledJavaType compiledJavaType = JavaTypeCompiler.compile(builder);
        IMultiLevelRegistry registry = (IMultiLevelRegistry) compiledJavaType.getCompiledType().getConstructor(Map.class).newInstance(new HashMap());

        Assert.assertFalse(registry.isRegistered(1, "1.1"));
        registry.register(1, "1.1", new Entry(1, "k1", 1, "n1"));
        Assert.assertTrue(registry.isRegistered(1, "1.1"));
        registry.unregister(1, "1.1");
        Assert.assertFalse(registry.isRegistered(1, "1.1"));

    }


    /**
     * Test a group registry implementation
     */
    public void testGroupRegistry() throws Exception {
        JavaType groupRegistry = new ClassTypeProvider(IGroupRegistry.class).get();
        Context context = new ContextImpl();
        ClassGeneratorConfig config = new ClassGeneratorConfig();
        context.put(ClassGeneratorConfig.class, config);
        JavaTypeBuilder builder = new SimpleClassBuilder("com.test.SimpleRegistry", groupRegistry, context)
                .addSuperInterfaces(IGroupRegistry.class)
                .addModifiers(JavaModifier.PUBLIC);

        builder.addField(new JavaFieldBuilder().addModifiers(JavaModifier.PRIVATE).setType(String.class).setName("field"));
        builder.addField(new JavaFieldBuilder().addModifiers(JavaModifier.PRIVATE).setType(new ParameterizedTypeImpl(null, Map.class, Integer.class, IEntry.class)).setImmutable(true).setName("entryRegistry"));
        CompiledJavaType compiledJavaType = JavaTypeCompiler.compile(builder);
        IGroupRegistry registry = (IGroupRegistry) compiledJavaType.getCompiledType().getConstructor(Map.class).newInstance(new HashMap());
        for (int i = 0; i < 3; i++) {
            Assert.assertFalse(registry.isEntryRegistered(i));
            IEntry entry = new Entry(i, "k1", i, "n1");
            registry.registerEntry(entry);
            Assert.assertTrue(registry.isEntryRegistered(i));
            registry.unregisterEntry(entry);
            Assert.assertFalse(registry.isEntryRegistered(i));
        }
    }


    protected class ValueProvider implements Function<IEntry, Integer> {

        @Override
        public Integer apply(@Nullable IEntry entry) {
            return entry.getValue();
        }
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

    public static interface ISimpleRegistry {

        IEntry get(Integer id);

        void register(IEntry entry);

        boolean isRegistered(Integer id);

        void unregister(Integer id);

        void unregister(IEntry entry);


    }


    public static interface IRegistry {

        IEntry get(String id);

        void register(IEntry entry);

        boolean isRegistered(String id);

        void unregister(String id);

        void unregister(IEntry entry);


    }


    public static interface IEntry {

        int getId();

        String getName();

        @Id
        String getKey();

        Integer getValue();


    }


    public static interface IMultiLevelRegistry {

        IEntry get(Integer id, String key);

        void register(Integer id, String key, IEntry entry);

        void unregister(Integer id, String key);

        boolean isRegistered(Integer id, String key);

    }

    public static interface IGroupRegistry {

        String getField();

        void registerEntry(IEntry entry);

        void unregisterEntry(IEntry entry);

        IEntry getEntry(Integer id);

        boolean isEntryRegistered(Integer id);

        Map<Integer, IEntry> getEntryRegistry();
    }

}
