package org.abstractmeta.code.g.core.generator;

import org.abstractmeta.code.g.config.Descriptor;
import org.abstractmeta.code.g.config.UnitDescriptor;
import org.abstractmeta.code.g.core.config.DescriptorImpl;
import org.abstractmeta.code.g.core.config.SourceMatcherImpl;
import org.abstractmeta.code.g.core.config.UnitDescriptorImpl;
import org.abstractmeta.code.g.generator.CodeUnitGenerator;
import org.abstractmeta.code.g.generator.GeneratedCode;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.util.Arrays;
import java.util.Properties;

/**
 */
@Test
public class CodeUnitGeneratorTest {


    public void testCodeUnitGenerator() {
        CodeUnitGenerator unitGenerator = new CodeUnitGeneratorImpl();
        GeneratedCode generatedCode= unitGenerator.generate(getUnitDescriptor());
        Assert.assertEquals(generatedCode.getRegistry().get().size(), 2);

    }

    protected UnitDescriptor getUnitDescriptor() {
        UnitDescriptorImpl result = new UnitDescriptorImpl();
        result.setSourceDirectory(new File("src/test/java").getAbsolutePath());
        DescriptorImpl descriptor = new DescriptorImpl();
        SourceMatcherImpl sourceMatcher = new SourceMatcherImpl();
        sourceMatcher.setSourceDirectory(new File("src/test/java").getAbsolutePath());
        sourceMatcher.setClassNames(Arrays.asList(Foo.class.getName()));
        descriptor.setSourceMatcher(sourceMatcher);
        descriptor.setGeneratorClass(ClassGenerator.class.getName());
        Properties properties = new Properties();
        properties.setProperty("generateBuilder", "true");
        descriptor.setProperties(properties);
        result.setDescriptors(Arrays.<Descriptor>asList(descriptor));
        result.setTargetSourceDirectory((new File("target/generated-sources/code-g").getAbsolutePath()));
        return result;
    }

    public static interface Foo {

        int getId();

    }


}
