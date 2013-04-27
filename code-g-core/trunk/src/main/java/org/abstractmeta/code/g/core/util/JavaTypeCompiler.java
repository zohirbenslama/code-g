package org.abstractmeta.code.g.core.util;

import org.abstractmeta.code.g.code.*;
import org.abstractmeta.code.g.core.code.CompiledJavaTypeImpl;
import org.abstractmeta.code.g.core.code.builder.SourcedJavaTypeBuilder;
import org.abstractmeta.code.g.core.diconfig.JavaTypeRendererProvider;
import org.abstractmeta.code.g.renderer.JavaTypeRenderer;
import org.abstractmeta.toolbox.compilation.compiler.JavaSourceCompiler;
import org.abstractmeta.toolbox.compilation.compiler.impl.JavaSourceCompilerImpl;
import org.abstractmeta.toolbox.compilation.compiler.util.ClassPathUtil;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * JavaTypeCompiler
 * <p/>
 * This utility is to test JavaTypeBuilder
 *
 * @author Adrian Witas
 */
public class JavaTypeCompiler {

    private static final Logger logger = Logger.getLogger(JavaTypeCompiler.class.getName());



    public static CompiledJavaType compile(JavaTypeBuilder javaTypeBuilder) {
        return compile(javaTypeBuilder, CompiledJavaType.class.getClassLoader(), null);
    }


    public static CompiledJavaType compile(JavaTypeBuilder javaTypeBuilder, File compilationOutputDirectory) {
         return compile(javaTypeBuilder, CompiledJavaType.class.getClassLoader(), compilationOutputDirectory);
    }

    public static CompiledJavaType compile(JavaTypeBuilder javaTypeBuilder, ClassLoader classLoader) {
        return compile(javaTypeBuilder, classLoader, null);
    }


    public static CompiledJavaType compile(JavaTypeBuilder javaTypeBuilder, ClassLoader classLoader, File compilationOutputDirectory) {
        JavaTypeRenderer renderer = new JavaTypeRendererProvider().get();
        JavaTypeImporter importer = javaTypeBuilder.getImporter();
        JavaType javaType = javaTypeBuilder.build();
        importer.getGenericTypeVariables().putAll(javaType.getGenericTypeVariables());
        importer.addTypes(javaTypeBuilder.getImportTypes());
        String sourceCode = renderer.render(javaType, importer, 0);
        SourcedJavaTypeBuilder sourcedJavaTypeBuilder = new SourcedJavaTypeBuilder();
        sourcedJavaTypeBuilder.setType(javaType);
        sourcedJavaTypeBuilder.setSourceCode(sourceCode);
        SourcedJavaType sourcedType = sourcedJavaTypeBuilder.build();
        JavaSourceCompiler javaSourceCompiler = new JavaSourceCompilerImpl();

        JavaSourceCompiler.CompilationUnit compilationUnit;
        if(compilationOutputDirectory != null) {
            compilationUnit = javaSourceCompiler.createCompilationUnit(compilationOutputDirectory);
            compilationUnit.addClassPathEntry(compilationOutputDirectory.getAbsolutePath());
            compilationUnit.addClassPathEntries(ClassPathUtil.getClassPathEntries());
        } else {
            compilationUnit =  javaSourceCompiler.createCompilationUnit();
        }

        String source = "" + sourcedType.getSourceCode();
        compilationUnit.addJavaSource(sourcedType.getType().getName(), source);
        ClassLoader compilationClassLoader;
        try {
            compilationClassLoader = javaSourceCompiler.compile(classLoader, compilationUnit);
            javaSourceCompiler.persistCompiledClasses(compilationUnit);
        } catch (RuntimeException e) {
            logger.log(Level.INFO, "Failed to compile source:" + source);
            throw e;
        }
        Class compiledType = null;
        try {

            compiledType = compilationClassLoader.loadClass(sourcedType.getType().getName());
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Missing compiled type " + sourcedType.getType().getName() + " please check package name", e);
        }

        return new CompiledJavaTypeImpl(javaType, sourceCode, compilationClassLoader, compiledType);
    }


}
