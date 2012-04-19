package org.abstractmeta.toolbox.codegen.plugin;


import org.abstractmeta.code.g.CodeBuilder;
import org.abstractmeta.code.g.common.CodeBuilderImpl;
import org.abstractmeta.code.g.common.PersistenceStorageHandler;
import org.abstractmeta.code.g.common.plugin.ClassGeneratorPlugin;
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
            codeBuilder.getPluginLoader().addPluginPackageName(ClassGeneratorPlugin.class.getPackage().getName());
            for (Descriptor descriptor : descriptors) {
                codeBuilder.addDescriptor(descriptor);
            }
            if (pluginPackages != null && !pluginPackages.isEmpty()) {
                for (String pluginPackage : pluginPackages) {
                    codeBuilder.getPluginLoader().addPluginPackageName(pluginPackage);
                }
            }

            PersistenceStorageHandler persistenceHandler = new PersistenceStorageHandler(new File(targetSourceDirectory));
            codeBuilder.build(persistenceHandler);
            return  persistenceHandler.getGeneratedFiles();

        } catch (Exception e) {
            throw new IllegalStateException("Failed to generate code", e);
        }
    }

   
    public Map<String, String> getCompilationSources(List<Descriptor> descriptors, String baseDir) {
        Map<String, String> result = new HashMap<String, String>();
        for (Descriptor descriptor : descriptors) {
            String source = descriptor.getSource();
            result.putAll(PluginUtil.loadJavaSources(baseDir, source));
            List<String> compilationSources = descriptor.getCompilationSources();
            if(compilationSources != null) {
                for(String compilationSource: compilationSources) {
                    result.putAll(PluginUtil.loadJavaSources(baseDir, compilationSource));
                }
            }
        }
        return result;
    }


}
