package org.abstractmeta.code.g.core.config.provider;
/* 
 * Long Provider
 * @author Adrian Witas
 */

import javax.inject.Provider;
import java.util.Properties;

public class IntegerProvider extends AbstractProvider<Long> implements Provider<Integer>  {

     public IntegerProvider(Properties properties, String[] pathFragments) {
        super(Long.class, properties, pathFragments);
    }

    @Override
    public Integer get() {
        String stringValue =  getValue();
        try {
            return Integer.parseInt(stringValue);
        } catch(NumberFormatException ex) {
            throw new IllegalStateException("Could not cast "  + getPath() + " => " + stringValue + " into " + getType(), ex);
        }
    }
}
