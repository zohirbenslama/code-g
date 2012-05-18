package org.abstractmeta.code.g.code;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Defines java constructor code fragment
 *
 * @author Adrian Witas
 */
public interface JavaConstructor  {

    String getName();

    List<String> getDocumentation();

    List<Annotation> getAnnotations();

    List<String> getModifiers();

    List<String> getParameterModifiers();

    List<Type> getParameterTypes();

    List<String> getParameterNames();
    
    List<String> getBody();
}
