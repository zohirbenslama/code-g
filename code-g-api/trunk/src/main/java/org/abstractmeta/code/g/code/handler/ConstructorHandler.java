package org.abstractmeta.code.g.code.handler;

import org.abstractmeta.code.g.code.JavaConstructor;
import org.abstractmeta.code.g.code.JavaTypeBuilder;
import org.abstractmeta.code.g.generator.Context;

/**
 * Represents constructor handle, which is notified when a new field is added to the owner.
 *
 * @author Adrian Witas
 */
public interface ConstructorHandler {

    void handle(JavaTypeBuilder owner, JavaConstructor target, Context context);

}
