package org.abstractmeta.code.g.code;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

/**
 * Represents java type importer.
 * This abstraction is responsible for formatting type name
 * for a given package name. For instance
 * if package name is set to com.test, then getSimpleTypeName for "com.test.Foo"
 * returns Foo.
 *
 *  @author Adrian Witas
 */
public interface JavaTypeImporter {

    String getPackageName();

    void addTypes(Collection<Type> importTypes);

    List<String> getTypeNames();

    String getSimpleTypeName(String typeName);

    String getSimpleTypeName(Type type);

    String getTypeName(Type type, Collection<Type> genericArgumentTypes);

    String getGenericArgumentTypeName(Type type);
    
    String getAnnotation(Annotation annotation);
    
    Type getType(String typeName);

    
}
