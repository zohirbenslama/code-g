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
package org.abstractmeta.code.g.config.loader;

import org.abstractmeta.code.g.code.JavaTypeRegistry;
import org.abstractmeta.code.g.config.Descriptor;
import org.abstractmeta.code.g.config.SourceFilter;
import org.abstractmeta.code.g.generator.Context;

import java.util.Collection;

/**
 *
 * Java type loader.
 *
 * @author Adrian Witas
 */
public interface SourceLoader {

    /**
     * Loads java types for a given source filter.
     *
     * @return loaded source
     */
    LoadedSource load(SourceFilter sourceFilter, JavaTypeRegistry registry, ClassLoader classLoader);
}
