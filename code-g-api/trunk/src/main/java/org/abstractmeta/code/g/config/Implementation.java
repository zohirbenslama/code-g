package org.abstractmeta.code.g.config;

import java.util.Collection;

/**
 * Represents Implementation
 * <p>
 * </p>
 *
 * @author Adrian Witas
 */
public interface Implementation {

    /**
     * Optional supper class that generated class needs to extend
     * @return  super class
     */
    String getSuperType();


    /**
     * Optional interfaces that generated class needs to implement
     * @return  super class
     */
    Collection<String> getInterfaces();


}
