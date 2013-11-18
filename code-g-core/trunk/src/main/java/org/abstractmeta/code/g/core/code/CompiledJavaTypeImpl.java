package org.abstractmeta.code.g.core.code;

import org.abstractmeta.code.g.code.CompiledJavaType;
import org.abstractmeta.code.g.code.JavaType;

/**
 * Compiled java type.
 *
 * @author Adrian Witas
 */
public class CompiledJavaTypeImpl extends SourcedJavaTypeImpl implements CompiledJavaType {

    private final ClassLoader classLoader;
    private final Class compiledType;
    private final String rootClassPath;

    public CompiledJavaTypeImpl(JavaType type, CharSequence sourceCode, ClassLoader classLoader, Class compiledType, String rootClassPath) {
        super(type, sourceCode);
        this.classLoader = classLoader;
        this.compiledType = compiledType;
        this.rootClassPath = rootClassPath;
    }

    @Override
    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public Class getCompiledType() {
        return compiledType;
    }

    @Override
    public String getRootClassPath() {
        return rootClassPath;
    }
}
