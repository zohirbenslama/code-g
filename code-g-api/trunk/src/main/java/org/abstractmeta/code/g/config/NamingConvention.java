package org.abstractmeta.code.g.config;

/**
 * Represents naming convention of generated code.
 *
 * @author Adrian Witas
 */
public interface NamingConvention {

    String getClassPrefix();

    String getClassPostfix();

    String getPackagePostfix();

}
