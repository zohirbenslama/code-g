package org.abstractmeta.code.g.core.config.provider;

import org.abstractmeta.code.g.config.UnitDescriptor;
import org.abstractmeta.code.g.core.config.*;
import org.abstractmeta.code.g.core.provider.ObjectProvider;

import javax.inject.Provider;
import java.util.Properties;

/**
 * UnitDescriptor Provider
 * This provider uses properties to create unit descriptor.
 * @author Adrian Witas
 */
public class UnitDescriptorProvider extends ObjectProvider<UnitDescriptor> implements Provider<UnitDescriptor> {

    public UnitDescriptorProvider(Properties properties, String... pathFragments) {
        super(UnitDescriptorImpl.class, properties, new Class[]{DescriptorImpl.class, NamingConventionImpl.class, SourceFilterImpl.class, ImplementationImpl.class}, pathFragments);
    }

}
