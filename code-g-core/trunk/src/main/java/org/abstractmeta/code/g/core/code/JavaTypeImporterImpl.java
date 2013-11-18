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

import com.google.common.base.Preconditions;
import org.abstractmeta.code.g.code.JavaTypeImporter;
import org.abstractmeta.code.g.core.internal.TypeNameWrapper;
import org.abstractmeta.code.g.core.util.JavaTypeUtil;
import org.abstractmeta.code.g.core.util.ReflectUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;

/**
 * Represents JavaTypeImporterImpl
 * <p>
 * </p>
 *
 * @author Adrian Witas
 */
public class JavaTypeImporterImpl implements JavaTypeImporter {

    private final String packageName;
    private final Set<String> typeNames;
    private final Map<String, Type> genericTypeVariables;

    public JavaTypeImporterImpl(String packageName) {
        this.packageName = packageName;
        this.typeNames = new TreeSet<String>();
        this.genericTypeVariables = new HashMap<String, Type>();
    }

    @Override
    public String getPackageName() {
        return packageName;
    }

    @Override
    public void addTypes(Collection<Type> importTypes) {
        for (Type type : importTypes) {
            if (type instanceof TypeNameWrapper) {
                addTypeName(TypeNameWrapper.class.cast(type).getTypeName());
                continue;
            }
            Class rawClass = ReflectUtil.getRawClass(type);
            if(rawClass.isArray()) {
                addTypeName(rawClass.getComponentType().getName());
            } else {
                addTypeName(rawClass.getName());

            }

        }
    }

    @Override
    public void addTypes(Type... importTypes) {
        Collection<Type> collection = new ArrayList<Type>(importTypes.length);
        Collections.addAll(collection, importTypes);
        addTypes(collection);
    }

    @Override
    public List<String> getImportTypeNames() {
        Set<String> result = new TreeSet<String>();
        for (String typeName : typeNames) {
            String importTypeName = getImportTypeName(typeName);
            if (importTypeName != null) {
                result.add(importTypeName);
            }

        }
        return new ArrayList<String>(result);
    }


    protected String getImportTypeName(String typeName) {
        int packageIndex = typeName.lastIndexOf('.');
        if (packageIndex == -1) return null;


        String packageName = typeName.substring(0, packageIndex);
        if ("java.lang".equals(packageName) || packageName.equals(this.packageName)) {
            return null;
        }
        int dollarPosition = typeName.indexOf('$');
        if (dollarPosition != -1) {
            typeName = typeName.substring(0, dollarPosition);
        }
        if (typeName.contains("<")) {
            typeName = typeName.substring(0, typeName.indexOf('<'));
        }
        return typeName;
    }

    @Override
    public String getSimpleTypeName(String typeName) {
        return JavaTypeUtil.getSimpleClassName(typeName, false);
    }

    @Override
    public String getSimpleTypeName(Type type) {
        Preconditions.checkNotNull(type, "type was null");
        if (type instanceof Class) {
            Class clazz = Class.class.cast(type);
            if (clazz.isPrimitive()) {
                return clazz.getSimpleName();
            } else if (clazz.isArray()) {
                return String.format("%s []", getSimpleTypeName(clazz.getComponentType()));
            }
            return getClassSimpleName(clazz.getName());

        } else if (type instanceof TypeNameWrapper) {
            TypeNameWrapper typeNameWrapper = TypeNameWrapper.class.cast(type);
            String simpleClassName = getClassSimpleName(typeNameWrapper.getTypeName());
            return getSimpleTypeNameWithGenericArgumentTypes(simpleClassName, typeNameWrapper.getGenericArgumentTypes());

        } else if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = ParameterizedType.class.cast(type);
            String argumentTypeName = getGenericArgumentTypeName(type);
            return String.format("%s%s", getSimpleTypeName(parameterizedType.getRawType()), argumentTypeName);
        } else if (type instanceof GenericArrayType) {
            return String.format("%s[]", getSimpleTypeName(GenericArrayType.class.cast(type).getGenericComponentType()));
        } else if (type instanceof TypeVariable) {

            String typeVariableName = TypeVariable.class.cast(type).getName();
            if (genericTypeVariables.containsKey(typeVariableName)) {
                Type result = genericTypeVariables.get(typeVariableName);
                return getSimpleTypeName(result);
            }
            return typeVariableName;
        } else if (type instanceof WildcardType) {
            return WildcardType.class.cast(type).toString();
        } else {
            throw new IllegalStateException(String.format("Unsupported type: %s", type + " " + type.getClass()));
        }
    }

    @Override
    public String getTypeName(Type type) {
        if (type instanceof Class) {
            Class clazz = Class.class.cast(type);
            if (clazz.isPrimitive()) {
                return clazz.getName();
            } else if (clazz.isArray()) {
                return String.format("%s []", getTypeName(clazz.getComponentType()));
            }
            return clazz.getName();

        } else if (type instanceof TypeNameWrapper) {
            TypeNameWrapper typeNameWrapper = TypeNameWrapper.class.cast(type);
            String className = typeNameWrapper.getTypeName();
            return getSimpleTypeNameWithGenericArgumentTypes(className, typeNameWrapper.getGenericArgumentTypes());

        } else if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = ParameterizedType.class.cast(type);
            String argumentTypeName = getGenericArgumentTypeName(type);
            return String.format("%s%s", getSimpleTypeName(parameterizedType.getRawType()), argumentTypeName);
        } else if (type instanceof GenericArrayType) {
            return String.format("%s[]", getSimpleTypeName(GenericArrayType.class.cast(type).getGenericComponentType()));
        } else if (type instanceof TypeVariable) {
            String typeVariableName = TypeVariable.class.cast(type).getName();
            if (genericTypeVariables.containsKey(typeVariableName)) {
                Type result = genericTypeVariables.get(typeVariableName);
                return getTypeName(result);
            }
            return typeVariableName;
        } else if (type instanceof WildcardType) {
            return WildcardType.class.cast(type).toString();
        } else {
            throw new IllegalStateException(String.format("Unsupported type: %s", type + " " + type.getClass()));
        }
    }

    @Override
    public String getTypeName(Type type, Collection<Type> genericArgumentTypes) {
        String simpleTypeName = getSimpleTypeName(type);
        return getSimpleTypeNameWithGenericArgumentTypes(simpleTypeName, genericArgumentTypes);
    }

    @Override
    public Map<String, Type> getGenericTypeVariables() {
        return genericTypeVariables;
    }

    protected String getSimpleTypeNameWithGenericArgumentTypes(String simpleTypeName, Collection<Type> genericArgumentTypes) {
        if (genericArgumentTypes != null && !genericArgumentTypes.isEmpty()) {
            return simpleTypeName + "<" + getGenericArgumentTypeName(genericArgumentTypes) + ">";
        }
        return simpleTypeName;
    }


    protected String getClassSimpleName(String className) {
        int lastDotPosition = className.lastIndexOf('.');
        String result = className;
        if (lastDotPosition != -1) {
            result = className.substring(lastDotPosition + 1);
        }
        if (result.indexOf('$') != -1) {
            result = result.replace('$', '.');
        }
        addTypeName(className);
        return result;
    }


    protected void addTypeName(String name) {
        typeNames.add(name);
    }

    @Override
    public String getGenericArgumentTypeName(Type type) {
        String result = "";
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = ParameterizedType.class.cast(type);
            Collection<Type> actualTypeArguments = new ArrayList<Type>();
            Collections.addAll(actualTypeArguments, parameterizedType.getActualTypeArguments());
            result = getGenericArgumentTypeName(actualTypeArguments);

        } else if (type instanceof GenericArrayType) {
            return String.format("%s[]", getSimpleTypeName(GenericArrayType.class.cast(type).getGenericComponentType()));

        } else if (type instanceof TypeVariable) {
            String typeVariableName = TypeVariable.class.cast(type).getName();
            if (genericTypeVariables.containsKey(typeVariableName)) {
                Type resultType = genericTypeVariables.get(typeVariableName);
                result = getSimpleTypeName(resultType);
            } else {
                result = typeVariableName;
            }
        } else if (type instanceof WildcardType) {
            result = WildcardType.class.cast(type).toString();
        } else if (type instanceof TypeNameWrapper) {
            Collection<Type> actualTypeArguments = TypeNameWrapper.class.cast(type).getGenericArgumentTypes();
            if (actualTypeArguments != null) {
                result = getGenericArgumentTypeName(actualTypeArguments);
            }
        }
        if (result.isEmpty()) return "";
        return "<" + result + ">";
    }

    protected String getGenericArgumentTypeName(Collection<Type> arguments) {
        StringBuilder argumentBuilder = new StringBuilder();
        for (Type argument : arguments) {
            if (argumentBuilder.length() > 0) {
                argumentBuilder.append(", ");
            }
            argumentBuilder.append(getSimpleTypeName(argument));
        }
        return String.format("%s", argumentBuilder.toString());
    }

    @Override
    public String getAnnotation(Annotation annotation) {
        String annotationName = annotation.annotationType().getName();
        int index = annotationName.lastIndexOf('.');
        getSimpleTypeName(annotation.annotationType());
        String name = annotationName.substring(index + 1, annotationName.length());

        name = name.replace("$", ".");
        Map<String, Object> values = ReflectUtil.annotationToMap(annotation);
        StringBuilder valueBuilder = new StringBuilder();
        for (String key : values.keySet()) {
            Object value = values.get(key);
            if (valueBuilder.length() > 0) {
                valueBuilder.append(", ");
            }
            valueBuilder.append(key);
            valueBuilder.append(" = ");
            if (value instanceof String) {
                valueBuilder.append("\"");
            }
            if (value.getClass().isArray()) {
                valueBuilder.append(formatAnnotationValueArray(value));

            } else if (value.getClass().isEnum()) {
                addTypeName(value.getClass().getName());
                valueBuilder.append(value.getClass().getSimpleName() + "." + value);
            } else {
                valueBuilder.append(value);
            }
            if (value instanceof String) {
                valueBuilder.append("\"");
            }
        }
        return String.format("@%s(%s)", name, valueBuilder.toString());
    }

    private String formatAnnotationValueArray(Object value) {
        int length = Array.getLength(value);
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < length; i++) {
            if (result.length() > 0) {
                result.append(",");
            }
            Object item = Array.get(value, i);
            if (item instanceof String) {
                result.append("\"").append(item).append("\"");
            } else {
                result.append(item);
            }
        }
        return "{" + result.toString() + "}";
    }

    @Override
    public Type getType(String typeName) {
        return new TypeNameWrapper(typeName);
    }


}
