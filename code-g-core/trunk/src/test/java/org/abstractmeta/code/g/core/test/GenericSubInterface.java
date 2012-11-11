package org.abstractmeta.code.g.core.test;

import org.abstractmeta.code.g.core.test.GenericInterfaceType;

/**
 * Represents GenericSubInterface
 *
 * @author Adrian Witas
 */
public interface GenericSubInterface<T> extends GenericInterfaceType<T> {

    int getField();
}
