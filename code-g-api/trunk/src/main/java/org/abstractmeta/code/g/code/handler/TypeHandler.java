package org.abstractmeta.code.g.code.handler;

import org.abstractmeta.code.g.code.JavaTypeBuilder;
import org.abstractmeta.code.g.generator.Context;

/**
 * Represents method handler, which is notified when a new type about to be created.
 * It allowed add constructor etc ...,
 *
 * @author Adrian Witas
 */
public interface TypeHandler {

    void handle(JavaTypeBuilder owner, Context context);

}
