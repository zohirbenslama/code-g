package org.abstractmeta.toolbox.codegen.plugin;


import org.abstractmeta.code.g.CodeBuilder;
import org.abstractmeta.code.g.core.CodeBuilderImpl;
import org.abstractmeta.code.g.core.PersistenceStorageHandler;
import org.abstractmeta.toolbox.codegen.plugin.util.PluginUtil;
import org.abstractmeta.toolbox.compilation.compiler.JavaSourceCompiler;
import org.abstractmeta.toolbox.compilation.compiler.impl.JavaSourceCompilerImpl;

import java.io.File;
import java.util.*;

/**
 * Represents CodeGenerator.
 *
 * @author Adrian Witas
 */

public class CodeGenerator {


    public Collection<File> generate(List<Descriptor> descriptors, List<String> classPathEntries, String targetSourceDirectory, String basedir, List<String> pluginPackages) {
        try {
            JavaSourceCompiler javaSourceCompiler = new JavaSourceCompilerImpl();

            JavaSourceCompiler.CompilationUnit compilationUnit = javaSourceCompiler.createCompilationUnit();
            compilationUnit.addClassPathEntries(classPathEntries);
            Map<String, String> javaSources = getCompilationSources(descriptors, basedir);
            for (String className : javaSources.keySet()) {
                compilationUnit.addJavaSource(className, javaSources.get(className));
            }
            ClassLoader classLoader = javaSourceCompiler.compile(compilationUnit);



            CodeBuilder codeBuilder = new CodeBuilderImpl();
            for (Descriptor descriptor : descriptors) {
                codeBuilder.addDescriptor(descriptor);
            }
            if (pluginPackages != null && ! pluginPackages.isEmpty()) {
                for (String pluginPackage : pluginPackages) {
                    codeBuilder.getPluginLoader().addPluginPackageName(pluginPackage);
                }
            }

            PersistenceStorageHandler storageHandler = new PersistenceStorageHandler(new File(targetSourceDirectory));
            codeBuilder.build(storageHandler, classLoader);
            return storageHandler.getGeneratedFiles();

        } catch (Exception e) {
            throw new IllegalStateException("Failed to generate code", e);
        }
    }


    public Map<String, String> getCompilationSources(List<Descriptor> descriptors, String baseDir) {
        Map<String, String> result = new HashMap<String, String>();
        for (Descriptor descriptor : descriptors) {
            String source = descriptor.getSourceClass();
            Map<String, String> descriptorSource = PluginUtil.loadJavaSources(baseDir, source);
            if (descriptorSource != null) {
                result.putAll(descriptorSource);
            }
            List<String> compilationSources = descriptor.getCompilationSources();
            if (compilationSources != null) {
                for (String compilationSource : compilationSources) {
                    result.putAll(PluginUtil.loadJavaSources(baseDir, compilationSource));
                }
            }
        }
        return result;
    }


}
