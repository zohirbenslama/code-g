package org.abstractmeta.code.g.code;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Defines java method code fragment.
 *
 * @author Adrian Witas
 */
public interface JavaMethod  {

    String getName();

    List<String> getDocumentation();

    List<Annotation> getAnnotations();

    List<String> getModifiers();

    Type getResultType();
    
    List<Type> getParameterTypes();

    List<String> getParameterNames();

    List<String> getParameterModifiers();

    List<String> getBody();
    
    List<JavaType> getNestedJavaTypes();
    
}
