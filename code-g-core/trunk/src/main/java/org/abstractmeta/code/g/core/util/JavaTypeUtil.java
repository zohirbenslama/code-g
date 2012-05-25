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

import org.abstractmeta.code.g.code.JavaMethod;
import org.abstractmeta.code.g.code.JavaType;
import org.abstractmeta.code.g.core.code.builder.JavaTypeBuilder;
import org.abstractmeta.code.g.core.collection.function.MethodNameKeyFunction;
import org.abstractmeta.code.g.core.internal.TypeNameWrapper;
import org.abstractmeta.code.g.core.provider.ClassTypeProvider;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

import java.lang.reflect.Type;
import java.util.*;


/**
 * Java Type utility.
 */
public class JavaTypeUtil {

    public static boolean isMethodCompatible(JavaType sourceType, JavaTypeBuilder targetBuilder) {
        return isMethodCompatible(sourceType.getMethods(), targetBuilder.getMethods());
    }


    public static boolean isMethodCompatible(Collection<JavaMethod> sourceMethods, Collection<JavaMethod> targetMethods) {
        Multimap<String, JavaMethod> sourceIndexedMethods = Multimaps.index(sourceMethods, new MethodNameKeyFunction());
        Multimap<String, JavaMethod> targetIndexedMethods = Multimaps.index(targetMethods, new MethodNameKeyFunction());

        for (String methodName : sourceIndexedMethods.keySet()) {
            Collection<JavaMethod> sourceTarget = sourceIndexedMethods.get(methodName);
            Collection<JavaMethod> targetSource = targetIndexedMethods.get(methodName);
            if (targetSource == null) {
                return false;
            }
            if (sourceTarget.size() > targetSource.size()) {
                return false;
            }
        }
        return true;
    }

    public static String getSuperTypeName(JavaType sourceType) {
        Type type = null;
        String result;
        if (sourceType.getSuperInterfaces() != null && sourceType.getSuperInterfaces().size() > 0) {
            type = sourceType.getSuperInterfaces().get(0);
        } else if (sourceType.getSuperType() != null && !Object.class.equals(sourceType.getSuperType())) {
            type = sourceType.getSuperType();

        }
        if (type == null) {
            result = sourceType.getName();

        } else if (type instanceof TypeNameWrapper) {
            result = ((TypeNameWrapper) type).getTypeName();

        } else {
            result = ReflectUtil.getRawClass(type).getName();
        }
        return result;
    }

    public static Type getSuperType(JavaType sourceType) {
        if (sourceType.getSuperInterfaces() != null && sourceType.getSuperInterfaces().size() > 0) {
            return sourceType.getSuperInterfaces().get(0);
        } else if (sourceType.getSuperType() != null && !Object.class.equals(sourceType.getSuperType())) {
            return sourceType.getSuperType();
        } else {
            return new TypeNameWrapper(sourceType.getName());
        }
    }


    /**
     * Return simple class for a given class name
     * @param className class name
     * @return simple class name
     */
    public static String getSimpleClassName(String className) {
        return getSimpleClassName(className, true);
    }

    /**
     *
     * Returns simple class name for given class name
     * if appendInnerClassDefiner flag is set definer class is added.
     *
     * @param className class name
     * @param appendInnerClassDefiner flag
     * @return simple class name
     */
    public static String getSimpleClassName(String className, boolean appendInnerClassDefiner) {
        int dotIndex = className.lastIndexOf('.');
        if (dotIndex != -1) {
            className = className.substring(dotIndex + 1, className.length());
        }
        if(className.indexOf(';') != -1) {
            className = className.replace(";", "[]");
        }
        if (className.indexOf('$') != -1) {
            className = className.replace("$", ".");
            if(appendInnerClassDefiner) {
                return className;
            }
            return getSimpleClassName(className, true);
        }

        return className;
    }


    public static JavaMethod matchFirstFieldByType(Type ownerType, Type matchingType) {
        Class rawOwnerType = ReflectUtil.getRawClass(ownerType);
        JavaType javaOwnerType = new ClassTypeProvider(rawOwnerType).get();
        return matchFirstAccessorByType(javaOwnerType, matchingType);
    }

    public static JavaMethod matchFirstAccessorByType(JavaType ownerType, Type matchingType) {
        for (JavaMethod method : ownerType.getMethods()) {
            if (!method.getName().startsWith("get")) continue;
            if (method.getResultType().equals(matchingType)) {
                return method;
            }
        }
        return null;
    }


}
