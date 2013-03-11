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
package org.abstractmeta.code.g.core.util;

import com.google.common.collect.ImmutableMap;

import javax.annotation.concurrent.Immutable;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple map utility
 *
 * @author Adrian Witas
 *
 */
public class MapMaker {

    public static <T> Map<String, T> make(Class<T> type, Object... arguments) {
        Map<String, T> result = new HashMap<String, T>();
        for(int i = 0; i < arguments.length; i+=2) {
            result.put((String)arguments[i], type.cast(arguments[i + 1]));
        }
        return result;
    }

    public static <T> Map<String, T> makeImmutable(Class<T> type, Object... arguments) {
        Map<String, T> result = new HashMap<String, T>();
        for(int i = 0; i < arguments.length; i+=2) {
            result.put((String)arguments[i], type.cast(arguments[i + 1]));
        }
        return ImmutableMap.copyOf(result);
    }




}


        
