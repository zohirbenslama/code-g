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
package org.abstractmeta.code.g.core.handler.type;

import org.abstractmeta.code.g.code.JavaField;
import org.abstractmeta.code.g.code.JavaMethod;
import org.abstractmeta.code.g.code.JavaType;
import org.abstractmeta.code.g.code.JavaTypeImporter;
import org.abstractmeta.code.g.config.Descriptor;
import org.abstractmeta.code.g.core.code.JavaTypeImporterImpl;
import org.abstractmeta.code.g.core.code.builder.JavaFieldBuilder;
import org.abstractmeta.code.g.core.internal.TypeNameWrapper;
import org.abstractmeta.code.g.core.code.builder.JavaTypeBuilderImpl;
import org.abstractmeta.code.g.core.util.ReflectUtil;
import org.abstractmeta.code.g.core.code.builder.JavaMethodBuilder;
import org.abstractmeta.code.g.core.util.JavaTypeUtil;
import org.abstractmeta.code.g.core.util.StringUtil;
import com.google.common.base.CaseFormat;
import com.google.common.base.Joiner;

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
public class BuilderTypeHandler  {

//
//    private final JavaTypeBuilderImpl ownerTypeBuilder;
//    private final Descriptor descriptor;
//    private final JavaTypeImporter importer = new JavaTypeImporterImpl("");
//
//    public BuilderTypeHandler(JavaTypeBuilderImpl ownerTypeBuilder, Descriptor descriptor) {
//        this.ownerTypeBuilder = ownerTypeBuilder;
//        this.descriptor = descriptor;
//    }
//
//
//    @Override
//    public void handle(JavaType sourceType) {
//        if (ownerTypeBuilder.containsMethod("build")) {
//            return;
//        }
//
//        generateBuildMethod(ownerTypeBuilder, sourceType);
//        setFieldDefaults(ownerTypeBuilder);
//    }
//
//    protected void setFieldDefaults(JavaTypeBuilderImpl ownerTypeBuilder) {
//        List<JavaField> javaFields = ownerTypeBuilder.getFields();
//        ownerTypeBuilder.setFields(new ArrayList<JavaField>());
//        for (JavaField field : javaFields) {
//            JavaFieldBuilder fieldBuilder = new JavaFieldBuilder();
//            fieldBuilder.merge(field);
//            setDefaultValue(ownerTypeBuilder, fieldBuilder);
//            ownerTypeBuilder.addField(fieldBuilder.build());
//        }
//    }
//
//
//    protected void generateBuildMethod(JavaTypeBuilderImpl typeBuilder, JavaType sourceType) {
//        JavaMethodBuilder methodBuilder = new JavaMethodBuilder();
//        methodBuilder.setName("build");
//        methodBuilder.addModifier("public");
//        Type buildResultType = JavaTypeUtil.matchDeclaringType(sourceType);
//        String buildResultSimpleClassName = JavaTypeUtil.getSimpleClassName(JavaTypeUtil.matchDeclaringTypeName(sourceType), true);
//        methodBuilder.setResultType(buildResultType);
//        List<String> buildTypeConstructorCallArguments = new ArrayList<String>();
//        List<String> buildTypeSettingCode = new ArrayList<String>();
//        Set<String> sourceMethods = new HashSet<String>();
//        for (JavaMethod sourceMethod : sourceType.getMethods()) {
//            sourceMethods.add(sourceMethod.getName());
//        }
//        StringBuilder simpleValidationBuilder = new StringBuilder();
//        for (JavaField field : sourceType.getFields()) {
//
//            String fieldName = field.getName();
//            Class fieldRawType = ReflectUtil.getRawClass(field.getType());
//            if (field.isImmutable()) {
//
//                if (descriptor.getImmutableImplementation() != null  && descriptor.getImmutableImplementation() .containsPath(fieldRawType.getName())) {
//                    String immutableImplementation = descriptor.getImmutableImplementation() .get(fieldRawType.getName());
//                    String implementationMethod = StringUtil.substringAfterLastIndexOf(immutableImplementation, ".");
//                    String importType = StringUtil.substringBeforeLastIndexOf(immutableImplementation, ".");
//                    String importSimpleTypeName = JavaTypeUtil.getSimpleClassName(importType);
//                    buildTypeConstructorCallArguments.add(String.format("%s.%s(%s)", importSimpleTypeName, implementationMethod, fieldName));
//                    typeBuilder.addImportType(new TypeNameWrapper(importType));
//
//                } else {
//                    buildTypeConstructorCallArguments.add(fieldName);
//                }
//            } else {
//
//                String setterMethodName = StringUtil.format(CaseFormat.LOWER_CAMEL, "set", fieldName, CaseFormat.LOWER_CAMEL);
//                if (sourceMethods.contains(setterMethodName)) {
//                    buildTypeSettingCode.add(String.format("result.%s(%s);", setterMethodName, fieldName));
//                }
//            }
//        }
//
//        if(simpleValidationBuilder.length() > 0) {
//            methodBuilder.addBodyLines(simpleValidationBuilder.toString());
//        }
//        typeBuilder.addImportType(methodBuilder.getResultType());
//        String builtImplementationSimpleTypeName = JavaTypeUtil.getSimpleClassName(sourceType.getName());
//        ownerTypeBuilder.addImportType(new TypeNameWrapper(sourceType.getName()));
//
//        String genericArgument = importer.getGenericArgumentTypeName(buildResultType);
//        methodBuilder.addBodyLines(String.format("%s%s result = new %s%s(%s);",
//                buildResultSimpleClassName,
//                genericArgument,
//                builtImplementationSimpleTypeName,
//                genericArgument,
//                Joiner.on(", ").join(buildTypeConstructorCallArguments)
//        ));
//        methodBuilder.addBodyLines(buildTypeSettingCode);
//        methodBuilder.addBodyLines("return result;");
//        typeBuilder.addMethod(methodBuilder.build());
//        typeBuilder.addImportType(NullPointerException.class);
//    }
//
//    protected void setDefaultValue(JavaTypeBuilderImpl typeBuilder, JavaFieldBuilder field) {
//        Class rawType = ReflectUtil.getRawClass(field.getType());
//        Type[] genericTypeArguments = ReflectUtil.getGenericActualTypeArguments(field.getType());
//        Class componentType = ReflectUtil.getGenericArgument(genericTypeArguments, 0, Object.class);
//        String componentSimpleTypeName = JavaTypeUtil.getSimpleClassName(componentType.getName(), true);
//        if (NavigableSet.class.isAssignableFrom(rawType)) {
//            typeBuilder.addImportType(TreeSet.class);
//            typeBuilder.addImportType(componentType);
//            field.setInitBody(String.format(" = new %s<%s>()", TreeSet.class.getSimpleName(), componentSimpleTypeName));
//        } else if (Set.class.isAssignableFrom(rawType)) {
//            typeBuilder.addImportType(HashSet.class);
//            typeBuilder.addImportType(componentType);
//            field.setInitBody(String.format(" = new %s<%s>()", HashSet.class.getSimpleName(), componentSimpleTypeName));
//        } else if (Collection.class.isAssignableFrom(rawType)) {
//            typeBuilder.addImportType(ArrayList.class);
//            typeBuilder.addImportType(componentType);
//            field.setInitBody(String.format(" = new %s<%s>()", ArrayList.class.getSimpleName(), componentSimpleTypeName));
//        } else if (Properties.class.isAssignableFrom(rawType)) {
//            typeBuilder.addImportType(HashSet.class);
//            typeBuilder.addImportType(componentType);
//            field.setInitBody(" = new Properties()");
//        } else if (NavigableMap.class.isAssignableFrom(rawType)) {
//            typeBuilder.addImportType(HashSet.class);
//            typeBuilder.addImportType(componentType);
//            Class valueType = ReflectUtil.getGenericArgument(genericTypeArguments, 1, Object.class);
//            String valueSimpleTypeName = JavaTypeUtil.getSimpleClassName(valueType.getName(), true);
//            ownerTypeBuilder.addImportType(valueType);
//            ownerTypeBuilder.addImportType(TreeMap.class);
//            field.setInitBody(String.format(" = new %s<%s, %s>()", TreeMap.class.getSimpleName(), componentSimpleTypeName, valueSimpleTypeName));
//        } else if (Map.class.isAssignableFrom(rawType)) {
//            typeBuilder.addImportType(HashSet.class);
//            typeBuilder.addImportType(componentType);
//            Class valueType = ReflectUtil.getGenericArgument(genericTypeArguments, 1, Object.class);
//            String valueSimpleTypeName = JavaTypeUtil.getSimpleClassName(valueType.getName(), true);
//            ownerTypeBuilder.addImportType(HashMap.class);
//            ownerTypeBuilder.addImportType(valueType);
//            field.setInitBody(String.format(" = new %s<%s, %s>()", HashMap.class.getSimpleName(), componentSimpleTypeName, valueSimpleTypeName));
//        } else if (rawType.isArray()) {
//            componentType = rawType.getComponentType();
//            typeBuilder.addImportType(componentType);
//            componentSimpleTypeName =  JavaTypeUtil.getSimpleClassName(componentType.getName(), true);
//            field.setInitBody(String.format(" = new %s[]{}", componentSimpleTypeName));
//
//        }
//    }


}
