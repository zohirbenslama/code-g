package org.abstractmeta.code.g.config.loader;

import org.abstractmeta.code.g.code.CompiledJavaType;
import org.abstractmeta.code.g.code.JavaType;
import org.abstractmeta.code.g.code.SourcedJavaType;

import java.util.Collection;

/**
 * Represents LoadedSource
 *
 * @author Adrian Witas
 */
public interface LoadedSource {

    ClassLoader getClassLoader();

    Collection<JavaType> getJavaTypes();

    Collection<CompiledJavaType> getCompiledJavaTypes();

}
