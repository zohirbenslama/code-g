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
import org.abstractmeta.code.g.code.JavaTypeRegistry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents JavaTypeRegistry.
 *
 * @author Adrian Witas
 */
public class JavaTypeRegistryImpl implements JavaTypeRegistry {
    
    private final Map<String, JavaType> javaTypes;


    public JavaTypeRegistryImpl() {
        this(new HashMap<String, JavaType>());
    }

    protected JavaTypeRegistryImpl(Map<String, JavaType> javaTypes) {
        this.javaTypes = javaTypes;
    }


    @Override
    public void register(JavaType type) {
        javaTypes.put(type.getName(), type);
    }

    @Override
    public JavaType get(String typeName) {
        return javaTypes.get(typeName);
    }

    @Override
    public boolean isRegistered(String typeName) {
        return javaTypes.containsKey(typeName);
    }

    @Override
    public void unregister(String typeName) {
        javaTypes.remove(typeName);
    }

    @Override
    public Collection<JavaType> get() {
        return new ArrayList<JavaType> (javaTypes.values());
    }
}
