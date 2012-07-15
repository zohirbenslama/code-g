package org.abstractmeta.code.g.examples.model1;

import java.util.Map;

/**
 * Represents ViewRegistry
 *
 * @author Adrian Witas
 */
public interface ViewRegistry {
    
    void register(View view);

    void unregister(View view);
    
    View get(int id);

    Map<Integer, View> getRegistry();
}

