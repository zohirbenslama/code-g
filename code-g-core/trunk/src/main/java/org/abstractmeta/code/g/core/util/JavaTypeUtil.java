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

import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import org.abstractmeta.code.g.code.JavaConstructor;
import org.abstractmeta.code.g.code.JavaField;
import org.abstractmeta.code.g.code.JavaMethod;
import org.abstractmeta.code.g.code.JavaType;
import org.abstractmeta.code.g.core.code.builder.JavaConstructorBuilder;
import org.abstractmeta.code.g.core.code.builder.JavaTypeBuilder;
import org.abstractmeta.code.g.core.collection.function.MethodNameKeyFunction;
import org.abstractmeta.code.g.core.collection.predicates.ConstructorArgumentPredicate;
import org.abstractmeta.code.g.core.internal.TypeNameWrapper;
import org.abstractmeta.code.g.core.provider.ClassTypeProvider;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.*;


/**
 * Java Type utility.
 *
 * @author Adrian Witas
 */
public class JavaTypeUtil {

    public static Map<Class, String> PRIMITIVES_ARRAY_TYPE_NAMES = new HashMap<Class, String>() {{
        put(boolean[].class, "boolean []");
        put(byte[].class, "byte []");
        put(short[].class, "short []");
        put(char[].class, "char []");
        put(int[].class, "int []");
        put(long[].class, "long []");
        put(double[].class, "double []");
        put(float[].class, "float []");
    }};

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


    public static Type matchDeclaringType(JavaType sourceType) {
        Type candidateType = null;
        if (sourceType.getSuperInterfaces() != null && sourceType.getSuperInterfaces().size() > 0) {
            candidateType = sourceType.getSuperInterfaces().get(0);

        } else if (sourceType.getSuperType() != null && !Object.class.equals(sourceType.getSuperType())) {
            candidateType = sourceType.getSuperType();
        }
        if (candidateType != null && matchFieldMethods(candidateType, sourceType.getMethods())) {
            return candidateType;
        }
        return new TypeNameWrapper(sourceType.getName());
    }

    public static String matchDeclaringTypeName(JavaType sourceType) {
        Type candidateType = matchDeclaringType(sourceType);
        if (candidateType instanceof TypeNameWrapper) {
            return TypeNameWrapper.class.cast(candidateType).getTypeName();
        }
        Class rawType = ReflectUtil.getRawClass(candidateType);
        return rawType.getName();
    }


    public static boolean matchFieldMethods(Type candidate, Collection<JavaMethod> methods) {
        if (candidate instanceof TypeNameWrapper) {
            return true;
        }
        Class rawClass = ReflectUtil.getRawClass(candidate);
        JavaType candidateType = new ClassTypeProvider(rawClass).get();
        Set<String> candidateMethods = new HashSet<String>();
        for (JavaMethod method : candidateType.getMethods()) {
            candidateMethods.add(method.getName());
        }
        for (JavaMethod method : methods) {
            String methodName = method.getName();
            if (methodName.length() > 3 && (methodName.startsWith("get")
                    || methodName.startsWith("is"))) {
                if (!candidateMethods.contains(methodName)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Return simple class for a given class name
     *
     * @param className class name
     * @return simple class name
     */
    public static String getSimpleClassName(String className) {
        return getSimpleClassName(className, true);
    }

    /**
     * Returns simple class name for given class name
     * if appendInnerClassDefiner flag is set definer class is added.
     *
     * @param className               class name
     * @param appendInnerClassDefiner flag
     * @return simple class name
     */
    public static String getSimpleClassName(String className, boolean appendInnerClassDefiner) {
        int dotIndex = className.lastIndexOf('.');
        if (dotIndex != -1) {
            className = className.substring(dotIndex + 1, className.length());
        }
        if (className.indexOf('[') != -1) {
            className = getPrimitiveArrayName(className);
        }
        if (className.indexOf(';') != -1) {
            className = className.replace(";", "[]");
        }

        if (className.indexOf('$') != -1) {
            className = className.replace("$", ".");
            if (appendInnerClassDefiner) {
                return className;
            }
            return getSimpleClassName(className, true);
        }

        return className;
    }

    /**
     * Converts internal primitive array type name like [I for int, to int []
     *
     * @param className array primitive class name
     * @return
     */
    public static String getPrimitiveArrayName(String className) {
        if (className.indexOf('[') != -1) {
            for (Class type : PRIMITIVES_ARRAY_TYPE_NAMES.keySet()) {
                if (className.contains(type.getName())) {
                    String arrayTypeLiteral = PRIMITIVES_ARRAY_TYPE_NAMES.get(type);
                    className = className.replace(type.getName(), arrayTypeLiteral);
                    int arrayFirstDimensionPosition = className.indexOf("[" + arrayTypeLiteral);
                    if (arrayFirstDimensionPosition != -1) {
                        className = className.substring(arrayFirstDimensionPosition + 1, className.length());
                        for (int i = 0; i <= arrayFirstDimensionPosition; i++) {
                            className = className + "[]";
                        }
                    }
                }
            }
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


    public static File getFileName(JavaType javaType, File rootDirectory) {
        String classFileName = javaType.getPackageName().replace('.', '/') + "/" + javaType.getSimpleName() + ".java";
        return new File(rootDirectory, classFileName);
    }


    public static boolean containsAnnotation(Collection<Annotation> annotations, String annotationName) {
        if (annotations == null || annotationName == null) return false;
        for (Annotation annotation : annotations) {
            String candidateAnnotationName = annotation.annotationType().getName();
            if (candidateAnnotationName.equals(annotationName)) {
                return true;
            }


        }
        return false;
    }


    public static JavaConstructor buildDefaultConstructor(JavaType sourceType, Map<String, String> fieldArgumentMap) {
        JavaConstructorBuilder constructorBuilder = new JavaConstructorBuilder();
        addSuperCall(sourceType, constructorBuilder, fieldArgumentMap);
        addConstructorParameters(sourceType, constructorBuilder, fieldArgumentMap);
        constructorBuilder.setName(sourceType.getSimpleName());
        constructorBuilder.addModifier("public");
        return constructorBuilder.build();
    }


    protected static void addConstructorParameters(JavaType sourceType, JavaConstructor javaConstructor, Map<String, String> fieldArgumentMap) {
        Iterable<JavaField> immutableJavaFields = Iterables.filter(sourceType.getFields(), new ConstructorArgumentPredicate(sourceType));
        for (JavaField field : immutableJavaFields) {
            String fieldName = field.getName();
            javaConstructor.getParameterNames().add(field.getName());
            javaConstructor.getParameterTypes().add(field.getType());
            String argumentName = fieldArgumentMap.get(fieldName);
            javaConstructor.getBody().add(String.format("this.%s = %s;", fieldName, argumentName));
        }
    }


    protected static void addSuperCall(JavaType superType, JavaConstructorBuilder constructorBuilder, Map<String, String> fieldArgumentMap) {
        JavaConstructor superConstructor = getMinConstructor(superType);
        StringBuilder result = new StringBuilder();
        if (superConstructor != null) {
            List<JavaField> javaFields = superType.getFields();
            int i = 0;
            for (Type parameterType : superConstructor.getParameterTypes()) {
                JavaField javaField = javaFields.get(i++);
                Class parameterRawClass = ReflectUtil.getRawClass(parameterType);
                if (result.length() > 0) {
                    result.append(", ");
                }
                if (parameterRawClass.isPrimitive()) {
                    result.append(fieldArgumentMap.containsKey(javaField.getName()) ? fieldArgumentMap.get(javaField.getName()) : "0");
                } else {
                    result.append(fieldArgumentMap.containsKey(javaField.getName()) ? fieldArgumentMap.get(javaField.getName()) : "null");
                }
            }
        }
        constructorBuilder.addBody("super(" + result.toString() + ");");
    }


    /**
     * Selects constructor with min number of arguments
     *
     * @param sourceType source type
     * @return constructor
     */
    public static JavaConstructor getMinConstructor(JavaType sourceType) {
        JavaConstructor result = null;
        int lastConstructorSize = Integer.MAX_VALUE;
        for (JavaConstructor constructor : sourceType.getConstructors()) {
            if (constructor.getParameterNames().size() == 0) {
                return constructor;
            }
            if (lastConstructorSize > constructor.getParameterNames().size()) {
                result = constructor;
                lastConstructorSize = result.getParameterNames().size();
            }
        }
        return result;

    }
}
