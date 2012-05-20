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
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Defines java type code fragment.
 *
 * @author Adrian Witas
 */
public interface JavaType  {

    String getName();
    
    String getSimpleName();

    Map<String, Type> getGenericTypeVariables();
    
    List<Type> getGenericTypeArguments();

    List<String> getDocumentation();

    List<Annotation> getAnnotations();

    List<String> getModifiers();

    String getPackageName();

    Set<Type> getImportTypes();

    Type getSuperType();

    List<Type> getSuperInterfaces();

    List<JavaField> getFields();

    List<JavaConstructor> getConstructors();

    List<String> getBody();

    List<JavaMethod> getMethods();

    List<JavaType> getNestedJavaTypes();

    boolean isNested();
    
    String getKind();

}
