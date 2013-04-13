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
package org.abstractmeta.code.g.generator;

/**
 * Represents Code Generation Context
 * @author Adrian Witas
 */
public interface Context {

    <T> T getOptional(Class<T> key);

    <T> T getOptional(Class<T> key, T defaultValue);

    /**
     * Return non-null value for a given key.
     * @param key
     * @param <T>
     * @return
     * @throws java.util.NoSuchElementException if element not found
     */
    <T> T get(Class<T> key);

    /**
     * Puts value into context with key class and its interfaces keys.
     * @param key key
     * @param value value
     * @param <T> value type if key
     * @throws IllegalStateException if key is already present
     */
    <T> void put(Class<T> key, T value);

    <T> void replace(Class<T> key, T value);

    boolean contains(Class key);

    <T> T remove(Class<T> key);

}
