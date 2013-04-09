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
package org.abstractmeta.code.g.config;


import java.util.Collection;

/**
 * Represents SourceMatcher
 *
 * @author Adrian Witas
 */
public interface SourceMatcher {

    String getSourceDirectory();

    Collection<String> getClassNames();

    Collection<String> getPackageNames();

    boolean isIncludeSubpackages();

    /**
     * Optional package based class inclusion pattern
     * @return collection of pattern
     */
    Collection<String> getInclusionPatterns();

    /**
     * Optional package based class exclusion pattern
     * @return collection of pattern
     */
    Collection<String> getExclusionPatterns();




    /**
     * List of dependable packages which source classes depend on,
     * they need to be compiled in order to compile source code for source classes,
     * but this package classes will not be included in the filtered result set
     * @return
     */
    Collection<String> getDependencyPackages();

}

