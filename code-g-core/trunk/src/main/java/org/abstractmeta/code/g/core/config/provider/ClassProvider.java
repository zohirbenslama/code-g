package org.abstractmeta.code.g.core.config.provider;
/* 
 * Boolean Provider
 * @author Adrian Witas
 */

import javax.inject.Provider;
import java.util.Properties;

public class ClassProvider extends AbstractProvider<Long> implements Provider<Class>  {

     public ClassProvider(Properties properties, String ... pathFragments) {
        super(Long.class, properties, pathFragments);
    }

    @Override
    public Class get() {
        String stringValue = getValue();
        try {
            return Class.forName(stringValue);
        } catch(Exception ex) {
            throw new IllegalStateException("Could not lookup"  + getPath() + " => " + stringValue + " into " + getType(), ex);
        }
    }
}
