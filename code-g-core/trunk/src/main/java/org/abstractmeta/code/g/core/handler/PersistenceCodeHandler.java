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

import com.google.common.io.Closeables;
import org.abstractmeta.code.g.code.SourcedJavaType;
import org.abstractmeta.code.g.config.UnitDescriptor;
import org.abstractmeta.code.g.core.code.builder.JavaTypeBuilder;
import org.abstractmeta.code.g.handler.CodeHandler;
import org.abstractmeta.code.g.code.JavaType;
import com.google.common.io.Files;
import org.abstractmeta.code.g.handler.CodeHandlerFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Persistence handler.
 * It saves generated code in the file system under source root directory.
 *
 * @author Adrian Witas
 */
public class PersistenceCodeHandler implements CodeHandler {

    private final File rootDirectory;
    private final List<File> generatedFiles;
    private final List<String> generatedTypeNames;


    public PersistenceCodeHandler(File rootDirectory) {
        this(rootDirectory, new ArrayList<File>(), new ArrayList<String>());
    }


    public PersistenceCodeHandler(File rootDirectory, List<File> generatedFiles, List<String> generatedTypeNames) {
        this.rootDirectory = rootDirectory;
        this.generatedFiles = generatedFiles;
        this.generatedTypeNames = generatedTypeNames;
    }

    protected boolean isOverridable(File classFile) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            Files.copy(classFile, outputStream);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to loadColumnFieldMap class " + classFile.getAbsolutePath(), e);
        } finally {
            Closeables.closeQuietly(outputStream);
        }
        String content = new String(outputStream.toByteArray());
        return content.contains(JavaTypeBuilder.CODE_G_GENERATOR_SIGNATURE);
    }

    protected void persistFile(File classFile, CharSequence sourceCode) {
        try {
            Files.createParentDirs(classFile);
            Files.write(sourceCode.toString().getBytes(), classFile);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to render code", e);
        }
    }

    public List<File> getGeneratedFiles() {
        return generatedFiles;
    }


    public List<String> getGeneratedTypeNames() {
        return generatedTypeNames;
    }

    @Override
    public void handle(SourcedJavaType sourcedJavaType) {
        File classFile = sourcedJavaType.getFile();
        if (classFile.exists()) {
            if (!isOverridable(classFile)) {
                return;
            }
        }
        persistFile(classFile, sourcedJavaType.getSourceCode());
        generatedTypeNames.add(sourcedJavaType.getType().getName());
        generatedFiles.add(classFile);
    }

    @Override
    public File getRootDirectory() {
        return rootDirectory;
    }

    @Override
    public ClassLoader compile() {
        return null;
    }

    @Override
    public ClassLoader compile(ClassLoader classLoader) {
        return classLoader;
    }

    /**
     * Represents  PersistenceCodeHandlerFactory.
     */
    public static class Factory implements CodeHandlerFactory {
        @Override
        public CodeHandler create(File rootDirectory) {
            return new PersistenceCodeHandler(rootDirectory);
        }
    }
}
