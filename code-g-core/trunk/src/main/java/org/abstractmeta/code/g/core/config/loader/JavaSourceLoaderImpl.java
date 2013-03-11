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
package org.abstractmeta.code.g.core.config.loader;

import com.google.common.base.Preconditions;
import com.google.common.collect.Collections2;
import com.google.common.io.Closeables;
import com.google.common.io.Files;
import org.abstractmeta.code.g.code.CompiledJavaType;
import org.abstractmeta.code.g.code.JavaType;
import org.abstractmeta.code.g.code.JavaTypeRegistry;
import org.abstractmeta.code.g.code.SourcedJavaType;
import org.abstractmeta.code.g.config.SourceFilter;
import org.abstractmeta.code.g.config.loader.LoadedSource;
import org.abstractmeta.code.g.config.loader.SourceLoader;
import org.abstractmeta.code.g.core.code.CompiledJavaTypeImpl;
import org.abstractmeta.code.g.core.code.SourcedJavaTypeImpl;
import org.abstractmeta.code.g.core.provider.ClassTypeProvider;
import org.abstractmeta.toolbox.compilation.compiler.JavaSourceCompiler;
import org.abstractmeta.toolbox.compilation.compiler.impl.JavaSourceCompilerImpl;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * SourceLoader implementation.
 *
 * @author Adrian Witas
 */
public class JavaSourceLoaderImpl implements SourceLoader {

    private final Logger logger = Logger.getLogger(JavaSourceLoaderImpl.class.getName());

    @Override
    public LoadedSource load(SourceFilter sourceFilter, JavaTypeRegistry registry, ClassLoader classLoader) {
        Preconditions.checkNotNull(sourceFilter, "sourceFilter was null");
        Preconditions.checkNotNull(registry, "registry was null");
        LoadedSourceImpl result = new LoadedSourceImpl();
        Map<String, String> javaSources = loadFromSourceDirectory(sourceFilter);
        Collection<JavaType> javaTypes = loadFromClassLoader(sourceFilter, classLoader);
        if(! javaSources.isEmpty()) {
            javaTypes.addAll(compileSources(javaSources, classLoader, result));
        }
        for(JavaType javaType: javaTypes) {
            if(! registry.isRegistered(javaType.getName())) {
                 registry.register(javaType);
            }
        }
        Collection<JavaType> filteredTypes = applyFilter(sourceFilter, registry);
        result.setJavaTypes(filteredTypes);
        Collection<CompiledJavaType> sourcedTypes = getCompiledJavaTypes(javaSources, registry, classLoader);
        result.setCompiledJavaTypes(sourcedTypes);
        return result;
    }

    protected Collection<JavaType> applyFilter(SourceFilter sourceFilter, JavaTypeRegistry registry) {
        return Collections2.filter(registry.get(), new SourceFilterPredicate(sourceFilter));
    }

    protected Collection<CompiledJavaType> getCompiledJavaTypes(Map<String, String> javaSources, JavaTypeRegistry registry, ClassLoader classLoader) {
        Collection<CompiledJavaType> result = new ArrayList<CompiledJavaType>();
        for(Map.Entry<String, String> entry: javaSources.entrySet()) {
            JavaType javaType = registry.get(entry.getKey());
            result.add(new CompiledJavaTypeImpl(javaType, entry.getValue(), classLoader));
        }
        return result;
    }

    protected Collection<JavaType> compileSources(Map<String, String> javaSources, ClassLoader classLoader, LoadedSourceImpl loadedSources) {
        Collection<JavaType> result = new ArrayList<JavaType>();
        JavaSourceCompiler javaSourceCompiler = new JavaSourceCompilerImpl();
        JavaSourceCompiler.CompilationUnit compilationUnit = javaSourceCompiler.createCompilationUnit();
        for(Map.Entry<String, String> entry: javaSources.entrySet()) {
            compilationUnit.addJavaSource(entry.getKey(), entry.getValue());
        }
        ClassLoader compilationClassLoader = javaSourceCompiler.compile(classLoader, compilationUnit);
        loadedSources.setClassLoader(compilationClassLoader);
        for(String className: javaSources.keySet()) {
            if(exists(className, compilationClassLoader)) {
                result.add(loadClass(className, compilationClassLoader));
            } else {
                logger.log(Level.WARNING, "Missing class in compiled source class loader " + className);
            }
        }
        return result;
    }



    protected Collection<JavaType> loadFromClassLoader(SourceFilter sourceFilter, ClassLoader classLoader) {
        Collection<JavaType> result = new ArrayList<JavaType>();
        if (classLoader == null) return result;
        loadClassesWithClassLoader(sourceFilter, result, classLoader);
        loadPackagesWithClassLoader(sourceFilter, result, classLoader);
        return result;
    }

    protected void loadClassesWithClassLoader(SourceFilter sourceFilter, Collection<JavaType> result, ClassLoader classLoader) {
        if (sourceFilter.getClassNames() == null) return;
        for (String className : sourceFilter.getClassNames()) {
            if (exists(className, classLoader)) {
                result.add(loadClass(className, classLoader));
            }
        }
    }


    protected void loadPackagesWithClassLoader(SourceFilter sourceFilter, Collection<JavaType> result, ClassLoader classLoader) {
        if (sourceFilter.getPackageNames() == null) return;
        Set<String> classNames = new HashSet<String>();
        for (String packageName : sourceFilter.getPackageNames()) {
            String internalPackageName = packageName.replace(".", "/");
            try {
                Enumeration<URL> packageUrls = classLoader.getResources(internalPackageName);
                if (packageUrls == null) continue;
                for (URL packageUrl : Collections.list(packageUrls)) {
                    classNames.addAll(loadPackagesWithClassLoader(packageName, packageUrl, sourceFilter.isIncludeSubpackages()));
                }
            } catch (IOException e) {
                throw new IllegalStateException("Failed to load package " + internalPackageName);
            }

        }
        if(logger.isLoggable(Level.FINE)) {
            logger.log(Level.FINE, "Loaded classes from package with class loader" + classNames);
        }
        for (String className : classNames) {
            if (exists(className, classLoader)) {
                result.add(loadClass(className, classLoader));
            }
        }
    }


    protected Collection<String> loadPackagesWithClassLoader(String packageName, URL packageUrl, boolean includeSubPackages) {
        Collection<String> result = new ArrayList<String>();
        String protocol = packageUrl.getProtocol();
        String fileName = packageUrl.getFile().replace("file:", "");
        if ("file".equals(protocol)) {
            result.addAll(loadPackageClassesFromFileSystem(fileName, packageName, includeSubPackages));
        } else if ("jar".equals(protocol)) {
            result.addAll(loadPackageClassesFromJarFile(fileName, packageName, includeSubPackages));
        } else {
            logger.log(Level.WARNING, "Unsupported package protocol " + protocol + " on " + packageUrl);
        }
        return result;
    }


    protected Collection<String> loadPackageClassesFromFileSystem(String fileName, String packageName, boolean includeSubPackages) {
        Collection<String> result = new ArrayList<String>();
        File directory = new File(fileName);
        if (directory.isDirectory()) {
            File [] files = directory.listFiles();
            if(files == null) return result;
            for(File file: files) {
                if(file.isDirectory()) {
                    if(includeSubPackages) {
                        result.addAll(loadPackageClassesFromFileSystem(file.getAbsolutePath(), packageName + "." + file.getName(), includeSubPackages));
                    }
                } else if(file.getName().endsWith(".class")) {
                    result.add(packageName + "." + file.getName().replace(".class", ""));
                }
            }
        }
        return result;
    }


    protected Collection<String> loadPackageClassesFromJarFile(String jarPackagePath, String packageName, boolean includeSubPackages) {
        Collection<String> result = new ArrayList<String>();
        int jarFileEndPosition = jarPackagePath.indexOf('!');
        String jarFileName = jarPackagePath.substring(0, jarFileEndPosition);
        String internalPackageName = packageName.replace(".", "/");
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(jarFileName);
            JarInputStream jar = new JarInputStream(inputStream);
            JarEntry jarEntry;
            while ((jarEntry = jar.getNextJarEntry()) != null) {
                String resourceCandidate = jarEntry.getName();
                if (resourceCandidate.startsWith(internalPackageName) && resourceCandidate.endsWith(".class")) {
                    String className = resourceCandidate.replace("/", ".").replace(".class", "");
                    if(includeSubPackages) {
                        result.add(className);
                    } else {
                        String simpleClassName = className.substring(0, packageName.length() + 1);
                        if(simpleClassName.indexOf('.') != -1) result.add(className);
                    }
                }
            }
            return result;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to loadColumnFieldMap class from jar file" + e, e);
        } finally {
            Closeables.closeQuietly(inputStream);
        }
    }

    /**
     * Check is a given class can be loaded by the supplied class loader
     * @param className
     * @param classLoader
     * @return
     */
    protected boolean exists(String className, ClassLoader classLoader) {
        try {
            classLoader.loadClass(className);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    protected JavaType loadClass(String className, ClassLoader classLoader) {
        try {
            Class result = classLoader.loadClass(className);
            return new ClassTypeProvider(result).get();
        } catch (Exception e) {
            throw new IllegalStateException("Failed to load " + className, e);
        }
    }


    /**
     * Returns a map of class name, source code for a given source filter.
     *
     * @param sourceFilter source filter
     * @return of class name, source code map
     */
    protected Map<String, String> loadFromSourceDirectory(SourceFilter sourceFilter) {
        Map<String, String> result = new HashMap<String, String>();
        if (sourceFilter.getSourceDirectory() == null) return result;
        loadClassesFromSourceDirectory(sourceFilter, result);
        loadPackagesFromSourceDirectory(sourceFilter, result);
        return result;
    }

    protected void loadPackagesFromSourceDirectory(SourceFilter sourceFilter, Map<String, String> result) {
        if (sourceFilter.getPackageNames() == null) return;
        for (String packageName : sourceFilter.getPackageNames()) {
            loadPackage(sourceFilter, packageName, result);
        }
    }


    protected void loadPackage(SourceFilter sourceFilter, String packageName, Map<String, String> result) {
        File packageDirectory = new File(new File(sourceFilter.getSourceDirectory()), packageName.replace(".", "/"));
        File[] files = packageDirectory.listFiles();
        if (!packageDirectory.exists() || files == null) {
            return;
        }

        for (File candidate : files) {
            if (candidate.isDirectory()) {
                if (sourceFilter.isIncludeSubpackages()) {
                    loadPackage(sourceFilter, packageName + "." + candidate.getName(), result);
                }
            } else if (candidate.getName().endsWith(".java")) {
                String simpleClassName = candidate.getName().replace(".java", "");
                String className = packageName + "." + simpleClassName;
                String sourceCode = loadSourceCode(sourceFilter.getSourceDirectory(), className);
                result.put(className, sourceCode);
            }
        }
    }

    protected void loadClassesFromSourceDirectory(SourceFilter sourceFilter, Map<String, String> result) {
        if (sourceFilter.getClassNames() == null) return;
        for (String className : sourceFilter.getClassNames()) {
            if (exists(sourceFilter.getSourceDirectory(), className)) {
                String sourceCode = loadSourceCode(sourceFilter.getSourceDirectory(), className);
                result.put(className, sourceCode);
            }
        }
    }

    protected boolean exists(String sourceDirectory, String sourceName) {
        return new File(new File(sourceDirectory), sourceName.replace(".", "/") + ".java").exists();
    }

    protected String loadSourceCode(String sourceDirectory, String sourceName) {
        File javaSourceClass = new File(new File(sourceDirectory), sourceName.replace(".", "/") + ".java");
        ByteArrayOutputStream outputStream = null;
        try {
            outputStream = new ByteArrayOutputStream();
            Files.copy(javaSourceClass, outputStream);
            return new String(outputStream.toByteArray());
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read  " + javaSourceClass.getAbsolutePath(), e);
        } finally {
            Closeables.closeQuietly(outputStream);
        }
    }


}
