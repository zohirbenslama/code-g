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
package org.abstractmeta.code.g.core.internal;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents TypeNameWrapper
 *
 * @author Adrian Witas
 */
public class TypeNameWrapper implements Type {
    private final String typeName;
    private final List<Type> genericArgumentTypes;

    public TypeNameWrapper(String typeName, Type... genericArgumentTypes) {
        this.genericArgumentTypes = new ArrayList<Type>();
        Collections.addAll(this.genericArgumentTypes, genericArgumentTypes);
        this.typeName = typeName;
    }

    public TypeNameWrapper(String typeName, List<Type>  genericArgumentTypes) {
        this.genericArgumentTypes = new ArrayList<Type>();
        this.genericArgumentTypes.addAll(genericArgumentTypes);
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }

    public List<Type> getGenericArgumentTypes() {
        return genericArgumentTypes;
    }

    @Override
    public String toString() {
        return "TypeNameWrapper{" +
                "typeName='" + typeName + '\'' +
                '}';
    }
}
