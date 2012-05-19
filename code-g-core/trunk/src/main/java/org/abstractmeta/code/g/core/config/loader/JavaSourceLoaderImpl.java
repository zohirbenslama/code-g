package org.abstractmeta.code.g.core.config.loader;

import com.google.common.io.Closeables;
import com.google.common.io.Files;
import org.abstractmeta.code.g.config.Descriptor;
import org.abstractmeta.code.g.config.loader.JavaSourceLoader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents JavaSourceLoaderImpl
 * <p>
 * </p>
 *
 * @author Adrian Witas
 */
public class JavaSourceLoaderImpl implements JavaSourceLoader {


    @Override
    public Map<String, String> load(File sourceDirectory, Iterable<Descriptor> descriptors) {
        Map<String, String> result = new HashMap<String, String>();
        for (Descriptor descriptor : descriptors) {
            if (descriptor.getSourceClass() != null) {
                loadJavaSource(sourceDirectory, descriptor.getSourceClass(), result);
            } else if (descriptor.getSourcePackage() != null) {
                String packageName = descriptor.getSourcePackage();
                boolean scanSubDirectories = packageName.endsWith(".*");
                loadJavaPackageSource(sourceDirectory, packageName.replace(".*", ""), scanSubDirectories, result);

            } else {
                throw new IllegalStateException("sourceClass and sourcePackage were null at " + descriptor);
            }
            List<String> compilationSources = descriptor.getCompilationSources();
            if (compilationSources != null) {
                for (String compilationSource : compilationSources) {
                    loadJavaPackageSource(sourceDirectory, compilationSource.replace(".*", ""), false, result);
                }
            }
        }
        return result;
    }

    private void loadJavaPackageSource(File sourceDirectory, String packageName, boolean scanSubDirectories, Map<String, String> result) {
        File packageDirectory = new File(sourceDirectory, packageName.replace(".", "/"));
        if(! packageDirectory.exists()) {
            return;
        }
        for (File candidate : packageDirectory.listFiles()) {
            if (candidate.isDirectory()) {
                if (scanSubDirectories) {
                    loadJavaPackageSource(candidate, packageName + "." + candidate.getName(), scanSubDirectories, result);
                }
            } else if (candidate.getName().endsWith(".java")) {
                String simpleName = candidate.getName().replace(".java", "");
                loadJavaSource(sourceDirectory, packageName + "." + simpleName, result);
            }
        }
    }

    private void loadJavaSource(File sourceDirectory, String sourceName, Map<String, String> result) {
        File javaSourceClass = new File(sourceDirectory, sourceName.replace(".", "/") + ".java");
        String sourceCode = loadFile(javaSourceClass);
        result.put(sourceName, sourceCode);
    }

    private String loadFile(File file) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            Files.copy(file, outputStream);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load file " + file.getAbsolutePath(), e);
        } finally {
            Closeables.closeQuietly(outputStream);
        }
        return new String(outputStream.toByteArray());
    }


}
