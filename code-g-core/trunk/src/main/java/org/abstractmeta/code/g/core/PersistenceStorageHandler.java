package org.abstractmeta.code.g.core;

import org.abstractmeta.code.g.CodeStorageHandler;
import org.abstractmeta.code.g.code.JavaType;
import com.google.common.io.Files;

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
public class PersistenceStorageHandler implements CodeStorageHandler {

    private final File sourceRoot;
    private final List<File> generatedFiles;

    public PersistenceStorageHandler(File sourceRoot) {
        this(sourceRoot, new ArrayList<File>());
    }


    public PersistenceStorageHandler(File sourceRoot, List<File> generatedFiles) {
        this.sourceRoot = sourceRoot;
        this.generatedFiles = generatedFiles;
    }

    public void handle(JavaType javaType, CharSequence sourceCode) {
        String classFileName = javaType.getPackageName().replace('.', '/') + "/" + javaType.getSimpleName() + ".java";
        File classFile = new File(sourceRoot, classFileName);
        persistFile(classFile, sourceCode);
        generatedFiles.add(classFile);
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
}
