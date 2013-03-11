package org.abstractmeta.code.g.code.handler;

import org.abstractmeta.code.g.code.JavaField;
import org.abstractmeta.code.g.code.JavaType;
import org.abstractmeta.code.g.code.JavaTypeBuilder;
import org.abstractmeta.code.g.generator.Context;

/**
 * Represents field handle, which is notified when a new field is added to the owner.
 *
 * @author Adrian Witas
 */
public interface FieldHandler {

    void handle(JavaTypeBuilder owner, JavaField target, Context context);

}
