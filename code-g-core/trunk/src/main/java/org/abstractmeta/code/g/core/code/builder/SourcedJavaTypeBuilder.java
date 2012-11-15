package org.abstractmeta.code.g.core.code.builder;

import org.abstractmeta.code.g.code.JavaType;
import org.abstractmeta.code.g.code.SourcedJavaType;
import org.abstractmeta.code.g.core.code.SourcedJavaTypeImpl;

import java.io.File;

/**
 * Represents  SourcedJavaTypeBuilder.
 */
public class SourcedJavaTypeBuilder {
    
    private JavaType type;
    private CharSequence sourceCode;
    private File file;

    public JavaType getType() {
        return type;
    }

    public SourcedJavaTypeBuilder setType(JavaType type) {
        this.type = type;
        return this;
    }

    public CharSequence getSourceCode() {
        return sourceCode;
    }

    public SourcedJavaTypeBuilder setSourceCode(CharSequence sourceCode) {
        this.sourceCode = sourceCode;
        return this;
    }

    public File getFile() {
        return file;
    }

    public SourcedJavaTypeBuilder setFile(File file) {
        this.file = file;
        return this;
    }


    public SourcedJavaType build() {
        return new SourcedJavaTypeImpl(type, sourceCode, file);
    }
}

        
