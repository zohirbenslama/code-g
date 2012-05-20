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

import com.google.common.io.Closeables;
import org.abstractmeta.code.g.code.JavaType;
import org.abstractmeta.code.g.code.JavaTypeRegistry;
import org.abstractmeta.code.g.config.Descriptor;
import org.abstractmeta.code.g.config.loader.JavaTypeLoader;
import org.abstractmeta.code.g.core.provider.ClassTypeProvider;
import org.abstractmeta.code.g.core.util.StringUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

/**
 * Provide implementation of JavaTypeLoader interface.
 *
 * @author Adrian Witas
 */
public class JavaTypeLoaderImpl implements JavaTypeLoader {

    @Override
    public Collection<String> load(JavaTypeRegistry registry, Descriptor descriptor, ClassLoader classLoader) {
        try {
            if (classLoader == null) {
                classLoader = descriptor.getClass().getClassLoader();
            }
            if (descriptor.getSourceClass() != null) {
                String loadedClass = loadSourceClass(registry, descriptor.getSourceClass(), classLoader);
                if (loadedClass == null) {
                    throw new IllegalStateException("Failed to load class " + descriptor.getSourceClass());
                }
                return Arrays.asList(loadedClass);
            } else if (descriptor.getSourcePackage() != null) {
                Collection<String> result = new ArrayList<String>();
                result.addAll(loadFromRegistry(registry, descriptor));
                result.addAll(loadPackageClasses(registry, descriptor, classLoader));
                if (!result.isEmpty()) {
                    return result;
                }

            }
        } catch (NullPointerException e) {
            if (descriptor == null) {
                throw new NullPointerException("descriptor was null");
            }
            throw e;
        }
        throw new IllegalStateException("Failed to load source - no class or package specified at " + descriptor);
    }

    private Collection<String> loadFromRegistry(JavaTypeRegistry registry, Descriptor descriptor) {
        String sourcePackage = descriptor.getSourcePackage();
        Collection<String> result = new ArrayList<String>();
        boolean wildcard = sourcePackage.endsWith(".*");
        if(wildcard) sourcePackage = sourcePackage.replace(".*", "");
        for (JavaType type : registry.get()) {
            if(wildcard) {
                if (type.getPackageName().startsWith(sourcePackage)) {
                    result.add(type.getName());
                }
            } else if (type.getPackageName().equals(sourcePackage)) {
                result.add(type.getName());
            }
        }
        return result;
    }


    private Collection<String> loadPackageClasses(JavaTypeRegistry registry, Descriptor descriptor, ClassLoader classLoader) {
        String internalPackageName = descriptor.getSourcePackage().replace(".", "/");
        int wildcardIndex = internalPackageName.indexOf('*');
        if (wildcardIndex > 0) { // * as package is an invalid option
            internalPackageName = internalPackageName.substring(0, wildcardIndex - 1);
        }
        List<String> result = new ArrayList<String>();
        try {
            Enumeration<URL> urls = classLoader.getResources(internalPackageName);
            for (URL resource : Collections.list(urls)) {
                loadResource(resource, internalPackageName, descriptor, classLoader, registry, result);
            }

        } catch (IOException e) {
            throw new IllegalStateException("Failed to load source " + descriptor, e);
        }
        return result;
    }

    private void loadResource(URL resource, String internalPackageName, Descriptor descriptor, ClassLoader classLoader, JavaTypeRegistry registry, List<String> result) throws MalformedURLException {
        String protocol = resource.getProtocol();
        String resourceName = resource.getFile();
        if (resourceName.endsWith(".class")) {
            String className = getClassName(resourceName, internalPackageName, descriptor);
            if (className == null) return;
            loadSourceClass(registry, className, classLoader);
            result.add(className);
        }

        if ("file".equals(protocol)) {
            boolean wildcard = descriptor.getSourcePackage().endsWith(".*");
            File resourceFile = new File(resourceName);
            if (resourceFile.isDirectory()) {
                for (File file : resourceFile.listFiles()) {
                    String relativeFile = "";
                    if (file.isDirectory()) {
                        if (!wildcard) continue;
                        relativeFile = "/" + file.getName();
                    }
                    loadResource(file.toURI().toURL(), internalPackageName + relativeFile, descriptor, classLoader, registry, result);
                }
            }
        } else if ("jar".equals(protocol)) {
            int jarIndex = resourceName.indexOf('!');
            String jarFileName = resourceName.substring(5, jarIndex);
            loadResourceFromJar(new File(jarFileName), internalPackageName, descriptor, classLoader, registry, result);

        } else {
            throw new IllegalStateException("Unsupported protocol " + protocol + " while loaded resource " + resourceName + " for " + descriptor);
        }


    }

    private void loadResourceFromJar(File jarFile, String internalPackageName, Descriptor descriptor, ClassLoader classLoader, JavaTypeRegistry registry, List<String> result) {
        InputStream inputStream = null;
        boolean wildcard = descriptor.getSourcePackage().endsWith(".*");
        try {
            inputStream = new FileInputStream(jarFile);
            JarInputStream jar = new JarInputStream(inputStream);
            JarEntry jarEntry;
            while ((jarEntry = jar.getNextJarEntry()) != null) {
                String resourceCandidate = jarEntry.getName();
                if (resourceCandidate.startsWith(internalPackageName) && resourceCandidate.endsWith(".class")) {
                    String className = resourceCandidate.replace("/", ".").replace(".class", "");
                    if (!wildcard) {
                        String packageName = StringUtil.substringBeforeLastIndexOf(className, ".");
                        if (packageName.length() != internalPackageName.length()) {
                            continue;
                        }
                    }
                    String simpleName = StringUtil.substringAfterLastIndexOf(className, ".");
                    if (!isAllowed(descriptor, simpleName)) {
                        continue;
                    }
                    loadSourceClass(registry, className, classLoader);
                    result.add(className);
                }
            }

        } catch (Exception e) {
            throw new IllegalStateException("Failed to load class from jar file" + e, e);
        } finally {
            Closeables.closeQuietly(inputStream);
        }
    }

    protected String getClassName(String resourceName, String internalPackageName, Descriptor descriptor) {
        int simpleClassNameEndPosition = resourceName.lastIndexOf('.');
        int simpleClassNameStartPosition = resourceName.lastIndexOf('/');
        String simpleClassName = resourceName.substring(simpleClassNameStartPosition + 1, simpleClassNameEndPosition);
        if (!isAllowed(descriptor, simpleClassName)) {
            return null;
        }
        return internalPackageName.replace("/", ".") + "." + simpleClassName;
    }

    protected boolean isAllowed(Descriptor descriptor, String simpleClassName) {
        Set<String> exclusions = descriptor.getExclusions();
        Set<String> inclusions = descriptor.getInclusions();
        return !(exclusions != null && !exclusions.isEmpty() && descriptor.getExclusions().contains(simpleClassName))
                && !(inclusions != null && !inclusions.isEmpty() && !inclusions.contains(simpleClassName));

    }

    protected String loadSourceClass(JavaTypeRegistry registry, String sourceClass, ClassLoader classLoader) {
        if (registry.isRegistered(sourceClass)) {
            return sourceClass;
        }
        try {
            Class result = classLoader.loadClass(sourceClass);
            JavaType javaType = new ClassTypeProvider(result).get();
            registry.register(javaType);
            return result.getName();
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Failed to load class " + sourceClass, e);
        }
    }
}
