package org.abstractmeta.code.g.code;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Defines java field code fragment.
 *
 * @author Adrian Witas
 */
public interface JavaField  {

    String getName();

    List<String> getDocumentation();

    List<Annotation> getAnnotations();

    List<String> getModifiers();

    String getInitBody();
    
    Type getType();

    boolean isImmutable();

}
