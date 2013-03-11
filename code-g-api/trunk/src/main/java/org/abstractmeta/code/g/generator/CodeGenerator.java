package org.abstractmeta.code.g.generator;

import org.abstractmeta.code.g.code.CompiledJavaType;
import org.abstractmeta.code.g.code.SourcedJavaType;
import org.abstractmeta.code.g.config.NamingConvention;
import org.abstractmeta.code.g.property.PropertyRegistry;

import java.util.Collection;

/**
 * Represents CodeGenerator
 *
 * @author Adrian Witas
 */
public interface CodeGenerator<T> {


    Collection<CompiledJavaType> generate(Context context);
    /**
     * Naming convention
     * @return
     */
    NamingConvention getNamingConvention();

    /**
     * Setting class created from {@link org.abstractmeta.code.g.config.Descriptor#getProperties()}
     * Note <b>Only a class is supported not an interface</b>
     * @return
     */
    Class<T> getSettingClass();

    PropertyRegistry getPropertyRegistry();

}
