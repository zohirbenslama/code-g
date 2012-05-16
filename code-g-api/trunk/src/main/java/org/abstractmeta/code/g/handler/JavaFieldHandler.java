package org.abstractmeta.code.g.handler;

import org.abstractmeta.code.g.code.JavaField;
import org.abstractmeta.code.g.code.JavaType;

/**
 * Represents field handler, which is notified when a new type is being built
 * with every single field defined on building type.
 *
 * @author Adrian Witas
 */
public interface JavaFieldHandler {

    /**
     * Handles a new field for a type to be built.
     *
     * @param sourceType source type used by code generator
     * @param javaField type field
     */
    void handle(JavaType sourceType, JavaField javaField);

}
