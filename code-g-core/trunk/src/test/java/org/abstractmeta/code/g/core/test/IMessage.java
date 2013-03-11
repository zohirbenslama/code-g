package org.abstractmeta.code.g.core.test;

import java.util.Collection;
import java.util.Map;

/**
 * Represents AMessage
 * <p>
 * </p>
 *
 * @author Adrian Witas
 */
public interface IMessage {

    int getId();

    String getName();

    void setName(String name);

    Map<String, String> getMap();


}
