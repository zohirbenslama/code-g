package org.abstractmeta.code.g.core.util;

import org.abstractmeta.code.g.code.*;
import org.abstractmeta.code.g.core.code.CompiledJavaTypeImpl;
import org.abstractmeta.code.g.core.code.builder.SourcedJavaTypeBuilder;
import org.abstractmeta.code.g.core.diconfig.JavaTypeRendererProvider;
import org.abstractmeta.code.g.renderer.JavaTypeRenderer;
import org.abstractmeta.toolbox.compilation.compiler.JavaSourceCompiler;
import org.abstractmeta.toolbox.compilation.compiler.impl.JavaSourceCompilerImpl;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.LoggingMXBean;

/**
 * JavaTypeCompiler
 *
 * This utility is to test JavaTypeBuilder
 *
 * @author Adrian Witas
 */
public class JavaTypeCompiler {

    private static final Logger logger = Logger.getLogger(JavaTypeCompiler.class.getName());

    public static CompiledJavaType compile(JavaTypeBuilder javaTypeBuilder) {
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
        JavaSourceCompiler.CompilationUnit compilationUnit = javaSourceCompiler.createCompilationUnit();
        String source = "" + sourcedType.getSourceCode();
        logger.log(Level.INFO, source);

        compilationUnit.addJavaSource(sourcedType.getType().getName(), source);
        ClassLoader compilationClassLoader = javaSourceCompiler.compile(CompiledJavaType.class.getClassLoader(), compilationUnit);

        Class compiledType = null;
        try {

            compiledType = compilationClassLoader.loadClass(sourcedType.getType().getName());
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Missing compiled type " + sourcedType.getType().getName() + " please check package name", e);
        }
        return new CompiledJavaTypeImpl(javaType, sourceCode, compilationClassLoader, compiledType);
    }


}
