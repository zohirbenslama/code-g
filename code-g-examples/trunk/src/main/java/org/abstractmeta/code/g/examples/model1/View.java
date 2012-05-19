package org.abstractmeta.code.g.examples.model1;

import java.util.Map;
import java.util.Properties;

/**
 * Represents View
 * @author Adrian Witas
 */
public interface View {
    
    int getId();
    
    Properties getVisualProperties();
    
    Properties getAttributes();
    
    Map<Integer, View> getViews();

}
