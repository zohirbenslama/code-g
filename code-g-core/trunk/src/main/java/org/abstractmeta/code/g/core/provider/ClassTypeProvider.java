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
package org.abstractmeta.code.g.core.provider;

import org.abstractmeta.code.g.code.JavaConstructor;
import org.abstractmeta.code.g.code.JavaField;
import org.abstractmeta.code.g.code.JavaMethod;
import org.abstractmeta.code.g.code.JavaType;
import org.abstractmeta.code.g.core.code.builder.JavaFieldBuilder;
import org.abstractmeta.code.g.core.code.builder.JavaConstructorBuilder;
import org.abstractmeta.code.g.core.code.builder.JavaTypeBuilder;
import org.abstractmeta.code.g.core.util.ReflectUtil;
import com.google.common.base.Splitter;
import org.abstractmeta.code.g.core.code.builder.JavaMethodBuilder;


import javax.inject.Provider;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Provides a java type for a given java class or interface.
 * 
 * @author Adrian Witas
 */
public class ClassTypeProvider implements Provider<JavaType> {

    private final Class sourceType;

    public ClassTypeProvider(Class sourceType) {
        if(sourceType == null) {
            throw new IllegalArgumentException("sourceType was null");
        }
        this.sourceType = sourceType;
    }

    @Override
    public JavaType get() {
        JavaTypeBuilder resultBuilder = new JavaTypeBuilder();
        
        resultBuilder.setTypeName(sourceType.getName());
        if (sourceType.isInterface()) {
            resultBuilder.setKind("interface");
        } else if (sourceType.isEnum()) {
            resultBuilder.setKind("enum");
        } else {
            resultBuilder.setKind("class");
        }
        resultBuilder.addGenericTypeArguments(sourceType.getTypeParameters());
        resultBuilder.addSuperInterfaces(Arrays.asList(sourceType.getGenericInterfaces()));
        resultBuilder.setSuperType(sourceType.getGenericSuperclass());
        resultBuilder.addMethods(readMethods());
        resultBuilder.addFields(readFields());
        resultBuilder.addConstructors(readConstructors(resultBuilder));
        return resultBuilder.build();
    }

    protected List<JavaField> readFields() {
        List<JavaField> result = new ArrayList<JavaField>();
        for (Field field : ReflectUtil.getFields(sourceType)) {
            JavaFieldBuilder fieldBuilder = new JavaFieldBuilder()
                    .setName(field.getName())
                    .setType(field.getGenericType())
                    .setImmutable(Modifier.isFinal(field.getModifiers()));
            for (String modifier : Splitter.on(" ").split(Modifier.toString(field.getModifiers()))) {
                fieldBuilder.addModifier(modifier);
            }
            result.add(fieldBuilder.build());
        }
        return result;
    }

    protected List<JavaMethod> readMethods() {
        List<JavaMethod> result = new ArrayList<JavaMethod>();
        for (Method method : ReflectUtil.getMethods(sourceType)) {

            JavaMethodBuilder methodBuilder = new JavaMethodBuilder()
                    .setName(method.getName())
                    .setResultType(method.getGenericReturnType());

            if(method.getExceptionTypes() != null) {
                for(Type exceptionType: method.getExceptionTypes()) {
                    methodBuilder.addExceptionTypes(exceptionType);
                }
            }
            for (String modifier : Splitter.on(" ").split(Modifier.toString(method.getModifiers()))) {
                methodBuilder.addModifier(modifier);
            }

            if (sourceType.isInterface() && methodBuilder.getModifiers().size() == 0) {
                methodBuilder.addModifier("public");
            }
            addMethodParameters(method, methodBuilder);
            result.add(methodBuilder.build());

        }
        return result;
    }

    protected void addMethodParameters(Method method, JavaMethodBuilder methodBuilder) {
        if (method.getGenericParameterTypes() == null) return;
        int i = 0;
        for (Type type : method.getGenericParameterTypes()) {
            methodBuilder.addParameter("argument" + i++, type);
        }
    }

    protected List<JavaConstructor> readConstructors(JavaTypeBuilder resultBuilder) {
        List<JavaConstructor> result = new ArrayList<JavaConstructor>();
        if (!sourceType.isInterface()) {

            for (Constructor constructor : sourceType.getConstructors()) {
                JavaConstructorBuilder constructorBuilder = new JavaConstructorBuilder();
                for (String modifier : Splitter.on(" ").split(Modifier.toString(constructor.getModifiers()))) {
                    constructorBuilder.addModifier(modifier);
                }

                if(constructor.getExceptionTypes() != null) {
                    for(Type exceptionType: constructor.getExceptionTypes()) {
                        constructorBuilder.addExceptionTypes(exceptionType);
                    }
                }
                constructorBuilder.setName(sourceType.getSimpleName());
                addConstructorParameters(constructor, constructorBuilder, resultBuilder);
                result.add(constructorBuilder.build());
            }

        }
        return result;
    }

    protected void addConstructorParameters(Constructor constructor, JavaConstructorBuilder constructorBuilder, JavaTypeBuilder resultBuilder) {

        if (constructor.getParameterTypes() == null) return;
        int i = 0;
        Class[] parameterTypes = constructor.getParameterTypes();
        if (isConstructorParametersMatchFieldTypes(parameterTypes, resultBuilder.getFields())) {
            for (JavaField field : resultBuilder.getFields()) {
                constructorBuilder.addParameter(field.getName(), field.getType());
            }

        } else {
            for (Type parameterType : constructor.getParameterTypes()) {
                constructorBuilder.addParameter("argument " + i++, parameterType);
            }

        }
    }

    /**
     * TODO add more robust implementation to actually check with reflection which arguments sets which field.
     *
     * Returns true if constructor parameters match field types.
     * @param parameterTypes
     * @param fields
     * @return
     */
    protected boolean isConstructorParametersMatchFieldTypes(Class[] parameterTypes, List<JavaField> fields) {
        if(parameterTypes.length != fields.size())  {
            return false;
        }
        for(int i = 0; i <parameterTypes.length; i++) {
            Class fieldType = ReflectUtil.getRawClass(fields.get(i).getType());
            if(! parameterTypes[i].equals(fieldType)) {
                return false;
            }
        }
        return true;
    }

}
