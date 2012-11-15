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
package org.abstractmeta.code.g.core.handler;

import org.abstractmeta.code.g.code.SourcedJavaType;
import org.abstractmeta.code.g.config.UnitDescriptor;
import org.abstractmeta.code.g.handler.CodeHandler;
import org.abstractmeta.code.g.code.JavaType;
import org.abstractmeta.code.g.handler.CodeHandlerFactory;
import org.abstractmeta.toolbox.compilation.compiler.JavaSourceCompiler;
import org.abstractmeta.toolbox.compilation.compiler.impl.JavaSourceCompilerImpl;

import javax.annotation.Nullable;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Source code compiler handler.
 *
 * @author Adrian Witas
 */
public class SourceCompilerHandler implements CodeHandler {

    private final JavaSourceCompiler javaSourceCompiler;
    private final JavaSourceCompiler.CompilationUnit compilationUnit;
    private final List<String> generatedTypes = new ArrayList<String>();
    private final File rootDirectory;

    public SourceCompilerHandler() {
        this(null);
    }

    public SourceCompilerHandler(@Nullable File rootDirectory) {
        this.rootDirectory = rootDirectory;
        this.javaSourceCompiler = new JavaSourceCompilerImpl();
        if (rootDirectory == null) {
            this.compilationUnit = javaSourceCompiler.createCompilationUnit();

        } else {
            this.compilationUnit = javaSourceCompiler.createCompilationUnit(rootDirectory);
        }
    }

    @Override
    public void handle(SourcedJavaType sourcedJavaType) {
        CharSequence sourceCode = sourcedJavaType.getSourceCode();
        JavaType javaType = sourcedJavaType.getType();
        if (sourceCode == null) {
            throw new IllegalArgumentException("sourceCode was null for " + javaType);
        }
        compilationUnit.addJavaSource(javaType.getName(), sourceCode.toString());
        generatedTypes.add(javaType.getName());
    }

    @Override
    public File getRootDirectory() {
        return rootDirectory;
    }

    public ClassLoader compile() {
        return javaSourceCompiler.compile(compilationUnit);
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


     /**
     * Represents  PersistenceCodeHandlerFactory.
     *
     */
    public static class Factory implements CodeHandlerFactory {
        @Override
        public CodeHandler create(File rootDirectory) {
            return new SourceCompilerHandler(rootDirectory);
        }
    }


}
