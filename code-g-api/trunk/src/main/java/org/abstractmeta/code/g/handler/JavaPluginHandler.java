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


import org.abstractmeta.code.g.code.JavaConstructor;
import org.abstractmeta.code.g.code.JavaField;
import org.abstractmeta.code.g.code.JavaMethod;
import org.abstractmeta.code.g.code.JavaType;
import org.abstractmeta.code.g.config.Descriptor;

/**
 * This handler customises an individual plugin,
 * to extend basic functionality, i.e. before assigning a value to a field.
 * The actual execution place is defined by actual plugin handlers, however there are some suggestions.
 *
 * See plugin documentation for more details.
 *
 *
 *
 * @author Adrian Witas
 */
public interface JavaPluginHandler {

    /**
     * This handler could be called when type is created
     * @param descriptor descriptor
     * @param targetType source type
     */
    void handle(Descriptor descriptor, JavaType targetType);

    /**
     * This handler could be called when a method is created for a filed.
     * @param descriptor  descriptor
     * @param targetType target java type
     * @param targetField target field
     * @param fieldMethod a method this is build for the target filed.
     */
    void handle(Descriptor descriptor, JavaType targetType, JavaField targetField, JavaMethod fieldMethod);

        /**
     * This handler could be called when a constructor is build
     * @param descriptor  descriptor
     * @param targetType target java type
     */
    void handle(Descriptor descriptor, JavaType targetType, JavaConstructor javaConstructor);

}

        
