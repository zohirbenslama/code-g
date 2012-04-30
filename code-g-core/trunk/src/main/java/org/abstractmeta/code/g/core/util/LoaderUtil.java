package org.abstractmeta.code.g.core.util;


import com.google.common.io.Closeables;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Class loader util.
 */
public class LoaderUtil {

    private static Logger logger = Logger.getLogger(LoaderUtil.class.getName());


    public static String extractPackageName(String source) {
        source = source.replace(".*", "");
        int lastDotPosition = source.lastIndexOf('.');
        if (lastDotPosition != -1) {
            //checks first character case to work out if its full class name, or just package
            char firstClassNameChar = source.charAt(lastDotPosition + 1);
            if (firstClassNameChar >= 'A' && firstClassNameChar <= 'Z') {
                source = source.substring(0, lastDotPosition);
            }
        }
        return source;
    }

    public static String extractSimpleClassName(String className) {
        className = className.replace(".*", "");
        int lastDotPosition = className.lastIndexOf('.');
        if (lastDotPosition != -1) {
            //checks first character case to work out if its full class name, or just package
            char firstClassNameChar = className.charAt(lastDotPosition + 1);
            if (firstClassNameChar >= 'A' && firstClassNameChar <= 'Z') {
                return className.substring(lastDotPosition + 1);
            }
        }
        return null;
    }


    /**
     * Returns list of classes defined in the given package name.
     * To include all sub packages '.*' can be used as postfix.
     *
     * @param resources   package name
     * @param classLoader
     * @return list of classes
     */
    public static List<Class> getClasses(String resources, ClassLoader classLoader) {
        List<Class> classes = new ArrayList<Class>();
        try {
            if (classLoader == null) {
                classLoader = LoaderUtil.class.getClassLoader();
            }
            String path = resources.replace('.', '/');
            boolean scanSubPackage = resources.endsWith(".*");
            if (scanSubPackage) {
                int pathLength = path.length();
                path = path.substring(0, pathLength - 2);
                int packageNameLength = resources.length();
                resources = resources.substring(0, packageNameLength - 2);
            } else {
                Class clazz = getClassForResourceFile(resources, classLoader);
                if (clazz != null) {
                    classes.add(clazz);
                    return classes;
                }
            }

            List<File> directories = new ArrayList<File>();
            for (URL resource : Collections.list(classLoader.getResources(path))) {
                if ("file".equals(resource.getProtocol())) {
                    directories.add(new File(resource.getFile()));
                    for (File resourceFile : directories) {
                        if (resourceFile.isDirectory()) {
                            classes.addAll(findClasses(resourceFile, resources, classLoader, scanSubPackage));
                        } else if (resourceFile.getName().endsWith(".class")) {
                            String packageName = extractPackageName(resources);
                            Class clazz = loadClass(packageName, resourceFile, classLoader);
                            if(clazz ==  null) {
                                throw new IllegalStateException("Class eas null for " + packageName + "  " + resourceFile);
                            }
                            classes.add(clazz);
                        }
                    }
                } else if ("jar".equals(resource.getProtocol())) {
                    String jarResourceName = resource.getFile();
                    int jarIndex = jarResourceName.indexOf('!');
                    String jarFileName = resource.getFile().substring(5, jarIndex);
                    File jarFile = new File(jarFileName);
                    classes.addAll(findClassesInJar(jarFile, resources, scanSubPackage, classLoader));

                } else {
                    logger.log(Level.WARNING, String.format("Skipped resource scan - unsupported protocol: %s, for url: %s", resource.getPort(), resource));
                }
            }

            return classes;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to get class", e);
        }
    }


    public static Class getClassForResourceFile(String resource, ClassLoader classLoader) {
        String javaSourceFile = resource.replace(".", "/") + ".class";
        URL resourceURL = classLoader.getResource(javaSourceFile);
        if (resourceURL != null) {
            File resourceFile = new File(resourceURL.getFile());
            String packageName = extractPackageName(resource);
            return loadClass(packageName, resourceFile, classLoader);
        }
        return null;
    }


    /**
     * Scans the supplied directory for classes.
     *
     * @param directory      directory to scan for classes
     * @param packageName    matching package name
     * @param classLoader
     * @param scanSubPackage scan sub package flag  @return list of classes
     */
    protected static List<Class> findClasses(File directory, String packageName, ClassLoader classLoader, boolean scanSubPackage) {
        List<Class> classes = new ArrayList<Class>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        try {
            for (File file : files) {
                if (file.isDirectory() && scanSubPackage) {
                    String subPackageName = String.format("%s.%s", packageName, file.getName());
                    classes.addAll(findClasses(file, subPackageName, classLoader, scanSubPackage));
                } else if(! file.isDirectory()) {
                    Class clazz = loadClass(packageName, file, classLoader);
                    classes.add(clazz);

                }
            }
        } catch (Exception e) {
            throw new IllegalStateException("Failed to find class", e);
        }
        return classes;
    }


    protected static Class loadClass(String packageName, File file, ClassLoader classLoader) {
        Class clazz = null;
        if (file.getName().endsWith(".class")) {
            String className = String.format("%s.%s", packageName, file.getName().substring(0, file.getName().length() - 6));
            try {
                clazz = classLoader.loadClass(className);
            } catch (ClassNotFoundException e) {
                throw new IllegalStateException("Failed to lookup class " + className, e);
            }

        }
        return clazz;

    }


    /**
     * Scans the supplied jarFile's package for classes.
     *
     * @param jarFile        jar file
     * @param packageName    matching package name
     * @param scanSubPackage scan sub package flag
     * @param classLoader
     * @return list of classes
     */
    protected static List<Class> findClassesInJar(File jarFile, String packageName,
                                                  boolean scanSubPackage, ClassLoader classLoader) {
        List<Class> classes = new ArrayList<Class>();
        if (!jarFile.exists()) {
            return classes;
        }

        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(jarFile);
            JarInputStream jar = new JarInputStream(inputStream);
            JarEntry jarEntry;
            while ((jarEntry = jar.getNextJarEntry()) != null) {
                String jarEntryName = jarEntry.getName();
                if (jarEntryName.endsWith(".class")) {
                    String className = jarEntryName.replace("/", ".");
                    className = className.substring(0, className.length() - 6);
                    if (!className.startsWith(packageName)) {
                        continue;
                    }
                    Class clazz = classLoader.loadClass(className);
                    if (scanSubPackage && clazz.getPackage().getName().equals(packageName)) {
                        classes.add(clazz);
                    } else {
                        classes.add(clazz);
                    }
                }
            }

        } catch (Exception e) {
            throw new IllegalStateException("Failed to load class from jar file" + e, e);
        } finally {
            Closeables.closeQuietly(inputStream);
        }
        return classes;
    }

}
