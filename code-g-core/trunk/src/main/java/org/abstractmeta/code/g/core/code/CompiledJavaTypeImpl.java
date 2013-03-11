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

    public CompiledJavaTypeImpl(JavaType type, CharSequence sourceCode, ClassLoader classLoader) {
        super(type, sourceCode);
        this.classLoader = classLoader;
    }

    @Override
    public ClassLoader getClassLoader() {
        return classLoader;
    }
}
