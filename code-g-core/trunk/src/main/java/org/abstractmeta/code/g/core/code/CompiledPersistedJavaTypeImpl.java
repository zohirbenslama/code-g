package org.abstractmeta.code.g.core.code;

import org.abstractmeta.code.g.code.CompiledJavaType;
import org.abstractmeta.code.g.code.JavaType;
import org.abstractmeta.code.g.code.PersistedJavaCode;

/**
 * Provides persisted compiled java type implementation
 */
public class CompiledPersistedJavaTypeImpl<T> implements CompiledJavaType<T>, PersistedJavaCode<T> {

    private final String javaFilePath;
    private final CompiledJavaType<T> compiledJavaType;

    public CompiledPersistedJavaTypeImpl(String javaFilePath, CompiledJavaType<T> compiledJavaType) {
        this.javaFilePath = javaFilePath;
        this.compiledJavaType = compiledJavaType;
    }



    @Override
    public ClassLoader getClassLoader() {
        return compiledJavaType.getClassLoader();
    }

    @Override
    public Class<T> getCompiledType() {
        return compiledJavaType.getCompiledType();
    }

    @Override
    public String getRootClassPath() {
        return compiledJavaType.getRootClassPath();
    }

    @Override
    public JavaType getType() {
        return compiledJavaType.getType();
    }

    @Override
    public CharSequence getSourceCode() {
        return compiledJavaType.getSourceCode();
    }

    @Override
    public String getJavaFilePath() {
        return javaFilePath;
    }
}
