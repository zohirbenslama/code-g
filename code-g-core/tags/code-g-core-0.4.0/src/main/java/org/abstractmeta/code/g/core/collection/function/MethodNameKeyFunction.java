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
package org.abstractmeta.code.g.core.collection.function;


import org.abstractmeta.code.g.code.JavaMethod;
import com.google.common.base.Function;

/**
 * Represents  MethodNameKeyFunction.
 *
 * @author awitas
 * @version 0.01 29/03/2012
 */

public class MethodNameKeyFunction implements Function<JavaMethod, String> {

    @Override
    public String apply(JavaMethod method) {
         return method.getName();
    }
}
