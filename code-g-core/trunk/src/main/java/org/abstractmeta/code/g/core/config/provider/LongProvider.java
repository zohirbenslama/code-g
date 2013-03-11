package org.abstractmeta.code.g.core.config.provider;
/* 
 * Long Provider
 * @author Adrian Witas
 */

import javax.inject.Provider;
import java.util.Properties;

public class LongProvider extends AbstractProvider<Long> implements Provider<Long>  {


    public LongProvider(Properties properties, String[] pathFragments) {
        super(Long.class, properties, pathFragments);
    }

    @Override
    public Long get() {
        String stringValue =  getValue();
        try {
            return Long.parseLong(stringValue);
        } catch(NumberFormatException ex) {
            throw new IllegalStateException("Could not cast "  + getPath() + " => " + stringValue + " into " + getType(), ex);
        }
    }
}
