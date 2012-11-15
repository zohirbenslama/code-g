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
package org.abstractmeta.code.g.handler;

import org.abstractmeta.code.g.code.SourcedJavaType;

import java.io.File;

/**
 * Code handler.
 *
 * This handler is invoked every time a code generation is competed.
 * The main usage of this abstraction is to delegate code storage logic.
 *
 * @author Adrian Witas
 */
public interface CodeHandler {

    void handle(SourcedJavaType sourcedJavaType);

    File getRootDirectory();

    ClassLoader compile();

    ClassLoader compile(ClassLoader classLoader);


}
