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
package org.abstractmeta.code.g.core.code;

import org.abstractmeta.code.g.code.JavaType;
import org.abstractmeta.code.g.code.SourcedJavaType;

import java.io.File;

/**
 * Provides implementation for SourcedJavaType
 */
public class SourcedJavaTypeImpl implements SourcedJavaType {

    private final JavaType type;
    private final CharSequence sourceCode;

    public SourcedJavaTypeImpl(JavaType type, CharSequence sourceCode) {
        this.type = type;
        this.sourceCode = sourceCode;
    }

    public JavaType getType() {
        return type;
    }

    public CharSequence getSourceCode() {
        return sourceCode;
    }

}

        
