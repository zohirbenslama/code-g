/**
 * Copyright 2011 Adrian Witas
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.abstractmeta.code.g.core.config.provider;

import org.abstractmeta.code.g.core.config.DescriptorImpl;
import org.abstractmeta.code.g.core.config.NamingConventionImpl;
import org.abstractmeta.code.g.core.config.SourceMatcherImpl;
import org.abstractmeta.code.g.core.config.UnitDescriptorImpl;
import org.abstractmeta.code.g.core.property.PropertyRegistryImpl;

import javax.inject.Provider;
import java.util.Properties;

/**
 * UnitDescriptor Provider
 * This provider uses properties to create unit descriptor.
 * @author Adrian Witas
 */
public class UnitDescriptorProvider extends ObjectProvider<UnitDescriptorImpl> implements Provider<UnitDescriptorImpl> {

    public UnitDescriptorProvider(Properties properties, String... pathFragments) {
        super(UnitDescriptorImpl.class, properties, new Class[]{DescriptorImpl.class, NamingConventionImpl.class, SourceMatcherImpl.class, PropertyRegistryImpl.class}, pathFragments);
    }

}
