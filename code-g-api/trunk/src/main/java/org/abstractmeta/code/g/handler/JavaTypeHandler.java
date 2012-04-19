package org.abstractmeta.code.g.handler;

import org.abstractmeta.code.g.code.JavaType;

/**
 * Represents method handler, which is notified when a new type is being built
 *
 * @author Adrian Witas
 */
public interface JavaTypeHandler {


    /**
     * Handles a new type to be built.
     *
     * @param sourceType java type
     */
    void handle(JavaType sourceType);

}
