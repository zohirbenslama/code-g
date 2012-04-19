package org.abstractmeta.toolbox.codegen.plugin.util;

import org.abstractmeta.code.g.common.util.LoaderUtil;
import com.google.common.io.Closeables;
import com.google.common.io.Files;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PluginUtil {


    public static Map<String, String> loadJavaSources(String baseDir, String source) {
        boolean scanSubDirectories = source.contains(".*");
        String packageName = LoaderUtil.extractPackageName(source);
        String simpleClassName = LoaderUtil.extractSimpleClassName(source);
        File baseDirectory = new File(baseDir);
        File packageDirectory = new File(baseDirectory, "src/main/java/" + packageName.replace('.', '/'));
        if (simpleClassName != null) {
            return loadJavaSingleSource(packageDirectory, packageName, simpleClassName);
        }
        return loadJavaSources(packageName, packageDirectory, scanSubDirectories);

    }

    protected static Map<String, String> loadJavaSingleSource(File packageDirectory, String packageName, String simpleClassName) {
        File javaFile = new File(packageDirectory.getAbsolutePath() + "/" + simpleClassName + ".java");
        if (!javaFile.exists()) {
            return Collections.emptyMap();
        }
        Map<String, String> result = new HashMap<String, String>();
        loadJavaSourceFile(result, packageName, javaFile);
        return result;
    }

    protected static Map<String, String> loadJavaSources(String packageName, File directory, boolean scanSubDirectories) {
        Map<String, String> result = new HashMap<String, String>();
        for (File candidate : directory.listFiles()) {
            if (candidate.isDirectory()) {
                if (scanSubDirectories)
                    result.putAll(loadJavaSources(packageName + "." + candidate.getName(), candidate, scanSubDirectories));
                continue;
            }
            loadJavaSourceFile(result, packageName, candidate);
        }
        return result;
    }


    protected static void loadJavaSourceFile(Map<String, String> result, String packageName, File javaFileCandidate) {
        if (javaFileCandidate.getName().endsWith(".java")) {
            String className = packageName + "." + javaFileCandidate.getName();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            try {
                Files.copy(javaFileCandidate, outputStream);
                result.put(className.replace(".java", ""), new String(outputStream.toByteArray()));
            } catch (IOException e) {
                throw new IllegalStateException("Failed to load file " + javaFileCandidate.getAbsolutePath(), e);
            } finally {
                Closeables.closeQuietly(outputStream);
            }
        }
    }

}
