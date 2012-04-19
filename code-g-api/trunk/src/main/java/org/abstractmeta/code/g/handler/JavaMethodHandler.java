package org.abstractmeta.code.g.handler;

import org.abstractmeta.code.g.code.JavaMethod;
import org.abstractmeta.code.g.code.JavaType;

/**
 * Represents method handler, which is notified when a new type is being built
 * with every single method defined on this type.
 *
 * @author Adrian Witas
 */
public interface JavaMethodHandler {

    /**
     * Handles a new method for a type to be built.
     *
     * @param sourceType source generator type
     * @param javaMethod java method
     */
    void handle(JavaType sourceType, JavaMethod javaMethod);


}
