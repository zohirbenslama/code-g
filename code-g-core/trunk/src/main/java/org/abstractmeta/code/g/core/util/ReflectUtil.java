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

import com.google.common.base.Joiner;
import org.abstractmeta.code.g.core.internal.ParameterizedTypeImpl;
import org.abstractmeta.code.g.core.internal.TypeNameWrapper;
import org.abstractmeta.toolbox.compilation.compiler.JavaSourceCompiler;
import org.abstractmeta.toolbox.compilation.compiler.impl.JavaSourceCompilerImpl;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;

/**
 * @author Adrian Witas
 */
public class ReflectUtil {


    /**
     * Returns raw type for a given type, Object.class if type is has no raw class
     *
     * @param type raw class type
     * @return raw class
     */
    public static Class getRawClass(Type type) {
        if (type instanceof Class) {
            return Class.class.cast(type);
        } else if (type instanceof ParameterizedType) {
            return getRawClass(ParameterizedType.class.cast(type).getRawType());
        } else if (type instanceof GenericArrayType) {
            Type componentType = GenericArrayType.class.cast(type).getGenericComponentType();
            if (componentType instanceof Class) {
                return Array.newInstance((Class) componentType, 0).getClass();
            } else if (componentType instanceof TypeVariable) {
                return Object[].class;
            }
        } else if (type instanceof TypeVariable) {
            return Object.class;
        } else if (type instanceof TypeNameWrapper) {
            try {
                return loadClass(((TypeNameWrapper) type).getTypeName(), null);
            } catch (ClassNotFoundException e) {
                return Object.class;
            }
        }
        return Object.class;
    }

    /**
     * Return object non primitive type for a supplied type i.e. for int.class it returns Integer.class.
     *
     * @param type type
     * @return non primitive type.
     */
    public static Type getObjectType(Type type) {
        Class rawResultType = ReflectUtil.getRawClass(type);
        if (rawResultType.isPrimitive()) {
            return ReflectUtil.getPrimitiveCounterType(rawResultType);
        }
        return type;
    }

    /**
     * Returns generic actual type argument for a given type
     *
     * @param type source type
     * @return generic actual argument types
     */
    public static Type[] getGenericActualTypeArguments(Type type) {
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = ParameterizedType.class.cast(type);
            return parameterizedType.getActualTypeArguments();
        } else if (type instanceof GenericArrayType) {
            return new Type[]{GenericArrayType.class.cast(type).getGenericComponentType()};
        } else if (type instanceof TypeNameWrapper) {
            return new Type[]{};
        } else if (type instanceof Class) {
            Class clazz = ((Class) type);
            if (clazz.isPrimitive()) {
                clazz = getPrimitiveCounterType(clazz);
            }
            TypeVariable[] typeVariables = clazz.getTypeParameters();
            if (typeVariables != null && typeVariables.length > 0) {
                Type[] result = new Type[typeVariables.length];
                for (int i = 0; i < result.length; i++) {
                    result[i] = Object.class;
                }
                return result;
            }
            return new Type[]{};
        } else if (type instanceof TypeVariable) {
            return TypeVariable.class.cast(type).getBounds();
        } else if (type instanceof WildcardType) {
            return WildcardType.class.cast(type).getLowerBounds();
        }
        throw new IllegalStateException(String.format("Unsupported type %s", type + " " + type.getClass()));
    }


    /**
     * Return set of type variables for a given type
     *
     * @param type source type
     * @return variable types.
     */
    public static Set<Type> getTypeVariables(Type type) {
        HashSet<Type> result = new HashSet<Type>();
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = ParameterizedType.class.cast(type);
            for (Type candidate : parameterizedType.getActualTypeArguments()) {
                result.addAll(getTypeVariables(candidate));
            }


        } else if (type instanceof GenericArrayType) {
            return getTypeVariables(GenericArrayType.class.cast(type).getGenericComponentType());
        } else if (type instanceof Class) {
            Class clazz = ((Class) type);
            if (clazz.isPrimitive()) {
                clazz = getPrimitiveCounterType(clazz);
            }
            TypeVariable[] typeVariables = clazz.getTypeParameters();
            if (typeVariables != null && typeVariables.length > 0) {
                for (int i = 0; i < typeVariables.length; i++) {
                    result.addAll(getTypeVariables(typeVariables[i]));
                }
            }
        } else if (type instanceof TypeVariable) {
            result.add(type);
        } else if (type instanceof WildcardType) {

            for (Type lowerType : WildcardType.class.cast(type).getLowerBounds()) {
                result.addAll(getTypeVariables(lowerType));
            }
            for (Type upperType : WildcardType.class.cast(type).getUpperBounds()) {
                result.addAll(getTypeVariables(upperType));
            }
        }
        return result;
    }


    /**
     * Returns all methods declared by a given type of interface.
     *
     * @param type class type
     * @return list of methods
     */
    public static List<Method> getMethods(Class type) {
        List<Method> result = new ArrayList<Method>();
        Set<String> methodSignatures = new HashSet<String>();
        getMethods(type, result, methodSignatures);
        return result;
    }

    /**
     * Returns a given method signature.
     *
     * @param method java methods
     * @return method signature
     */
    public static String getMethodSignature(Method method) {
        return getMethodSignature(method.getName(), method.getParameterTypes());
    }


    /**
     * Returns method signature.
     */
    public static String getMethodSignature(String methodName, Class... parameterTypes) {
        return methodName + ":" + Joiner.on(",").join(getClassNames(parameterTypes));
    }


    protected static void getMethods(Class type, List<Method> result, Set<String> methodSignatures) {
        Method[] methods = type.getDeclaredMethods();
        for (Method method : methods) {
            String signature = getMethodSignature(method);
            if (!methodSignatures.contains(signature)) {
                result.add(method);
                methodSignatures.add(signature);
            }
        }
        if (type.getSuperclass() != null && type.getSuperclass() != Class.class && type.getSuperclass() != Object.class) {
            getMethods(type.getSuperclass(), result, methodSignatures);
        }
        if (type.getInterfaces() != null) {
            for (Class iFace : type.getInterfaces()) {
                getMethods(iFace, result, methodSignatures);
            }
        }
    }

    public static List<Field> getFields(Class type) {
        List<Field> result = new ArrayList<Field>();
        getFields(type, result);
        return result;
    }

    private static void getFields(Class type, List<Field> result) {
        Field[] fields = type.getDeclaredFields();
        for (Field field : fields) {
            result.add(field);
        }
        if (type.getSuperclass() != null && type.getSuperclass() != Class.class && type.getSuperclass() != Object.class) {
            getFields(type.getSuperclass(), result);
        }
    }

    public static Map<String, Object> annotationToMap(Annotation annotation) {
        Class type = annotation.annotationType();
        Map<String, Object> result = new HashMap<String, Object>();
        for (Method method : type.getMethods()) {
            String methodName = method.getName();
            if (method.getGenericParameterTypes().length > 0 || "toString".equals(methodName) || "hashCode".equals(methodName) || "annotationType".equals(methodName)) {
                continue;
            }
            try {
                method.setAccessible(true);
                Object instance = method.invoke(annotation);
                Object defaultValue = method.getDefaultValue();
                if (defaultValue != null && defaultValue.equals(instance)) {
                    continue;
                }
                if (defaultValue != null && defaultValue.getClass().isArray()) {
                    if (!defaultValue.getClass().isPrimitive()) {
                        if (Arrays.equals((Object[]) defaultValue, (Object[]) instance)) continue;
                    }
                }
                result.put(method.getName(), instance);
            } catch (Exception e) {
                throw new IllegalStateException("Failed to read annotation " + annotation, e);
            }
        }
        return result;
    }

    public static Class getPrimitiveCounterType(Class primitiveType) {
        char c = primitiveType.getSimpleName().charAt(0);
        switch (c) {
            case 'i':
                return Integer.class;
            case 's':
                return Short.class;
            case 'l':
                return Long.class;
            case 'c':
                return Character.class;
            case 'f':
                return Float.class;
            case 'd':
                return Double.class;
            case 'v':
                return Void.class;
            default:
                if (primitiveType == boolean.class) {
                    return Boolean.class;
                } else if (primitiveType == byte.class) {
                    return Byte.class;
                } else {
                    throw new IllegalStateException("Invalid primitive type " + primitiveType);
                }
        }
    }


    public static Class getPrimitiveType(Class type) {

        if (Integer.class.equals(type)) {
            return int.class;
        } else if (Long.class.equals(type)) {
            return long.class;
        } else if (Short.class.equals(type)) {
            return short.class;
        } else if (Character.class.equals(type)) {
            return char.class;
        } else if (Byte.class.equals(type)) {
            return byte.class;
        } else if (Boolean.class.equals(type)) {
            return boolean.class;
        } else if (Float.class.equals(type)) {
            return float.class;
        } else if (Double.class.equals(type)) {
            return double.class;
        } else if (Void.class.equals(type)) {
            return void.class;
        }
        return null;
    }

//    public static Class getGenericArgument(Type type, int argumentIndex, Class defaultType) {
//        if (type instanceof ParameterizedType) {
//            Type[] types = ParameterizedType.class.cast(type).getActualTypeArguments();
//            if (argumentIndex < types.length) {
//                return ReflectUtil.getRawClass(types[argumentIndex]);
//            }
//        }
//        return defaultType;
//    }
//
//
//    public static Class getGenericArgument(Type[] types, int argumentIndex, Class defaultType) {
//        if (argumentIndex < types.length) {
//            return ReflectUtil.getRawClass(types[argumentIndex]);
//        }
//        return defaultType;
//    }


    public static Class getGenericClassArgument(Type[] types, int argumentIndex, Class defaultType) {
        if (argumentIndex < types.length) {
            return ReflectUtil.getRawClass(types[argumentIndex]);
        }
        return defaultType;
    }

    public static Collection<String> getClassNames(Class... classes) {
        List<String> result = new ArrayList<String>();
        for (Class clazz : classes) {
            result.add(clazz.getName());
        }
        return result;
    }


    public static boolean isPrimitiveType(Type type) {
        if (type instanceof TypeNameWrapper) {
            return false;
        }
        Class rawClass = getRawClass(type);
        return rawClass.isPrimitive();
    }


    public static boolean isArrayType(Type type) {
        if (type instanceof TypeNameWrapper) {
            return false;
        }
        if (type instanceof GenericArrayType) {
            return true;
        }
        Class rawClass = getRawClass(type);
        return rawClass.isArray();
    }



    public static Type resolveTypeVariables(Type type, Map<String, Type> typeVariables) {
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = ParameterizedType.class.cast(type);
            List<Type> newArguments = new ArrayList<Type>();
            for (Type argument : parameterizedType.getActualTypeArguments()) {
                if (argument instanceof TypeVariable) {
                    TypeVariable typeVariable = TypeVariable.class.cast(argument);
                    if (typeVariables.containsKey(typeVariable.getName())) {
                        newArguments.add(typeVariables.get(typeVariable.getName()));
                    } else {
                        newArguments.add(argument);
                    }
                } else {
                    newArguments.add(argument);

                }
            }
            return new ParameterizedTypeImpl(parameterizedType.getOwnerType(), parameterizedType.getRawType(), newArguments.toArray(new Type[]{}));
        } else if(type instanceof GenericArrayType) {
            GenericArrayType arrayType = GenericArrayType.class.cast(type);
            if(arrayType.getGenericComponentType() != null) {
                return resolveTypeVariables(arrayType.getGenericComponentType(), typeVariables);
            }
        }
        return type;
    }


    public static <T> T newInstance(Class<T> type, String className, ClassLoader classLoader) {
        Class clazz = null;
        if (type.isInterface()) {
            throw new IllegalStateException("Can not instantiate interface " + type);
        }
        try {
            clazz = loadClass(className, classLoader);
            return type.cast(clazz.newInstance());
        } catch (ClassCastException e) {
            throw new ClassCastException("Failed to cast " + clazz + " " + type.getName());
        } catch (Exception e) {
            throw new IllegalStateException("Failed to create a new instance of class " + className, e);
        }
    }

    public static <T> T newInstance(Class<T> type, String className) {
        return newInstance(type, className, type.getClassLoader());
    }

    public static <T> T newInstance(Class<T> type) {
        return newInstance(type, type.getName());
    }


    public static Class loadClass(String className, ClassLoader classLoader) throws ClassNotFoundException {
        if (classLoader == null) {
            classLoader = ReflectUtil.class.getClassLoader();
        }
        return classLoader.loadClass(className);
    }


    public static Object invokeMethod(Object instance, String methodName, Class[] argumentTypes, Object... arguments) {
        try {
            Method method = instance.getClass().getMethod(methodName, argumentTypes);
            return method.invoke(instance, arguments);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to invoke method " + methodName, e);
        }

    }

    public static void setFieldValue(Object instance, String fieldName, Object value) {
        try {
            Field field = instance.getClass().getDeclaredField(fieldName);
            setFieldValue(instance, field, value);
        } catch (NoSuchFieldException e) {
            throw new IllegalStateException("Failed to lookup field " + instance.getClass().getName() + "." + fieldName, e);
        }
    }

    public static void setFieldValue(Object instance, Field field, Object value) {
        try {
            field.setAccessible(true);
            field.set(instance, value);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to set field " + instance.getClass().getName() + "." + field.getName(), e);
        }

    }

    public static <T> T getFieldValue(Class<T> fieldType, Object instance, String fieldName) {
        try {
            Field field = instance.getClass().getDeclaredField(fieldName);
            return getFieldValue(fieldType, instance, field);
        } catch (NoSuchFieldException e) {
            throw new IllegalStateException("Failed to lookup field " + instance.getClass().getName() + "." + fieldName, e);
        }
    }


    public static <T> T getFieldValue(Class<T> fieldType, Object instance, Field field) {
        try {
            if (!field.isAccessible()) field.setAccessible(true);
            Object result = field.get(instance);
            if (result == null) return null;
            return fieldType.cast(result);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to read value from field " + instance.getClass().getName() + "." + field.getName(), e);
        }

    }

    public static Class compileSource(String className, String sourceCode) {
        JavaSourceCompiler javaSourceCompiler = new JavaSourceCompilerImpl();
        JavaSourceCompiler.CompilationUnit compilationUnit = javaSourceCompiler.createCompilationUnit();
        compilationUnit.addJavaSource(className, sourceCode);
        ClassLoader classLoader = javaSourceCompiler.compile(compilationUnit);
        try {
            return classLoader.loadClass(className);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Failed to load class " + className, e);
        }
    }

    public static <T> T getInstance(Class<T> owner) {
        try {
            return (T) owner.newInstance();
        } catch (Exception e) {
            throw new IllegalStateException("Failed to instantiate " + owner, e);
        }
    }




    public static <T> T getInstance(Class<T> owner, Class[] argumentTypes, Object[] arguments) {
        try {
            Constructor<T> constructor = owner.getConstructor(argumentTypes);
            return owner.cast(constructor.newInstance(arguments));
        } catch (Exception e) {
            throw new IllegalStateException("Failed to instantiate " + owner, e);
        }
    }

    public static Type getComponentType(Type type) {
        if (type instanceof GenericArrayType) {
             return GenericArrayType.class.cast(type).getGenericComponentType();
        } else if (type instanceof Class) {
            return Class.class.cast(type).getComponentType();
        }
        throw new IllegalStateException("Not an array " + type);
    }
}
