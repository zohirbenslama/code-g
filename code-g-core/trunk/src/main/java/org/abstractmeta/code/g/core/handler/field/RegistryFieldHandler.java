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
package org.abstractmeta.code.g.core.handler.field;

import com.google.common.base.CaseFormat;
import com.sun.tools.doclets.internal.toolkit.builders.MethodBuilder;
import org.abstractmeta.code.g.code.JavaField;
import org.abstractmeta.code.g.code.JavaMethod;
import org.abstractmeta.code.g.code.JavaType;
import org.abstractmeta.code.g.code.JavaTypeImporter;
import org.abstractmeta.code.g.config.Descriptor;
import org.abstractmeta.code.g.core.code.JavaTypeImporterImpl;
import org.abstractmeta.code.g.core.code.builder.JavaMethodBuilder;
import org.abstractmeta.code.g.core.code.builder.JavaTypeBuilder;
import org.abstractmeta.code.g.core.expression.AbstractionPatterns;
import org.abstractmeta.code.g.core.expression.MethodMatcherImpl;
import org.abstractmeta.code.g.core.internal.ParameterizedTypeImpl;
import org.abstractmeta.code.g.core.internal.TypeVariableImpl;
import org.abstractmeta.code.g.core.util.JavaTypeUtil;
import org.abstractmeta.code.g.core.util.ReflectUtil;
import org.abstractmeta.code.g.core.util.StringUtil;
import org.abstractmeta.code.g.expression.AbstractionMatch;
import org.abstractmeta.code.g.expression.MethodMatch;
import org.abstractmeta.code.g.expression.MethodMatcher;
import org.abstractmeta.code.g.handler.JavaFieldHandler;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents RegistryFieldHandler
 * <p/>
 * TODO sanity check go register method
 *
 * @author Adrian Witas
 */
public class RegistryFieldHandler implements JavaFieldHandler {

    public static final String ON_FIELD_CHANGE_INIT_BODY_HANDLER = "onFieldChangeInitBodyHandler";
    public  final static String CREATE_MAP_METHOD = "createMap";

    private final JavaTypeBuilder ownerTypeBuilder;
    private final MethodMatcher methodMatcher;
    private final JavaTypeImporter importer;
    private final  Descriptor descriptor;

    public RegistryFieldHandler(JavaTypeBuilder ownerTypeBuilder, Descriptor descriptor) {
        this.ownerTypeBuilder = ownerTypeBuilder;
        this.methodMatcher = new MethodMatcherImpl();
        this.importer = new JavaTypeImporterImpl(ownerTypeBuilder.getName());
        this.descriptor = descriptor;
    }


    @Override
    public void handle(JavaType sourceType, JavaField javaField) {
        Type fieldType = javaField.getType();
        Class rawFieldType = ReflectUtil.getRawClass(fieldType);
        if (!Map.class.isAssignableFrom(rawFieldType)) {
            return;
        }
        Map<String, AbstractionMatch> matches = methodMatcher.indexByName(methodMatcher.match(sourceType.getMethods(), AbstractionPatterns.REGISTRY_PATTERN));
        String fieldName = javaField.getName();
        if (!fieldName.toLowerCase().endsWith("registry")) {
            return;
        }
        String groupName;
        if (fieldName.equals("registry")) {
            groupName = AbstractionMatch.DEFAULT_GROUP_NAME;
        } else {
            //a registry filed name is generate from group name as follow xxxRegistry, where xxxx is group name
            String registryFiledName = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, fieldName);
            groupName = registryFiledName.substring(0, registryFiledName.length() - 8);
        }

        AbstractionMatch groupMatch = matches.get(groupName);
        if (groupMatch == null) {
            return;
        }

        for (MethodMatch match : groupMatch.getMatches()) {
            JavaMethod javaMethod = match.getMethod();
            String methodName = javaMethod.getName();
            JavaMethodBuilder methodBuilder = new JavaMethodBuilder()
                    .setName(javaMethod.getName())
                    .setResultType(javaMethod.getResultType());

            for (String modifier : javaMethod.getModifiers()) {
                if ("abstract".equals(modifier)) continue;
                methodBuilder.addModifier(modifier);
            }


            for (int i = 0; i < javaMethod.getParameterNames().size(); i++) {
                methodBuilder.addParameter(javaMethod.getParameterNames().get(i), javaMethod.getParameterTypes().get(i));
            }
            if (methodName.startsWith("registerAll")) {
                buildRegisterAllMethod(methodBuilder, groupMatch, javaMethod, javaField);

            } else if (methodName.startsWith("unregisterAll")) {
                buildUnregisterAllMethod(methodBuilder, javaField);

            } else if (methodName.startsWith("register")) {
                buildRegisterMethod(methodBuilder, groupMatch, javaMethod, javaField);

            } else if (methodName.startsWith("unregister")) {
                buildUnregisteredMethod(methodBuilder, groupMatch, javaMethod, javaField);

            } else if (methodName.startsWith("getAll")) {
                buildGetAllMethod(methodBuilder, javaMethod, javaField);


            } else if (methodName.startsWith("get")) {
                buildGetMethod(methodBuilder, javaMethod, javaField);


            } else if (methodName.startsWith("is") && methodName.endsWith("Registered")) {
                if (AbstractionMatch.DEFAULT_GROUP_NAME.equals(groupName) || methodName.contains(groupName)) {
                    buildIsRegisteredMethod(methodBuilder, javaMethod, javaField);
                }
            }

            ownerTypeBuilder.addMethod(methodBuilder.build());
        }
    }

    protected void buildGetAllMethod(JavaMethodBuilder methodBuilder, JavaMethod getMethodMatch, JavaField javaField) {
        methodBuilder.addBody(String.format("return %s.values();", javaField.getName()));
    }

    protected void buildUnregisterAllMethod(JavaMethodBuilder methodBuilder, JavaField javaField) {
        methodBuilder.addBody(String.format("%s.clear();", javaField.getName()));
    }

    protected void buildRegisterAllMethod(JavaMethodBuilder methodBuilder, AbstractionMatch groupMatch, JavaMethod javaMethod, JavaField javaField) {
        MethodMatch registerMatch = groupMatch.getMatch("register", Object[].class);
        String parameterName = javaMethod.getParameterNames().get(0);
        Class collectionType = ReflectUtil.getGenericArgument(javaField.getType(), 1, Object.class);
        methodBuilder.addBody(String.format("for(%s item: %s) { ", collectionType.getSimpleName(), parameterName));
        methodBuilder.addBody(String.format("    %s(item);", registerMatch.getMethod().getName()));
        methodBuilder.addBody("}");
    }

    protected void buildRegisterMethod(JavaMethodBuilder methodBuilder, AbstractionMatch groupMatch, JavaMethod registerMethod, JavaField javaField) {
        MethodMatch getMethodMatch = groupMatch.getMatch("get", Object.class);
        if (registerMethod.getParameterNames().size() == 1) {
            buildSingleParameterRegisterMethod(methodBuilder, getMethodMatch, registerMethod, javaField);
        } else {
            buildMultiParameterRegisterMethod(methodBuilder, getMethodMatch, registerMethod, javaField);
        }
    }


    protected void buildMultiParameterRegisterMethod(JavaMethodBuilder methodBuilder, MethodMatch getMethodMatch, JavaMethod registerMethod, JavaField javaField) {
        if (!(javaField.getType() instanceof ParameterizedType)) return;
        String mapFieldName = javaField.getName();
        List<String> parameterNames = registerMethod.getParameterNames();
        Type valueType = javaField.getType();
        for (int i = 0; i + 1 < parameterNames.size(); i++) {
            String keyArgumentName = parameterNames.get(i);
            String valueArgumentName = parameterNames.get(i + 1);
            if (i + 2 < parameterNames.size()) {
                buildCreateMapMethod();
                String valueForThisKeyMap = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, mapFieldName) + "_" + keyArgumentName.toUpperCase());
                Type valueMapType = ParameterizedType.class.cast(valueType).getActualTypeArguments()[1];
                String valueMapTypeLiteral = importer.getSimpleTypeName(valueMapType);
                methodBuilder.addBody(String.format("%s %s = %s.get(%s);", valueMapTypeLiteral, valueForThisKeyMap, mapFieldName, keyArgumentName));
                methodBuilder.addBody(String.format("if(%s == null) {", valueForThisKeyMap));
                methodBuilder.addBody(String.format("    %s = " + CREATE_MAP_METHOD + "();", valueForThisKeyMap));
                methodBuilder.addBody(String.format("    %s.put(%s, %s);", mapFieldName, keyArgumentName, valueForThisKeyMap));
                methodBuilder.addBody("}");
                mapFieldName = valueForThisKeyMap;
                valueType = valueMapType;
            } else {
                methodBuilder.addBody(mapFieldName + ".put(" + keyArgumentName + ", " + valueArgumentName + ");");
            }
        }
    }


    protected void buildCreateMapMethod() {
        if (ownerTypeBuilder.containsMethod(CREATE_MAP_METHOD)) {
            return;
        }
        JavaMethodBuilder resultMethod = new JavaMethodBuilder();
        resultMethod.addModifier("protected");
        resultMethod.addModifier("<K, V>");
        resultMethod.setName(CREATE_MAP_METHOD);
        resultMethod.setResultType(new ParameterizedTypeImpl(null, Map.class, new TypeVariableImpl("K"), new TypeVariableImpl("V")));
        resultMethod.addBody("return new " + HashMap.class.getSimpleName() + "<K, V>();");
        ownerTypeBuilder.addMethod(resultMethod.build());
        ownerTypeBuilder.addImportType(HashMap.class);
    }


    protected void buildSingleParameterRegisterMethod(JavaMethodBuilder methodBuilder, MethodMatch getMethodMatch, JavaMethod registerMethod, JavaField javaField) {
        String registerArgumentName = registerMethod.getParameterNames().get(0);
        Type registryIndexType = getMethodMatch.getMethod().getParameterTypes().get(0);
        Type registryValueType = registerMethod.getParameterTypes().get(0);
        JavaMethod accessor = JavaTypeUtil.matchFirstFieldByType(registryValueType, registryIndexType);
        methodBuilder.addBody(String.format("%s.put(%s.%s(), %s);", javaField.getName(), registerArgumentName, accessor.getName(), registerArgumentName));
    }


    protected void buildGetMethod(JavaMethodBuilder methodBuilder, JavaMethod getMethodMatch, JavaField javaField) {
        if (getMethodMatch.getParameterNames().size() == 1) {
            buildSingleParameterGetMethod(methodBuilder, getMethodMatch, javaField);
        } else {
            buildMultiParameterGetMethod(methodBuilder, getMethodMatch, javaField);
        }

    }

    private void buildMultiParameterGetMethod(JavaMethodBuilder methodBuilder, JavaMethod getMethodMatch, JavaField javaField) {
        if (!(javaField.getType() instanceof ParameterizedType)) return;
        String mapFieldName = javaField.getName();
        List<String> parameterNames = getMethodMatch.getParameterNames();
        Type valueType = javaField.getType();
        for (int i = 0; i < parameterNames.size(); i++) {
            String keyArgumentName = parameterNames.get(i);
            if (i < parameterNames.size() - 1) {
                buildCreateMapMethod();
                String valueForThisKeyMap = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, mapFieldName) + "_" + keyArgumentName.toUpperCase());
                Type valueMapType = ParameterizedType.class.cast(valueType).getActualTypeArguments()[1];
                String valueMapTypeLiteral = importer.getSimpleTypeName(valueMapType);
                methodBuilder.addBody(String.format("%s %s = %s.get(%s);", valueMapTypeLiteral, valueForThisKeyMap, mapFieldName, keyArgumentName));
                methodBuilder.addBody(String.format("if(%s == null) {", valueForThisKeyMap));
                methodBuilder.addBody("    return null;");
                methodBuilder.addBody("}");
                mapFieldName = valueForThisKeyMap;
                valueType = valueMapType;
            } else {
                methodBuilder.addBody("return " + mapFieldName + ".get(" + keyArgumentName + ");");
            }
        }
    }

    private void buildSingleParameterGetMethod(JavaMethodBuilder methodBuilder, JavaMethod getMethodMatch, JavaField javaField) {
        String parameterName = getMethodMatch.getParameterNames().get(0);
        methodBuilder.addBody(String.format("return %s.get(%s);", javaField.getName(), parameterName));
    }


    protected void buildIsRegisteredMethod(JavaMethodBuilder methodBuilder, JavaMethod isRegisteredMethod, JavaField javaField) {
        if (isRegisteredMethod.getParameterNames().size() == 1) {
            buildSingleParameterIsRegisteredMethod(methodBuilder, isRegisteredMethod, javaField);
        } else {
            buildMultiParametersIsRegisteredMethod(methodBuilder, isRegisteredMethod, javaField);

        }
    }

    private void buildMultiParametersIsRegisteredMethod(JavaMethodBuilder methodBuilder, JavaMethod isRegisteredMethod, JavaField javaField) {
        if (!(javaField.getType() instanceof ParameterizedType)) return;
        String mapFieldName = javaField.getName();
        List<String> parameterNames = isRegisteredMethod.getParameterNames();
        Type valueType = javaField.getType();
        for (int i = 0; i < parameterNames.size(); i++) {
            String keyArgumentName = parameterNames.get(i);
            if (i < parameterNames.size() - 1) {
                buildCreateMapMethod();
                String valueForThisKeyMap = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, mapFieldName) + "_" + keyArgumentName.toUpperCase());
                Type valueMapType = ParameterizedType.class.cast(valueType).getActualTypeArguments()[1];
                String valueMapTypeLiteral = importer.getSimpleTypeName(valueMapType);
                methodBuilder.addBody(String.format("if(!%s.containsKey(%s)) return false;", mapFieldName, keyArgumentName));
                methodBuilder.addBody(String.format("%s %s = %s.get(%s);", valueMapTypeLiteral, valueForThisKeyMap, mapFieldName, keyArgumentName));
                methodBuilder.addBody(String.format("if(%s == null) {", valueForThisKeyMap));
                methodBuilder.addBody("    return false;");
                methodBuilder.addBody("}");
                mapFieldName = valueForThisKeyMap;
                valueType = valueMapType;
            } else {
                methodBuilder.addBody("return " + mapFieldName + ".containsKey(" + keyArgumentName + ");");
            }
        }
    }

    protected void buildSingleParameterIsRegisteredMethod(JavaMethodBuilder methodBuilder, JavaMethod isRegisteredMethod, JavaField javaField) {
        String parameterName = isRegisteredMethod.getParameterNames().get(0);
        methodBuilder.addBody(String.format("return %s.containsKey(%s);", javaField.getName(), parameterName));
    }


    protected void buildUnregisteredMethod(JavaMethodBuilder methodBuilder, AbstractionMatch groupMatch, JavaMethod unregisteredMethod, JavaField javaField) {
        if (unregisteredMethod.getParameterNames().size() == 1) {
            buildSingleArgumentUnregisteredMethod(methodBuilder, groupMatch, unregisteredMethod, javaField);
        } else {
            buildMulipleArgumentUnregisteredMethods(methodBuilder, groupMatch, unregisteredMethod, javaField);
        }
    }

    private void buildMulipleArgumentUnregisteredMethods(JavaMethodBuilder methodBuilder, AbstractionMatch groupMatch, JavaMethod unregisteredMethod, JavaField javaField) {
        if (!(javaField.getType() instanceof ParameterizedType)) return;
        String mapFieldName = javaField.getName();
        List<String> parameterNames = unregisteredMethod.getParameterNames();
        Type valueType = javaField.getType();
        for (int i = 0; i < parameterNames.size(); i++) {
            String keyArgumentName = parameterNames.get(i);
            if (i < parameterNames.size() - 1) {
                buildCreateMapMethod();
                String valueForThisKeyMap = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, mapFieldName) + "_" + keyArgumentName.toUpperCase());
                Type valueMapType = ParameterizedType.class.cast(valueType).getActualTypeArguments()[1];
                String valueMapTypeLiteral = importer.getSimpleTypeName(valueMapType);
                methodBuilder.addBody(String.format("if(!%s.containsKey(%s)) return ;", mapFieldName, keyArgumentName));
                methodBuilder.addBody(String.format("%s %s = %s.get(%s);", valueMapTypeLiteral, valueForThisKeyMap, mapFieldName, keyArgumentName));
                methodBuilder.addBody(String.format("if(%s == null) {", valueForThisKeyMap));
                methodBuilder.addBody("    return ;");
                methodBuilder.addBody("}");
                mapFieldName = valueForThisKeyMap;
                valueType = valueMapType;
            } else {
                methodBuilder.addBody("" + mapFieldName + ".remove(" + keyArgumentName + ");");
            }
        }    }

    protected void buildSingleArgumentUnregisteredMethod(JavaMethodBuilder methodBuilder, AbstractionMatch groupMatch, JavaMethod unregisteredMethod, JavaField javaField) {
        String parameterName = unregisteredMethod.getParameterNames().get(0);
        MethodMatch getMethodMatch = groupMatch.getMatch("get", Object.class);
        Type registryIndexType = getMethodMatch.getMethod().getParameterTypes().get(0);
        Type registryValueType = unregisteredMethod.getParameterTypes().get(0);
        if (registryIndexType.equals(registryValueType)) {
            methodBuilder.addBody(String.format("%s.remove(%s);", javaField.getName(), parameterName));

        } else {
            JavaMethod accessor = JavaTypeUtil.matchFirstFieldByType(registryValueType, registryIndexType);
            if (accessor == null) {
                throw new IllegalStateException("Failed to match be scanner type " + registryValueType + " " + registryIndexType);
            }
            methodBuilder.addBody(String.format("%s.remove(%s.%s());", javaField.getName(), parameterName, accessor.getName()));
        }
    }

    public Descriptor getDescriptor() {
        return descriptor;
    }
}
