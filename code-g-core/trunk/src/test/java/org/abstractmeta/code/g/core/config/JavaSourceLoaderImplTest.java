package org.abstractmeta.code.g.core.config;

import org.abstractmeta.code.g.code.JavaTypeRegistry;
import org.abstractmeta.code.g.config.loader.LoadedSource;
import org.abstractmeta.code.g.core.code.JavaTypeRegistryImpl;
import org.abstractmeta.code.g.core.config.loader.JavaSourceLoaderImpl;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.util.Arrays;

/**
 * Basic test JavaSourceLoader use cases
 * @author Adrian Witas
 */
@Test
public class JavaSourceLoaderImplTest {

    private final JavaTypeRegistry registry = new JavaTypeRegistryImpl();

    public void testLoadPackageFromExternalJar() {
        JavaSourceLoaderImpl sourceLoader = new JavaSourceLoaderImpl();
        SourceMatcherImpl sourceFilter = new SourceMatcherImpl();
        sourceFilter.setPackageNames(Arrays.asList("javax.inject"));
        sourceFilter.setIncludeSubpackages(true);
        LoadedSource result = sourceLoader.load(sourceFilter, registry, JavaSourceLoaderImplTest.class.getClassLoader());
        Assert.assertTrue(result.getJavaTypes().size() > 4, "should load classes from jar using package name");
    }


    public void testLoadClassFromExternalJar() {
        JavaSourceLoaderImpl sourceLoader = new JavaSourceLoaderImpl();
        SourceMatcherImpl sourceFilter = new SourceMatcherImpl();
        sourceFilter.setClassNames(Arrays.asList("javax.inject.Inject"));
        sourceFilter.setIncludeSubpackages(true);
        LoadedSource result = sourceLoader.load(sourceFilter, registry, JavaSourceLoaderImplTest.class.getClassLoader());
        Assert.assertEquals(result.getJavaTypes().size(), 1, "should load javax.inject.Inject");
    }


    public void testLoadClassFromExternalPath() {
        JavaSourceLoaderImpl sourceLoader = new JavaSourceLoaderImpl();
        SourceMatcherImpl sourceFilter = new SourceMatcherImpl();
        sourceFilter.setPackageNames(Arrays.asList("org.abstractmeta.code.g.core.helper"));
        sourceFilter.setIncludeSubpackages(true);
        LoadedSource result = sourceLoader.load(sourceFilter, registry, JavaSourceLoaderImplTest.class.getClassLoader());
        Assert.assertTrue(result.getJavaTypes().size() > 1, "should load org.abstractmeta.code.g.core.helper");
    }


    public void testLoadClassFromSourcePath() {
        JavaSourceLoaderImpl sourceLoader = new JavaSourceLoaderImpl();
        SourceMatcherImpl sourceFilter = new SourceMatcherImpl();
        sourceFilter.setPackageNames(Arrays.asList("org.abstractmeta.code.g.test.source"));
        sourceFilter.setIncludeSubpackages(true);
        sourceFilter.setSourceDirectory(new File("src/test/test-source").getAbsolutePath());
        LoadedSource result = sourceLoader.load(sourceFilter, registry, JavaSourceLoaderImplTest.class.getClassLoader());
        Assert.assertTrue(! result.getJavaTypes().isEmpty(), "should load org.abstractmeta.code.g.core.annotation");
        Assert.assertTrue(! result.getCompiledJavaTypes().isEmpty(), "should have source code");

    }


}
