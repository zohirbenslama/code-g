package org.abstractmeta.code.g;

import org.abstractmeta.code.g.code.JavaType;

/**
 * Code persistence handler.
 * It responsible for storing generated source code.
 *
 * @author Adrian Witas
 */
public interface CodeStorageHandler {

    void handle(JavaType javaType, CharSequence sourceCode);
}
