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
package org.abstractmeta.code.g.code;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Represents java type importer.
 * This abstraction is responsible for formatting type name
 * for a given package name. For instance
 * if package name is set to com.test, then getSimpleTypeName for "com.test.Foo"
 * returns Foo.
 *
 *  @author Adrian Witas
 */
public interface JavaTypeImporter {

    String getPackageName();

    void addTypes(Collection<Type> importTypes);

    void addTypes(Type ... importTypes);

    List<String> getTypeNames();

    String getTypeName(Type type);

    String getTypeName(Type type, Collection<Type> genericArgumentTypes);

    Map<String, Type> getGenericTypeVariables();

    String getGenericArgumentTypeName(Type type);
    
    String getAnnotation(Annotation annotation);
    
    Type getType(String typeName);

    String getSimpleTypeName(String typeName);

    String getSimpleTypeName(Type type);



}
