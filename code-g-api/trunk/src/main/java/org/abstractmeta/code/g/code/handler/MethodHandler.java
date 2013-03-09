package org.abstractmeta.code.g.code.handler;

import org.abstractmeta.code.g.code.JavaMethod;
import org.abstractmeta.code.g.code.JavaType;
import org.abstractmeta.code.g.code.JavaTypeBuilder;

/**
 * Represents MethodHandler
 * Represents method handle, which is notified when a new method is added to the owner type.
 *
 * @author Adrian Witas
 */
public interface MethodHandler {

    void handle(JavaTypeBuilder owner, JavaMethod target);

}
