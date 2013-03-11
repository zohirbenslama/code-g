package org.abstractmeta.code.g.core.test;

import org.abstractmeta.code.g.core.helper.iface.GenericMessage;

/**
 * Represents GenericSubInterface
 *
 * @author Adrian Witas
 */
public interface GenericSubInterface<T> extends GenericMessage<T> {

    int getField();
}
