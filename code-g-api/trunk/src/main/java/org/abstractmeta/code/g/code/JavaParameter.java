package org.abstractmeta.code.g.code;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Represents a method or constructor parameter
 *
 * @author Adrian Witas
 */
public interface JavaParameter {

    String getName();

    Type getType();

    List<Annotation> getAnnotations();

    List<JavaModifier> getModifiers();

    boolean isVarTypeArgument();

}
