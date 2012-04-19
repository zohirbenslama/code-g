package org.abstractmeta.code.g.code;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;

/**
 * Defines java type code fragment.
 *
 * @author Adrian Witas
 */
public interface JavaType  {

    String getName();
    
    String getSimpleName();
    
    List<Type> getGenericTypeArguments();

    List<String> getDocumentation();

    List<Annotation> getAnnotations();

    List<String> getModifiers();

    String getPackageName();

    Set<Type> getImportTypes();

    Type getSuperType();

    List<Type> getSuperInterfaces();

    List<JavaField> getFields();

    List<JavaConstructor> getConstructors();

    List<String> getBody();

    List<JavaMethod> getMethods();

    List<JavaType> getNestedJavaTypes();

    boolean isNested();
    
    String getKind();

}
