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
package org.abstractmeta.code.g.core.expression;

import org.abstractmeta.code.g.code.JavaMethod;
import org.abstractmeta.code.g.expression.MethodMatch;
import org.abstractmeta.code.g.expression.MethodPattern;

/**
 * Represents MethodMatch.
 *
 * @author Adrian Witas
 */
public class MethodMatchImpl implements MethodMatch {

    private final JavaMethod method;
    private final MethodPattern pattern;

    public MethodMatchImpl(JavaMethod method, MethodPattern pattern) {
        this.method = method;
        this.pattern = pattern;
    }

    public JavaMethod getMethod() {
        return method;
    }

    public MethodPattern getPattern() {
        return pattern;
    }

}
