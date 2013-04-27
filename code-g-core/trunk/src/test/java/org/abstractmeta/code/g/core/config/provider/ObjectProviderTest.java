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
package org.abstractmeta.code.g.core.config.provider;

import org.abstractmeta.code.g.config.Descriptor;
import org.abstractmeta.code.g.config.UnitDescriptor;
import org.abstractmeta.code.g.core.config.DescriptorImpl;
import org.abstractmeta.code.g.core.config.NamingConventionImpl;
import org.abstractmeta.code.g.core.config.UnitDescriptorImpl;
import org.abstractmeta.code.g.core.config.provider.ObjectProvider;
import org.abstractmeta.code.g.core.util.PropertiesUtil;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.util.Arrays;
import java.util.Properties;

/**
 * Represents ObjectProviderTest
 *
 * @author Adrian Witas
 */
@Test
public class ObjectProviderTest {

    public void testObjectProvider() {
        Properties properties = PropertiesUtil.loadFromFile(new File("src/test/code-g/unit-provider-test.properties"));
        ObjectProvider<UnitDescriptor> provider =   new ObjectProvider<UnitDescriptor>(UnitDescriptorImpl.class, properties, new Class[]{DescriptorImpl.class, NamingConventionImpl.class});
        UnitDescriptor unitDescriptor = provider.get();
        Assert.assertEquals(unitDescriptor.getDependencyPackages(), Arrays.asList("foo", "bar"));
        Assert.assertEquals(unitDescriptor.getSourceDirectory(), "${basedir}/src/tets/test-me/");
        Assert.assertEquals(unitDescriptor.getTargetSourceDirectory(), "${project.build.generated.sources.directory}");
        Assert.assertNotNull(unitDescriptor.getDescriptors());
        Assert.assertEquals(unitDescriptor.getDescriptors().size(), 1);
        {
            Descriptor descriptor = unitDescriptor.getDescriptors().get(0);
            Assert.assertEquals(descriptor.getGeneratorClass(), "Abc");
            Assert.assertEquals(descriptor.getProperties().size(), 2);
            Assert.assertEquals(descriptor.getProperties().get("key1"), "val1");

        }


    }

}
