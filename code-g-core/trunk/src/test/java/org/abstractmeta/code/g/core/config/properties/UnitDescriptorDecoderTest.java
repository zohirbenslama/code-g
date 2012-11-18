package org.abstractmeta.code.g.core.config.properties;

import com.google.common.collect.Maps;
import org.abstractmeta.code.g.config.Descriptor;
import org.abstractmeta.code.g.config.UnitDescriptor;
import org.abstractmeta.code.g.core.util.PropertiesUtil;
import org.testng.Assert;
import org.testng.annotations.Test;


import java.io.File;
import java.util.*;

/**
 * Represents  UnitDescriptorDecoderTest.
 *
 */
@Test
public class UnitDescriptorDecoderTest {


    public void testDecode() {
        UnitDescriptorsDecoder decoder = new UnitDescriptorsDecoder(template);
        Properties properties = PropertiesUtil.loadFromFile(new File("src/main/code-g/unit.properties"));
        List<UnitDescriptor> unitDescriptors = decoder.decode(Maps.fromProperties(properties));
        UnitDescriptor unitDescriptor = unitDescriptors.get(0);
        Assert.assertEquals(unitDescriptor.getSourcePackage(), "org.abstractmeta.code.g.test.me");
        Assert.assertEquals(unitDescriptor.getSourceDirectory(), "${basedir}/src/tets/test-me/");
        Assert.assertEquals(unitDescriptor.getTargetDirectory(), "${project.build.generated.sources.directory}");
        Descriptor descriptor =  unitDescriptor.getDescriptors().get(0);
        Assert.assertEquals(descriptor.getPlugin(), "org.abstractmeta.code.g.core.plugin.ClassGeneratorPlugin");
        Assert.assertEquals(descriptor.getInterfaces(), "org.abstractmeta.code.g.test.Commandify");
        Assert.assertEquals(new TreeSet<String>(descriptor.getExclusions()), new TreeSet<String>(Arrays.asList("FakeFoo", "FakeDummy")));
    }


}

        
