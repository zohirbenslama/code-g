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

import org.abstractmeta.code.g.code.JavaMethod;
import org.abstractmeta.code.g.code.JavaType;

/**
 * Represents method handler, which is notified when a new type is being built
 * with every single method defined on this type.
 *
 * @author Adrian Witas
 */
public interface JavaMethodHandler {

    /**
     * Handles a new method for a type to be built.
     *
     * @param sourceType source plugin type
     * @param javaMethod java method
     */
    void handle(JavaType sourceType, JavaMethod javaMethod);


}
