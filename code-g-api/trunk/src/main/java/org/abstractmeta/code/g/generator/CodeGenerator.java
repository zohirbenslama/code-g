package org.abstractmeta.code.g.generator;

import org.abstractmeta.code.g.code.SourcedJavaType;
import org.abstractmeta.code.g.config.NamingConvention;

import java.util.Collection;

/**
 * Represents CodeGenerator
 *
 * @author Adrian Witas
 */
public interface CodeGenerator {

    Collection<SourcedJavaType> generate(Context context);

    /**
     * Naming convention
     * @return
     */
    NamingConvention getNamingConvention();


}
