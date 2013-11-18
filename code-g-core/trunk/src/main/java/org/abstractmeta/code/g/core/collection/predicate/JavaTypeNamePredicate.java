package org.abstractmeta.code.g.core.collection.predicate;

import com.google.common.base.Predicate;
import org.abstractmeta.code.g.code.JavaType;

import javax.annotation.Nullable;

/**
 * JavaType predicate
 */
public class JavaTypeNamePredicate implements Predicate<JavaType> {


    private final String typeName;

    public JavaTypeNamePredicate(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public boolean apply(JavaType javaType) {
        return typeName.equals(javaType.getName());
    }
}
