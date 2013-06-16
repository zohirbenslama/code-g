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

import com.google.common.base.Splitter;
import org.abstractmeta.code.g.code.*;
import org.abstractmeta.code.g.core.code.builder.*;
import org.abstractmeta.code.g.core.generator.ContextImpl;
import org.abstractmeta.code.g.core.util.ReflectUtil;

import javax.inject.Provider;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * Provides a java type for a given java class or interface.
 *
 * @author Adrian Witas
 */
public class ClassTypeProvider implements Provider<JavaType> {

    private final Class sourceType;

    public ClassTypeProvider(Class sourceType) {
        if (sourceType == null) {
            throw new IllegalArgumentException("sourceType was null");
        }
        this.sourceType = sourceType;
    }

    @Override
    public JavaType get() {
        JavaKind javaKind;
        if (sourceType.isInterface()) {
            javaKind = JavaKind.INTERFACE;
        } else if (sourceType.isEnum()) {
            javaKind = JavaKind.ENUM;
        } else if (sourceType.isAnnotation()) {
            javaKind = JavaKind.ANNOTATION;
        } else {
            javaKind = JavaKind.CLASS;
        }
        JavaTypeBuilderImpl resultBuilder = new JavaTypeBuilderImpl(javaKind, sourceType.getName());
        resultBuilder.addGenericTypeArguments(sourceType.getTypeParameters());
        resultBuilder.addSuperInterfaces(Arrays.asList(sourceType.getGenericInterfaces()));
        resultBuilder.setSuperType(sourceType.getGenericSuperclass());
        resultBuilder.addMethods(readMethods());
        resultBuilder.addFields(readFields());
        resultBuilder.addConstructors(readConstructors(resultBuilder));
        resultBuilder.addAnnotations(Arrays.asList(sourceType.getAnnotations()));
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
                if (modifier.isEmpty()) continue;
                fieldBuilder.addModifier(JavaModifier.valueOf(modifier.toUpperCase()));
            }
            fieldBuilder.addAnnotations(Arrays.asList(field.getAnnotations()));
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

            if (method.getExceptionTypes() != null) {
                for (Type exceptionType : method.getExceptionTypes()) {
                    methodBuilder.addExceptionTypes(exceptionType);
                }
            }
            for (String modifier : Splitter.on(" ").split(Modifier.toString(method.getModifiers()))) {
                if (modifier.isEmpty()) continue;
                methodBuilder.addModifiers(JavaModifier.valueOf(modifier.toUpperCase()));
            }
            List<Annotation> annotations = new ArrayList<Annotation>();
            Collections.addAll(annotations, method.getAnnotations());
            methodBuilder.addAnnotations(annotations);
            if (sourceType.isInterface() && methodBuilder.getModifiers().size() == 0) {
                methodBuilder.addModifiers(JavaModifier.PUBLIC);
            }
            addMethodParameters(method, methodBuilder);
            result.add(methodBuilder.build());

        }
        return result;
    }

    protected void addMethodParameters(Method method, JavaMethodBuilder methodBuilder) {
        if (method.getGenericParameterTypes() == null) return;
        int i = 0;

        Type[] types = method.getGenericExceptionTypes();
        Annotation[][] annotations = method.getParameterAnnotations();
        for (Type type : method.getGenericParameterTypes()) {
            JavaParameterBuilder parameterBuilder = new JavaParameterBuilder()
                    .setName("argument" + i)
                    .setType(type)
                    .setVarTypeArgument(method.isVarArgs());
            if (annotations != null && annotations.length > i && annotations[i] != null) {
                parameterBuilder.addAnnotations(annotations[i]);
            }
            methodBuilder.addParameters(parameterBuilder.build());
            i++;
        }
    }

    protected List<JavaConstructor> readConstructors(JavaTypeBuilderImpl resultBuilder) {
        List<JavaConstructor> result = new ArrayList<JavaConstructor>();
        if (!sourceType.isInterface()) {
            for (Constructor constructor : sourceType.getConstructors()) {

                JavaConstructorBuilder constructorBuilder = new JavaConstructorBuilder();
                for (String modifier : Splitter.on(" ").split(Modifier.toString(constructor.getModifiers()))) {
                    if (modifier.isEmpty()) continue;
                    constructorBuilder.addModifier(JavaModifier.valueOf(modifier.toUpperCase()));
                }
                if (constructor.getExceptionTypes() != null) {
                    for (Type exceptionType : constructor.getExceptionTypes()) {
                        constructorBuilder.addExceptionTypes(exceptionType);
                    }
                }
                constructorBuilder.setName(sourceType.getSimpleName());
                constructorBuilder.addAnnotations(Arrays.asList(constructor.getAnnotations()));
                addConstructorParameters(constructor, constructorBuilder, resultBuilder);
                result.add(constructorBuilder.build());
            }

        }
        return result;
    }

    protected void addConstructorParameters(Constructor constructor, JavaConstructorBuilder constructorBuilder, JavaTypeBuilderImpl resultBuilder) {
        if (constructor.getParameterTypes() == null || constructor.getParameterTypes().length == 0) return;
        int i = 0;
        Class[] parameterTypes = constructor.getParameterTypes();
        if (isConstructorParametersMatchFieldTypes(parameterTypes, resultBuilder.getFields())) {
            int argumentCount = constructor.getParameterTypes().length;
            for (JavaField field : resultBuilder.getFields()) {
                constructorBuilder.addParameter(field.getName(), field.getType());
                if (++i >= argumentCount) break;
            }

        } else {
            for (Type parameterType : constructor.getParameterTypes()) {
                constructorBuilder.addParameter("argument " + i++, parameterType);
            }

        }
    }

    /**
     * TODO add more robust implementation to actually check with reflection which arguments sets which field.
     * <p/>
     * Returns true if constructor parameters match field types.
     *
     * @param parameterTypes
     * @param fields
     * @return
     */
    protected boolean isConstructorParametersMatchFieldTypes(Class[] parameterTypes, List<JavaField> fields) {
        for (int i = 0; i < parameterTypes.length; i++) {
            Class fieldType = ReflectUtil.getRawClass(fields.get(i).getType());
            if (!parameterTypes[i].equals(fieldType)) {
                return false;
            }
        }
        return true;
    }

}
