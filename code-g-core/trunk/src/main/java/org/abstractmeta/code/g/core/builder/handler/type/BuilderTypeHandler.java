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
package org.abstractmeta.code.g.core.builder.handler.type;

import com.google.common.base.CaseFormat;
import com.google.common.base.Joiner;
import org.abstractmeta.code.g.code.*;
import org.abstractmeta.code.g.code.handler.TypeHandler;
import org.abstractmeta.code.g.core.code.builder.JavaFieldBuilder;
import org.abstractmeta.code.g.core.code.builder.JavaMethodBuilder;
import org.abstractmeta.code.g.core.internal.ParameterizedTypeImpl;
import org.abstractmeta.code.g.core.internal.TypeNameWrapper;
import org.abstractmeta.code.g.core.util.CodeGeneratorUtil;
import org.abstractmeta.code.g.core.util.JavaTypeUtil;
import org.abstractmeta.code.g.core.util.ReflectUtil;
import org.abstractmeta.code.g.generator.Context;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;

/**
 * This handle creates constructor for all fields defined on the builder owner type.
 * In case where all fields are mutable the empty constructor is added.
 * <p>For instance for the given source type
 * <code><pre>
 * public interface Bar {
 *     int getId();
 *     Bar getBar();
 *     String getDummy();
 *     void setDummy(String dummy);
 *     Map<String, Bar> getBarMap();
 *     List<Bar> getBarList();
 *     Bar[] getBars();
 * }
 *  </code></pre>
 * The following code is generated
 * <code><pre>
 * public Bar generate() {
 *     Bar result = new FooImpl(id, bar, barMap, barList, bars);
 *     result.setDummy(dummy);
 *     return result;
 * }
 * </pre></code>
 * </p>
 *
 * @author Adrian Witas
 */
public class BuilderTypeHandler implements TypeHandler {


    @Override
    public void handle(JavaTypeBuilder owner, Context context) {
        if (owner.containsMethod("build")) {
            return;
        }

        generateBuildMethod(owner, context);
        setFieldDefaults(owner);
    }

    protected void setFieldDefaults(JavaTypeBuilder owner) {
        List<JavaField> javaFields = owner.getFields();

        owner.setFields(new ArrayList<JavaField>());
        for (JavaField field : javaFields) {
            JavaFieldBuilder fieldBuilder = new JavaFieldBuilder();
            fieldBuilder.merge(field);
            setDefaultValue(owner, fieldBuilder);
            owner.addField(fieldBuilder.build());
        }
    }


    protected void generateBuildMethod(JavaTypeBuilder owner, Context context) {
        JavaType sourceType = owner.getSourceType();
        JavaMethodBuilder methodBuilder = new JavaMethodBuilder();
        methodBuilder.setName("build");
        methodBuilder.addModifier(JavaModifier.PUBLIC);

        Type buildResultType = JavaTypeUtil.getOwnerInterfaceOrType(sourceType);
        owner.getImporter().addTypes(methodBuilder.getResultType());

        Type buildResultImplementationType = new TypeNameWrapper(sourceType.getName(), owner.getGenericTypeArguments());
        owner.getImporter().addTypes(buildResultImplementationType);

        methodBuilder.setResultType(buildResultType);

        List<String> buildTypeConstructorCallArguments = new ArrayList<String>();
        List<String> buildTypeSettingCode = new ArrayList<String>();
        Set<String> sourceMethods = new HashSet<String>();
        for (JavaMethod sourceMethod : sourceType.getMethods()) {
            sourceMethods.add(sourceMethod.getName());
        }

        Map<Class, ImmutableImplementation> immutableImplementations = getImmutableImplementation(context);
        for (JavaField field : sourceType.getFields()) {
            String fieldName = field.getName();
            Class fieldRawType = ReflectUtil.getRawClass(field.getType());
            if (field.isImmutable()) {
                ImmutableImplementation implementation = immutableImplementations.get(fieldRawType);
                if (implementation != null) {
                    buildTypeConstructorCallArguments.add(String.format("%s.%s(%s)", owner.getImporter().getSimpleTypeName(implementation.implementationClass), implementation.implementationMethod.getName(), fieldName));
                    owner.getImporter().addTypes(implementation.implementationClass);

                } else {
                    buildTypeConstructorCallArguments.add(fieldName);
                }
            } else {

                String setterMethodName = CodeGeneratorUtil.format(CaseFormat.LOWER_CAMEL, "set", fieldName, CaseFormat.LOWER_CAMEL);
                if (sourceMethods.contains(setterMethodName)) {
                    buildTypeSettingCode.add(String.format("result.%s(%s);", setterMethodName, fieldName));
                }
            }
        }


        String builtResultTypeName = owner.getImporter().getSimpleTypeName(buildResultType);
        String builtImplementationSimpleTypeName = owner.getImporter().getSimpleTypeName(buildResultImplementationType);
        methodBuilder.addBodyLines(String.format("%s result = new %s(%s);",
                builtResultTypeName,
                builtImplementationSimpleTypeName,
                Joiner.on(", ").join(buildTypeConstructorCallArguments)
        ));
        methodBuilder.addBodyLines(buildTypeSettingCode);
        methodBuilder.addBodyLines("return result;");
        owner.addMethod(methodBuilder.build());
        owner.getImporter().addTypes(NullPointerException.class);
    }


    protected void setDefaultValue(JavaTypeBuilder owner, JavaFieldBuilder field) {
        Class rawType = ReflectUtil.getRawClass(field.getType());


        Type[] genericTypeArguments = ReflectUtil.getGenericActualTypeArguments(field.getType());
        for (Type type : genericTypeArguments) {
            owner.getImporter().addTypes(type);
        }
        Class implementationClass = null;

        if (NavigableSet.class.isAssignableFrom(rawType) || SortedSet.class.isAssignableFrom(rawType)) {
            implementationClass = TreeSet.class;
        } else if (Set.class.isAssignableFrom(rawType)) {
            implementationClass = HashSet.class;
        } else if (List.class.isAssignableFrom(rawType) || Collection.class.isAssignableFrom(rawType)) {
            implementationClass = ArrayList.class;
        } else if (Properties.class.isAssignableFrom(rawType)) {
            implementationClass = Properties.class;
        } else if (NavigableMap.class.isAssignableFrom(rawType) || SortedMap.class.isAssignableFrom(rawType)) {
            implementationClass = TreeMap.class;
        } else if (Map.class.isAssignableFrom(rawType)) {
            implementationClass = HashMap.class;
        } else if (rawType.isArray()) {
            Class componentType = rawType.getComponentType();
            owner.getImporter().addTypes(componentType);
            String componentSimpleTypeName = JavaTypeUtil.getSimpleClassName(componentType.getName(), true);
            field.setInitBody(String.format(" = new %s[]{}", componentSimpleTypeName));
        }

        if (implementationClass != null) {
            owner.getImporter().addTypes(implementationClass);
            Type implementationType = new ParameterizedTypeImpl(null, implementationClass, genericTypeArguments);
            field.setInitBody(String.format(" = new %s()", owner.getImporter().getSimpleTypeName(implementationType)));
        }
    }


    protected static class ImmutableImplementation {
        private final Class implementationClass;
        private final Method implementationMethod;

        public ImmutableImplementation(Class implementationClass, Method implementationMethod) {
            this.implementationClass = implementationClass;
            this.implementationMethod = implementationMethod;
        }
    }


    @SuppressWarnings("unchecked")
    protected Map<Class, ImmutableImplementation> getImmutableImplementation(Context context) {
        if (!context.contains(Config.class)) return Collections.emptyMap();
        Map<Class, Class> mapping = context.get(Config.class).getImmutableImplementation();
        if (mapping == null) return Collections.emptyMap();
        Map<Class, ImmutableImplementation> result = new HashMap<Class, ImmutableImplementation>();
        OUTER:
        for (Class source : mapping.keySet()) {
            Class target = mapping.get(source);
            for (Method candidate : target.getDeclaredMethods()) {
                if (source.isAssignableFrom(candidate.getReturnType())) {
                    Class[] parameterTypes = candidate.getParameterTypes();
                    if (parameterTypes == null || parameterTypes.length != 1) break OUTER;
                    if (candidate.getReturnType().isAssignableFrom(parameterTypes[0])) {
                        result.put(source, new ImmutableImplementation(target, candidate));
                    }
                }

            }
        }
        return result;
    }


    public static interface Config {
        /**
         * Source class and target
         *
         * @return
         */
        Map<Class, Class> getImmutableImplementation();

    }


}
