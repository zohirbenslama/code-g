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

import org.abstractmeta.code.g.config.Descriptor;
import org.abstractmeta.code.g.config.SourceMatcher;
import org.abstractmeta.code.g.config.UnitDescriptor;
import org.abstractmeta.code.g.core.config.provider.UnitDescriptorProvider;
import org.abstractmeta.code.g.core.util.PropertiesUtil;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.util.Arrays;
import java.util.Properties;

/**
 */
@Test
public class UnitDescriptorProviderTest {

    public void testUnitDescriptorProvider() {

        Properties configProperties = PropertiesUtil.loadFromFile(new File("src/test/code-g/unit.properties").getAbsoluteFile());
        UnitDescriptorProvider provider = new UnitDescriptorProvider(configProperties);
        UnitDescriptor unitDescriptor = provider.get();
        Assert.assertEquals(unitDescriptor.getDependencyPackages(), Arrays.asList("com.test", "com.test1"));
        Assert.assertEquals(unitDescriptor.getTargetSourceDirectory(), "targetDir");
        Assert.assertEquals(unitDescriptor.getTargetCompilationDirectory(), "targetCompilationDir");
        Assert.assertEquals(unitDescriptor.getClassPathEntries(), Arrays.asList("classPathEntries"));
        Assert.assertEquals(unitDescriptor.getDescriptors().size(), 2);
        Assert.assertNotNull(unitDescriptor.getPostDescriptor());

        {
            Descriptor descriptor = unitDescriptor.getDescriptors().get(0);
            Assert.assertEquals(descriptor.getGeneratorClass(), "classA");

            Assert.assertEquals(descriptor.getProperties().size(), 1);
            Assert.assertEquals(descriptor.getProperties().getProperty("key1"), "value1");


            Assert.assertNotNull(descriptor.getSourceMatcher());

            SourceMatcher sourceMatcher = descriptor.getSourceMatcher();
            Assert.assertEquals(sourceMatcher.isIncludeSubpackages(), true);
            Assert.assertEquals(sourceMatcher.getClassNames(), Arrays.asList("C1", "C2"));
            Assert.assertEquals(sourceMatcher.getPackageNames(), Arrays.asList("p1", "p2"));
            Assert.assertEquals(sourceMatcher.getDependencyPackages(), Arrays.asList("d1", "d2"));
            Assert.assertEquals(sourceMatcher.getExclusionPatterns(), Arrays.asList("e1", "e2"));
            Assert.assertEquals(sourceMatcher.getInclusionPatterns(), Arrays.asList("i1", "i2"));


        }
        {
            Descriptor descriptor = unitDescriptor.getDescriptors().get(1);
            Assert.assertEquals(descriptor.getGeneratorClass(), "classB");
            Assert.assertEquals(descriptor.getProperties().size(), 2);
            Assert.assertEquals(descriptor.getProperties().getProperty("key3"), "value3");

            Assert.assertNotNull(descriptor.getSourceMatcher());
            SourceMatcher sourceMatcher = descriptor.getSourceMatcher();
            Assert.assertEquals(sourceMatcher.getClassNames(), Arrays.asList("C10", "C20"));
            Assert.assertEquals(sourceMatcher.getPackageNames(), Arrays.asList("p10", "p20"));
            Assert.assertEquals(sourceMatcher.getDependencyPackages(), Arrays.asList("d10", "d20"));
            Assert.assertEquals(sourceMatcher.getExclusionPatterns(), Arrays.asList("e10", "e20"));
            Assert.assertEquals(sourceMatcher.getInclusionPatterns(), Arrays.asList("i10", "i20"));
        }

    }

}
