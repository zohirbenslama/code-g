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
package org.abstractmeta.code.g;

import org.abstractmeta.code.g.config.Descriptor;
import org.abstractmeta.code.g.config.UnitDescriptor;
import org.abstractmeta.code.g.handler.CodeHandler;

import java.io.File;
import java.util.Collection;
import java.util.List;

/**
 * Represents UnitGenerator
 * <p>
 * </p>
 *
 * @author Adrian Witas
 */
public interface UnitGenerator {

    /**
     * Builds code for the specified descriptors.
     *
     * @param unitDescriptor descriptor
     * @return list of the source code files
     */
   Collection<File> generate(UnitDescriptor unitDescriptor);


}
