package org.abstractmeta.code.g.core.util;

import org.abstractmeta.code.g.config.Descriptor;
import org.abstractmeta.code.g.config.UnitDescriptor;
import org.abstractmeta.code.g.core.config.DescriptorImpl;
import org.abstractmeta.code.g.core.config.SourceMatcherImpl;
import org.abstractmeta.code.g.core.config.UnitDescriptorImpl;
import org.abstractmeta.code.g.core.generator.ClassGenerator;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Properties;

/**
 * Represents UnitDescriptorUtil
 * <p>
 * </p>
 *
 * @author Adrian Witas
 */
public class UnitDescriptorUtil {



        public static UnitDescriptor getUnitDescriptorByPackages(File sourceDirectory, File targetSourceDirectory, Collection<String> packageNames, Class generatorClass, String... properties) {
        UnitDescriptorImpl result = new UnitDescriptorImpl();
        result.setSourceDirectory(sourceDirectory.getAbsolutePath());
        DescriptorImpl descriptor = new DescriptorImpl();
        SourceMatcherImpl sourceMatcher = new SourceMatcherImpl();
        sourceMatcher.setSourceDirectory(sourceDirectory.getAbsolutePath());
        sourceMatcher.setPackageNames(packageNames);
        descriptor.setSourceMatcher(sourceMatcher);
        descriptor.setGeneratorClass(generatorClass.getName());
        Properties classGeneratorProperties = new Properties();
        for (int i = 0; i < properties.length; i+=2) {
            classGeneratorProperties.setProperty(properties[0], properties[1]);
        }
        descriptor.setProperties(classGeneratorProperties);
        result.setDescriptors(Arrays.<Descriptor>asList(descriptor));
        result.setTargetSourceDirectory(targetSourceDirectory.getAbsolutePath());
        return result;


    }

    public static UnitDescriptor getUnitDescriptorByClasses(File sourceDirectory, File targetSourceDirectory, Collection<String> classNames, Class generatorClass, String... properties) {
        UnitDescriptorImpl result = new UnitDescriptorImpl();
        result.setSourceDirectory(sourceDirectory.getAbsolutePath());
        DescriptorImpl descriptor = new DescriptorImpl();
        SourceMatcherImpl sourceMatcher = new SourceMatcherImpl();
        sourceMatcher.setSourceDirectory(sourceDirectory.getAbsolutePath());
        sourceMatcher.setClassNames(classNames);
        descriptor.setSourceMatcher(sourceMatcher);
        descriptor.setGeneratorClass(generatorClass.getName());
        Properties classGeneratorProperties = new Properties();
        for (int i = 0; i < properties.length; i+=2) {
            classGeneratorProperties.setProperty(properties[i], properties[i+1]);
        }
        descriptor.setProperties(classGeneratorProperties);
        result.setDescriptors(Arrays.<Descriptor>asList(descriptor));
        result.setTargetSourceDirectory(targetSourceDirectory.getAbsolutePath());
        return result;


    }
}
