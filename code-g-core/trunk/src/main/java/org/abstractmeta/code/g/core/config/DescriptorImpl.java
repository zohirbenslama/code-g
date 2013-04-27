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
package org.abstractmeta.code.g.core.config;


import org.abstractmeta.code.g.config.Descriptor;
import org.abstractmeta.code.g.config.NamingConvention;
import org.abstractmeta.code.g.config.SourceMatcher;

import java.util.Properties;

/**
 * Default descriptor implementation.
 *
 * @author Adrian Witas
 */

public class DescriptorImpl implements Descriptor {

    private SourceMatcher sourceMatcher;
    private NamingConvention namingConvention;
    private String generatorClass;
    private Properties properties;

    public SourceMatcher getSourceMatcher() {
        return sourceMatcher;
    }

    public void setSourceMatcher(SourceMatcher sourceMatcher) {
        this.sourceMatcher = sourceMatcher;
    }

    public NamingConvention getNamingConvention() {
        return namingConvention;
    }

    public void setNamingConvention(NamingConvention namingConvention) {
        this.namingConvention = namingConvention;
    }

    public String getGeneratorClass() {
        return generatorClass;
    }

    public void setGeneratorClass(String generatorClass) {
        this.generatorClass = generatorClass;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    @Override
    public String toString() {
        return "DescriptorImpl{" +
                "sourceMatcher=" + sourceMatcher +
                ", namingConvention=" + namingConvention +
                ", generatorClass='" + generatorClass + '\'' +
                ", properties=" + properties +
                '}';
    }
}
