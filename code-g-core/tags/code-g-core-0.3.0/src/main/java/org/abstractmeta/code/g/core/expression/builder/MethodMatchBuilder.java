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
package org.abstractmeta.code.g.core.expression.builder;

import org.abstractmeta.code.g.code.JavaMethod;
import org.abstractmeta.code.g.core.expression.MethodMatchImpl;
import org.abstractmeta.code.g.expression.MethodMatch;
import org.abstractmeta.code.g.expression.MethodPattern;

/**
 * Represents MethodMatchBuilder
 *
 * @author Adrian Witas
 */
public class MethodMatchBuilder {

    private  JavaMethod method;
    private MethodPattern pattern;

    public JavaMethod getMethod() {
        return method;
    }

    public MethodMatchBuilder setMethod(JavaMethod method) {
        this.method = method;
        return this;
    }

    public MethodPattern getPattern() {
        return pattern;
    }

    public MethodMatchBuilder setPattern(MethodPattern pattern) {
        this.pattern = pattern;
        return this;
    }

    public MethodMatch build() {
        return new MethodMatchImpl(method, pattern);
    }
}
