package org.abstractmeta.code.g.handler;

import org.abstractmeta.code.g.code.JavaType;

/**
 * Code handler.
 *
 * This handler is invoked every time a code generation is competed.
 * The main usage of this abstraction is to delegate code storage logic.
 *
 * @author Adrian Witas
 */
public interface CodeHandler {

    void handle(JavaType javaType, CharSequence sourceCode);
}
