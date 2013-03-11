package org.abstractmeta.code.g.core.config.loader;

import org.abstractmeta.code.g.code.JavaType;
import org.abstractmeta.code.g.code.SourcedJavaType;
import org.abstractmeta.code.g.config.loader.LoadedSource;

import java.util.Collection;

/**
 * Represents LoadedSourceImpl
 * <p>
 * </p>
 *
 * @author Adrian Witas
 */
public class LoadedSourceImpl implements LoadedSource {

    private ClassLoader classLoader;
    private Collection<JavaType> javaTypes;
    private Collection<SourcedJavaType> sourcedJavaTypes;


    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public Collection<JavaType> getJavaTypes() {
        return javaTypes;
    }

    public void setJavaTypes(Collection<JavaType> javaTypes) {
        this.javaTypes = javaTypes;
    }

    public Collection<SourcedJavaType> getSourcedJavaTypes() {
        return sourcedJavaTypes;
    }

    public void setSourcedJavaTypes(Collection<SourcedJavaType> sourcedJavaTypes) {
        this.sourcedJavaTypes = sourcedJavaTypes;
    }
}
