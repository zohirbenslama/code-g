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
