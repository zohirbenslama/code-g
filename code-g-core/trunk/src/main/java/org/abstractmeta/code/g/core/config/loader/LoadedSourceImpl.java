package org.abstractmeta.code.g.core.config.loader;

import org.abstractmeta.code.g.code.CompiledJavaType;
import org.abstractmeta.code.g.code.JavaType;
import org.abstractmeta.code.g.config.loader.LoadedSource;
import org.abstractmeta.toolbox.compilation.compiler.JavaSourceCompiler;
import org.abstractmeta.toolbox.compilation.compiler.registry.JavaFileObjectRegistry;

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
    private Collection<CompiledJavaType> compiledJavaTypes;

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

    public Collection<CompiledJavaType> getCompiledJavaTypes() {
        return compiledJavaTypes;
    }

    public void setCompiledJavaTypes(Collection<CompiledJavaType> compiledJavaTypes) {
        this.compiledJavaTypes = compiledJavaTypes;
    }


}
