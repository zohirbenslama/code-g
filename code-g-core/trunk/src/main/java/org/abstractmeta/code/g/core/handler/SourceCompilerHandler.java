package org.abstractmeta.code.g.core.handler;

import org.abstractmeta.code.g.handler.CodeHandler;
import org.abstractmeta.code.g.code.JavaType;
import org.abstractmeta.toolbox.compilation.compiler.JavaSourceCompiler;
import org.abstractmeta.toolbox.compilation.compiler.impl.JavaSourceCompilerImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * Source code compiler handler.
 *
 * @author Adrian Witas
 */
public class SourceCompilerHandler implements CodeHandler {

    private final  JavaSourceCompiler javaSourceCompiler;
    private final JavaSourceCompiler.CompilationUnit compilationUnit;
    private final List<String> generatedTypes = new ArrayList<String>();

    public SourceCompilerHandler() {
        this.javaSourceCompiler = new JavaSourceCompilerImpl();
        this.compilationUnit = javaSourceCompiler.createCompilationUnit();
    }

    @Override
    public void handle(JavaType javaType, CharSequence sourceCode) {
        compilationUnit.addJavaSource(javaType.getName(), sourceCode.toString());
        generatedTypes.add(javaType.getName());
    }

    public ClassLoader compile() {
       return  javaSourceCompiler.compile(compilationUnit);
    }

    public ClassLoader compile(ClassLoader parentClassLoader) {
       return  javaSourceCompiler.compile(parentClassLoader, compilationUnit);
    }


    public JavaSourceCompiler.CompilationUnit getCompilationUnit() {
        return compilationUnit;
    }

    public List<String> getGeneratedTypes() {
        return generatedTypes;
    }

    public JavaSourceCompiler getJavaSourceCompiler() {
        return javaSourceCompiler;
    }

}
