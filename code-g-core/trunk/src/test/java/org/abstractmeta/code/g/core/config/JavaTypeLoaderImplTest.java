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
package org.abstractmeta.code.g.core.config;


import org.abstractmeta.code.g.code.JavaTypeRegistry;
import org.abstractmeta.code.g.config.loader.JavaTypeLoader;
import org.abstractmeta.code.g.core.code.JavaTypeRegistryImpl;
import org.abstractmeta.code.g.core.config.builder.DescriptorBuilder;
import org.abstractmeta.code.g.core.config.loader.JavaTypeLoaderImpl;
import org.abstractmeta.code.g.core.config.test.Bar;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.inject.Inject;
import java.util.Collection;

@Test
public class JavaTypeLoaderImplTest {


    protected JavaTypeLoader getJavaTypeLoader() {
        return new JavaTypeLoaderImpl();
    }

    public void testJavaTypeLoaderImpl() {
        JavaTypeLoader loader = getJavaTypeLoader();
        Assert.assertNotNull(loader);
    }

    public void testLoadSourceClass() {
        JavaTypeLoader loader = getJavaTypeLoader();
        JavaTypeRegistry registry = new JavaTypeRegistryImpl();


        {   //inner class test
            DescriptorBuilder builder = new DescriptorBuilder();
            builder.setSourceClass(Foo.class.getName());
            Collection<String> loaded = loader.load(registry, builder.build(), null);
            String[] loadedTypes = loaded.toArray(new String[]{});
            Assert.assertEquals(loadedTypes[0], Foo.class.getName());
        }
        {   //class test
            DescriptorBuilder builder = new DescriptorBuilder();
            builder.setSourceClass(Bar.class.getName());
            Collection<String> loaded = loader.load(registry, builder.build(), null);
            String[] loadedTypes = loaded.toArray(new String[]{});
            Assert.assertEquals(loadedTypes[0], Bar.class.getName());
        }


    }


    public void testLoadSourcePackage() {
        JavaTypeLoader loader = getJavaTypeLoader();
        JavaTypeRegistry registry = new JavaTypeRegistryImpl();

        {   //source package test
            DescriptorBuilder builder = new DescriptorBuilder();
            builder.setSourcePackage(Bar.class.getPackage().getName());
            Collection<String> loaded = loader.load(registry, builder.build(), null);
            String[] loadedTypes = loaded.toArray(new String[]{});
            Assert.assertEquals(loadedTypes[0], Bar.class.getName());
        }
    }

    public void testLoadSourcePackageFromJar() {
        JavaTypeLoader loader = getJavaTypeLoader();
        JavaTypeRegistry registry = new JavaTypeRegistryImpl();

        {   //source package test
            DescriptorBuilder builder = new DescriptorBuilder();
            builder.setSourcePackage(Inject.class.getPackage().getName());
            Collection<String> loaded = loader.load(registry, builder.build(), null);
            Assert.assertTrue(loaded.size() > 0);
        }
    }

    public void testLoadSourcePackageWithWildcard() {
        JavaTypeLoader loader = getJavaTypeLoader();
        JavaTypeRegistry registry = new JavaTypeRegistryImpl();

        {   //source package test
            DescriptorBuilder builder = new DescriptorBuilder();
            builder.setSourcePackage(Bar.class.getPackage().getName() + ".*");
            Collection<String> loaded = loader.load(registry, builder.build(), null);
            String[] loadedTypes = loaded.toArray(new String[]{});
            Assert.assertEquals(loaded.size(), 3);
        }
    }

    public void testLoadSourcePackageAndInclusion() {
        JavaTypeLoader loader = getJavaTypeLoader();
        JavaTypeRegistry registry = new JavaTypeRegistryImpl();

        {   //source package test
            DescriptorBuilder builder = new DescriptorBuilder();
            builder.addInclusion("Bar");
            builder.setSourcePackage(Bar.class.getPackage().getName() + ".*");
            Collection<String> loaded = loader.load(registry, builder.build(), null);
            String[] loadedTypes = loaded.toArray(new String[]{});
            Assert.assertEquals(loaded.size(), 1);
        }
    }


    public void testLoadSourcePackageAndExclusion() {
        JavaTypeLoader loader = getJavaTypeLoader();
        JavaTypeRegistry registry = new JavaTypeRegistryImpl();

        {   //source package test
            DescriptorBuilder builder = new DescriptorBuilder();
            builder.addExclusions("A");
            builder.setSourcePackage(Bar.class.getPackage().getName() + ".*");
            Collection<String> loaded = loader.load(registry, builder.build(), null);
            String[] loadedTypes = loaded.toArray(new String[]{});
            Assert.assertEquals(loaded.size(), 2);
        }

    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testLoadInvalidClass() {
        JavaTypeLoader loader = getJavaTypeLoader();
        JavaTypeRegistry registry = new JavaTypeRegistryImpl();

        {   //source package test
            DescriptorBuilder builder = new DescriptorBuilder();
            builder.addExclusions("A");
            builder.setSourceClass(Bar.class.getPackage().getName() + ".*");
            loader.load(registry, builder.build(), null);
        }
    }


    @Test(expectedExceptions = IllegalStateException.class)
    public void testLoadEmptySource() {
        JavaTypeLoader loader = getJavaTypeLoader();
        JavaTypeRegistry registry = new JavaTypeRegistryImpl();

        {   //source package test
            DescriptorBuilder builder = new DescriptorBuilder();
            loader.load(registry, builder.build(), null);
        }
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testNullDescriptor() {
        JavaTypeLoader loader = getJavaTypeLoader();
        JavaTypeRegistry registry = new JavaTypeRegistryImpl();
        loader.load(registry, null, null);
    }

    public static class Foo {

    }

}
