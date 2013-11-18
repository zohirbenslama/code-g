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
package org.abstractmeta.code.g.core.builder.handler.field;

import com.google.common.base.CaseFormat;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import org.abstractmeta.code.g.code.*;
import org.abstractmeta.code.g.code.handler.FieldHandler;
import org.abstractmeta.code.g.core.code.builder.JavaFieldBuilder;
import org.abstractmeta.code.g.core.code.builder.JavaMethodBuilder;
import org.abstractmeta.code.g.core.collection.predicate.ExcludeJavaModifierPredicate;
import org.abstractmeta.code.g.core.expression.AbstractionPatterns;
import org.abstractmeta.code.g.core.internal.ParameterizedTypeImpl;
import org.abstractmeta.code.g.core.internal.TypeVariableImpl;
import org.abstractmeta.code.g.core.util.JavaTypeUtil;
import org.abstractmeta.code.g.core.util.ReflectUtil;
import org.abstractmeta.code.g.core.util.CodeGeneratorUtil;
import org.abstractmeta.code.g.expression.AbstractionMatch;
import org.abstractmeta.code.g.expression.MethodMatch;
import org.abstractmeta.code.g.expression.MethodMatcher;
import org.abstractmeta.code.g.generator.Context;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents RegistryFieldHandler
 *
 * <p>This handler builds the following methods
 *     <ul>
 *         <li>register[GROUP FIELD]</li>
 *         <li>unregister[GROUP FIELD]</li>
 *         <li>get[GROUP FIELD]</li>
 *         <li>is[GROUP FIELD]Register</li>
 *
 *         <li>registerAll[GROUP FIELD]</li>
 *         <li>unregisterAll[GROUP FIELD]</li>
 *         <li>getAll[GROUP FIELD]</li>
 *
 *     </ul>
 *     <h2>Simple Registry use cases</h2>
 *     For this example interface if field registry if present the followings implementations can be generated
 *     <pre>

    public static interface  ISimpleRegistry {

        IEntry get(Integer id);

        void register(IEntry entry);

        boolean isRegistered(Integer id);

        void unregister(Integer id);

        void unregister(IEntry entry);

    }



    public static interface IEntry {

        int getId();

        String getKey();

        Integer getValue();

        String getName();

    }
 *     </pre>
<ol>
 <li><b>Basic implementation</b>
    Default key provider is built based on matching a key type with the first encountered value type's field type.
    This default matching can be customized by using annotation on value's field to point a source for a key provider
    see {@link org.abstractmeta.code.g.core.builder.handler.field.RegistryFieldHandler.Config#getRegistryKeyAnnotation()}
 <pre>
package com.test;

import java.util.Map;

public class SimpleRegistry implements ISimpleRegistry {

    private final Map&lt;Integer, IEntry> registry;

    public SimpleRegistry(Map&lt;Integer, IEntry> registry) {
        super();
        this.registry = registry;
    }

    public Map&lt;Integer, IEntry> getRegistry() {
        return this.registry;
    }

    public void register(IEntry argument0) {
        registry.put(argument0.getValue(), argument0);
    }

    public void unregister(Integer argument0) {
        registry.remove(argument0);
    }

    public void unregister(IEntry argument0) {
        registry.remove(argument0.getValue());
    }

    public boolean isRegistered(Integer argument0) {
        return registry.containsKey(argument0);
    }

    public IEntry get(Integer argument0) {
        return registry.get(argument0);
    }

}

 </pre></li>
 * <li><b>Custom Key Provider implementation</b>
 <pre>
package com.test;

import com.google.common.base.Function;
import java.util.Map;

public class SimpleRegistry implements ISimpleRegistry {
    private Function&lt;IEntry, Integer> keyProvider = new KeyProvider();
    private final Map&lt;Integer, IEntry> registry;

    public SimpleRegistry(Map&lt;Integer, IEntry> registry) {
        super();
        this.registry = registry;
    }

    public Map&lt;Integer, IEntry> getRegistry() {
        return this.registry;
    }

    public void setKeyProvider(Function&lt;IEntry, Integer> keyProvider) {
        this.keyProvider = keyProvider;
    }

    public Function&lt;IEntry, Integer> getIdAccessor() {
        return this.keyProvider;
    }

    public void register(IEntry argument0) {
        registry.put(keyProvider.apply(argument0), argument0);
    }

    public void unregister(Integer argument0) {
        registry.remove(argument0);
    }

    public void unregister(IEntry argument0) {
        registry.remove(keyProvider.apply(argument0));
    }

    public boolean isRegistered(Integer argument0) {
        return registry.containsKey(argument0);
    }

    public IEntry get(Integer argument0) {
        return registry.get(argument0);
    }


    public static class KeyProvider implements Function&lt;IEntry, Integer> {


        public Integer apply(IEntry value) {
            return value.getValue();
        }

    }
}

 </pre></li></ol>
 * <h2>Multi Level Registry use case</h2>
 *  For this example interface if field registry if present the followings implementation can be generated
 * <p>
 *     <i>Note that any key leve depth is supported as long a registry field is supplied as generic type</i>
 * </p>
 <pre>
 public static interface IMultiLevelRegistry {

     IEntry get(Integer id, String key);

     void register(Integer id, String key, IEntry entry);

     void unregister(Integer id, String key);

     boolean isRegistered(Integer id, String key);

 }
 </pre>
 *<ol> <li><b>Implementation</b>
 <pre>
package com.test;

import java.util.HashMap;
import java.util.Map;

public class MultiLevelRegistry implements IMultiLevelRegistry {

    private final Map&lt;Integer, Map&lt;String, IEntry>> registry;

    public MultiLevelRegistry(Map&lt;Integer, Map&lt;String, IEntry>> registry) {
        super();
        this.registry = registry;
    }

    public Map&lt;Integer, Map&lt;String, IEntry>> getRegistry() {
        return this.registry;
    }

    protected  &lt;K,V> Map&lt;K, V> makeMap() {
        return new HashMap&lt;K, V>();
    }

    public void register(Integer argument0, String argument1, IEntry argument2) {
        Map&lt;String, IEntry> registryArgument0 = registry.get(argument0);
        if(registryArgument0 == null) {
        registryArgument0 = makeMap();
        registry.put(argument0, registryArgument0);
        }
        registryArgument0.put(argument1, argument2);
    }

    public void unregister(Integer argument0, String argument1) {
         if(!registry.containsKey(argument0)) return ;
             Map&lt;String, IEntry> registryArgument0 = registry.get(argument0);
             if(registryArgument0 == null) {
                return ;
             }
         registryArgument0.remove(argument1);
    }

    public boolean isRegistered(Integer argument0, String argument1) {
        if(!registry.containsKey(argument0)) return false;
            Map<String, IEntry> registryArgument0 = registry.get(argument0);
            if(registryArgument0 == null) {
            return false;
            }
        return registryArgument0.containsKey(argument1);
    }

    public IEntry get(Integer argument0, String argument1) {
        Map<String, IEntry> registryArgument0 = registry.get(argument0);
        if(registryArgument0 == null) {
            return null;
        }
        return registryArgument0.get(argument1);
    }

 }
 </pre>    
 *</li>
 *</ol>
 * <h2>Group Registry use case</h2>
 * <p>In this case matching is applied to field group name like registerFoo, isFooRegistered, etc ...</p>
 *  For this example interface if field entryRegistry if present the followings implementation can be generated
 <pre>
public static interface IGroupRegistry {

    String getSource();
    
    void registerEntry(IEntry entry);
    
    void unregisterEntry(IEntry entry);
    
    IEntry getEntry(Integer id);
    
    boolean isEntryRegistered(Integer id);
    
    Map&lt;Integer, IEntry> getEntryRegistry();
}
</pre>
 *<ol> <li><b>Implementation</b>
 <pre>
package com.test;

import java.util.Map;

public class SimpleRegistry implements IGroupRegistry {
    private String field;
    private final Map&lt;Integer, IEntry> entryRegistry;

    public SimpleRegistry(Map&lt;Integer, IEntry> entryRegistry) {
        super();
        this.entryRegistry = entryRegistry;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getSource() {
        return this.field;
    }

    public Map&lt;Integer, IEntry> getEntryRegistry() {
        return this.entryRegistry;
    }

    public void registerEntry(IEntry argument0) {
        entryRegistry.put(argument0.getValue(), argument0);
    }

    public void unregisterEntry(IEntry argument0) {
        entryRegistry.remove(argument0.getValue());
    }

    public boolean isEntryRegistered(Integer argument0) {
        return entryRegistry.containsKey(argument0);
    }

    public IEntry getEntry(Integer argument0) {
        return entryRegistry.get(argument0);
    }

}
 </pre>
 </li></ol>
 * 
 * </p>
 *
 * @author Adrian Witas
 */
public class RegistryFieldHandler implements FieldHandler {

    public static final String DEFAULT_REGISTRY_FIELD = "registry";
    public static final String DEFAULT_MAKE_MAP_METHOD = "makeMap";


    private final MethodMatcher methodMatcher;

    public RegistryFieldHandler(MethodMatcher methodMatcher) {
        this.methodMatcher = methodMatcher;
    }

    @Override
    public void handle(JavaTypeBuilder owner, JavaField target, Context context) {
        if (!isApplicable(owner, target, context)) return;

        JavaType source = owner.getSourceType();
        Map<String, AbstractionMatch> sourceMatches = methodMatcher.indexByName(methodMatcher.match(source.getMethods(), AbstractionPatterns.REGISTRY_PATTERN));
        String registryFieldName = getRegistryFieldName(context);
        String groupName = getMatchedGroupName(target, registryFieldName);
        AbstractionMatch groupMatch = sourceMatches.get(groupName);
        buildRegistryMethods(owner, target, groupMatch, context);
    }

    protected boolean isApplicable(JavaTypeBuilder owner, JavaField target, Context context) {
        Class rawFieldType = ReflectUtil.getRawClass(target.getType());
        if (!Map.class.isAssignableFrom(rawFieldType)) return false;
        if(owner.getSourceType() == null) return false;
        String registryFieldName = getRegistryFieldName(context);
        return target.getName().toLowerCase().endsWith(registryFieldName);
    }

    protected String getRegistryFieldName(Context context) {
        if(! context.contains(Config.class)) return DEFAULT_REGISTRY_FIELD;
        return CodeGeneratorUtil.getValue(context.get(Config.class).getRegistryFieldName(), DEFAULT_REGISTRY_FIELD);
    }

    protected String getCreateMapMethodName(Context context) {
        if(! context.contains(Config.class)) return DEFAULT_MAKE_MAP_METHOD;
        return CodeGeneratorUtil.getValue(context.get(Config.class).getRegistryCreateMapMethodName(), DEFAULT_MAKE_MAP_METHOD);
    }


    protected boolean isUseKeyProvider(Context context) {
        return context.contains(Config.class) && Boolean.TRUE.equals(context.get(Config.class).isRegistryItemUseKeyProvider());
    }


    protected String getMatchedGroupName(JavaField field, String registryFiledName) {
        String fieldName = field.getName();
        if (fieldName.equals(registryFiledName)) {
            return AbstractionMatch.EMPTY_GROUP_NAME;
        }
        String upperCamelFieldName = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, fieldName);
        return upperCamelFieldName.substring(0, upperCamelFieldName.length() - registryFiledName.length());
    }

    private void buildRegistryMethods(JavaTypeBuilder owner, JavaField target, AbstractionMatch groupMatch, Context context) {
        for (MethodMatch match : groupMatch.getMatches()) {
            JavaMethod sourceMatchedMethod = match.getMethod();
            JavaMethodBuilder methodBuilder = new JavaMethodBuilder()
                    .setName(sourceMatchedMethod.getName())
                    .setResultType(sourceMatchedMethod.getResultType());
            methodBuilder.addModifiers(Collections2.filter(sourceMatchedMethod.getModifiers(), new ExcludeJavaModifierPredicate(JavaModifier.ABSTRACT)));
            methodBuilder.addParameters(sourceMatchedMethod.getParameters());
            buildRegistryMethod(owner, methodBuilder, groupMatch, sourceMatchedMethod, target, context);
            owner.addMethod(methodBuilder.build());
        }
    }

    protected void buildRegistryMethod(JavaTypeBuilder owner, JavaMethodBuilder methodBuilder, AbstractionMatch groupMatch, JavaMethod sourceMatchedMethod, JavaField targetField, Context context) {
        String methodName = methodBuilder.getName();
        String groupName = groupMatch.getName();
        if (methodName.startsWith("registerAll")) {
            buildRegisterAllMethod(methodBuilder, groupMatch, sourceMatchedMethod, targetField);

        } else if (methodName.startsWith("unregisterAll")) {
            buildUnregisterAllMethod(methodBuilder, targetField);

        } else if (methodName.startsWith("register")) {
            buildRegisterMethod(owner, methodBuilder, groupMatch, sourceMatchedMethod, targetField, context);

        } else if (methodName.startsWith("unregister")) {
            buildUnregisteredMethod(owner, methodBuilder, groupMatch, sourceMatchedMethod, targetField, context);

        } else if (methodName.startsWith("getAll")) {
            buildGetAllMethod(methodBuilder, targetField);


        } else if (methodName.startsWith("get")) {
            buildGetMethod(owner, methodBuilder, sourceMatchedMethod, targetField, context);


        } else if (methodName.startsWith("is") && methodName.endsWith("Registered")) {
            if (AbstractionMatch.EMPTY_GROUP_NAME.equals(groupName) || methodName.contains(groupName)) {
                buildIsRegisteredMethod(owner, methodBuilder, sourceMatchedMethod, targetField, context);
            }
        }
    }

    protected void buildRegisterAllMethod(JavaMethodBuilder methodBuilder, AbstractionMatch groupMatch, JavaMethod javaMethod, JavaField javaField) {
        MethodMatch registerMatch = groupMatch.getMatch("register", Object[].class);
        String parameterName = javaMethod.getParameters().get(0).getName();
        Type[] genericActualTypeArguments = ReflectUtil.getGenericActualTypeArguments(javaField.getType());
        Class collectionType = ReflectUtil.getGenericClassArgument(genericActualTypeArguments, 1, Object.class);
        methodBuilder.addBodyLines(String.format("for(%s item: %s) { ", collectionType.getSimpleName(), parameterName));
        methodBuilder.addBodyLines(String.format("    %s(item);", registerMatch.getMethod().getName()));
        methodBuilder.addBodyLines("}");
    }


    protected void buildRegisterMethod(JavaTypeBuilder owner, JavaMethodBuilder methodBuilder, AbstractionMatch groupMatch, JavaMethod registerMethod, JavaField javaField, Context context) {
        MethodMatch getMethodMatch = groupMatch.getMatch("get", Object.class);
        if (registerMethod.getParameters().size() == 1) {
            buildSingleParameterRegisterMethod(owner, methodBuilder, getMethodMatch, registerMethod, javaField, context);
        } else {
            buildMultiParameterRegisterMethod(owner, methodBuilder, registerMethod, javaField, context);
        }
    }


    protected void buildSingleParameterRegisterMethod(JavaTypeBuilder owner, JavaMethodBuilder methodBuilder, MethodMatch getMethodMatch, JavaMethod registerMethod, JavaField javaField, Context context) {
        String registerArgumentName = registerMethod.getParameters().get(0).getName();
        Type registryKeyType = getMethodMatch.getMethod().getParameters().get(0).getType();
        Type registryValueType = registerMethod.getParameters().get(0).getType();

        String annotation  = context.get(Config.class).getRegistryKeyAnnotation();
        JavaMethod accessor =  JavaTypeUtil.matchOwnerFieldWithMatchingType(registryValueType, registryKeyType, annotation);
        if(accessor == null) {
            throw new IllegalStateException("Failed to match accessor " + registryValueType + " with " + registryKeyType);
        }
        boolean useKeyProvider = isUseKeyProvider(context);
        if (useKeyProvider) {
            buildKeyProviderField(owner, javaField, accessor, registryValueType);
            String providerFieldName =  JavaTypeUtil.getKeyProviderFieldName(javaField.getName());
            methodBuilder.addBodyLines(String.format("%s.put(%s.apply(%s), %s);", javaField.getName(), providerFieldName, registerArgumentName, registerArgumentName));

        } else {

            methodBuilder.addBodyLines(String.format("%s.put(%s.%s(), %s);", javaField.getName(), registerArgumentName, accessor.getName(), registerArgumentName));
        }
    }



    protected void buildMultiParameterRegisterMethod(JavaTypeBuilder owner, JavaMethodBuilder methodBuilder,  JavaMethod registerMethod, JavaField javaField, Context context) {
        if (!(javaField.getType() instanceof ParameterizedType)) return;
        String mapFieldName = javaField.getName();
        List<String> parameterNames = JavaTypeUtil.getParameterNames(registerMethod.getParameters());
        Type valueType = javaField.getType();
        String createMapMethodName = getCreateMapMethodName(context);
        for (int i = 0; i + 1 < parameterNames.size(); i++) {
            String keyArgumentName = parameterNames.get(i);
            String valueArgumentName = parameterNames.get(i + 1);
            if (i + 2 < parameterNames.size()) {
                buildCreateMapMethod(owner, context);
                String valueForThisKeyMap = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, mapFieldName) + "_" + keyArgumentName.toUpperCase());
                Type valueMapType = ParameterizedType.class.cast(valueType).getActualTypeArguments()[1];
                String valueMapTypeLiteral = owner.getImporter().getSimpleTypeName(valueMapType);
                methodBuilder.addBodyLines(String.format("%s %s = %s.get(%s);", valueMapTypeLiteral, valueForThisKeyMap, mapFieldName, keyArgumentName));
                methodBuilder.addBodyLines(String.format("if(%s == null) {", valueForThisKeyMap));
                methodBuilder.addBodyLines(String.format("    %s = %s();", valueForThisKeyMap, createMapMethodName));
                methodBuilder.addBodyLines(String.format("    %s.put(%s, %s);", mapFieldName, keyArgumentName, valueForThisKeyMap));
                methodBuilder.addBodyLines("}");
                mapFieldName = valueForThisKeyMap;
                valueType = valueMapType;
            } else {
                methodBuilder.addBodyLines(mapFieldName + ".put(" + keyArgumentName + ", " + valueArgumentName + ");");
            }
        }
    }


    protected  void buildKeyProviderField(JavaTypeBuilder owner, JavaField field, JavaMethod provideMethod, Type registryValueType) {
        String providerFieldName = JavaTypeUtil.getKeyProviderFieldName(field.getName());
        Type iFace = new ParameterizedTypeImpl(null, Function.class, registryValueType, provideMethod.getResultType());
        JavaTypeBuilder simpleType = JavaTypeUtil.buildKeyProviderFunction(provideMethod, field.getName(), registryValueType);
        owner.addNestedJavaTypes(simpleType);
        owner.addField(new JavaFieldBuilder()
                .addModifier(JavaModifier.PRIVATE)
                .setName(providerFieldName)
                .setClassInitValue(String.format(" = new %s()", simpleType.getSimpleName()))
                .setType(iFace)
        );
    }




    protected void buildGetAllMethod(JavaMethodBuilder methodBuilder, JavaField javaField) {
        methodBuilder.addBodyLines(String.format("return %s.values();", javaField.getName()));
    }


    protected void buildUnregisterAllMethod(JavaMethodBuilder methodBuilder, JavaField javaField) {
        methodBuilder.addBodyLines(String.format("%s.clear();", javaField.getName()));
    }


    protected void buildUnregisteredMethod(JavaTypeBuilder owner, JavaMethodBuilder methodBuilder, AbstractionMatch groupMatch, JavaMethod unregisteredMethod, JavaField javaField, Context context) {
        if (unregisteredMethod.getParameters().size() == 1) {
            buildSingleArgumentUnregisteredMethod(methodBuilder, groupMatch, unregisteredMethod, javaField, context);
        } else {
            buildMultipleArgumentUnregisteredMethods(owner, methodBuilder, unregisteredMethod, javaField, context);
        }
    }


    protected void buildSingleArgumentUnregisteredMethod(JavaMethodBuilder methodBuilder, AbstractionMatch groupMatch, JavaMethod unregisteredMethod, JavaField javaField, Context context) {
        String parameterName = unregisteredMethod.getParameters().get(0).getName();
        MethodMatch getMethodMatch = groupMatch.getMatch("get", Object.class);
        Type registryKeyType = getMethodMatch.getMethod().getParameters().get(0).getType();
        Type registryValueType = unregisteredMethod.getParameters().get(0).getType();
        if (registryKeyType.equals(registryValueType)) {
            methodBuilder.addBodyLines(String.format("%s.remove(%s);", javaField.getName(), parameterName));

        } else {
            String annotation = context.get(Config.class).getRegistryKeyAnnotation();
            JavaMethod accessor =  JavaTypeUtil.matchOwnerFieldWithMatchingType(registryValueType, registryKeyType, annotation);
            if(accessor == null) {
                throw new IllegalStateException("Failed to match accessor " + registryValueType + " " +registryKeyType);
            }
            boolean useKeyProvider = isUseKeyProvider(context);
            if (useKeyProvider) {
                String providerFieldName = JavaTypeUtil.getKeyProviderFieldName(javaField.getName());
                methodBuilder.addBodyLines(String.format("%s.remove(%s.apply(%s));", javaField.getName(), providerFieldName, parameterName));

            } else {
                methodBuilder.addBodyLines(String.format("%s.remove(%s.%s());", javaField.getName(), parameterName, accessor.getName()));
            }
        }

    }


    protected void buildMultipleArgumentUnregisteredMethods(JavaTypeBuilder owner, JavaMethodBuilder javaMethodBuilder, JavaMethod unregisteredMethod, JavaField javaField, Context context) {
        if (!(javaField.getType() instanceof ParameterizedType)) return;
        String mapFieldName = javaField.getName();
        List<String> parameterNames = JavaTypeUtil.getParameterNames(unregisteredMethod.getParameters());
        Type valueType = javaField.getType();
        for (int i = 0; i < parameterNames.size(); i++) {
            String keyArgumentName = parameterNames.get(i);
            if (i < parameterNames.size() - 1) {
                buildCreateMapMethod(owner, context);
                String valueForThisKeyMap = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, mapFieldName) + "_" + keyArgumentName.toUpperCase());
                Type valueMapType = ParameterizedType.class.cast(valueType).getActualTypeArguments()[1];
                String valueMapTypeLiteral = owner.getImporter().getSimpleTypeName(valueMapType);
                javaMethodBuilder.addBodyLines(String.format("if(!%s.containsKey(%s)) return ;", mapFieldName, keyArgumentName));
                javaMethodBuilder.addBodyLines(String.format("%s %s = %s.get(%s);", valueMapTypeLiteral, valueForThisKeyMap, mapFieldName, keyArgumentName));
                javaMethodBuilder.addBodyLines(String.format("if(%s == null) {", valueForThisKeyMap));
                javaMethodBuilder.addBodyLines("    return ;");
                javaMethodBuilder.addBodyLines("}");
                mapFieldName = valueForThisKeyMap;
                valueType = valueMapType;
            } else {
                javaMethodBuilder.addBodyLines("" + mapFieldName + ".remove(" + keyArgumentName + ");");
            }
        }
    }


    protected void buildGetMethod(JavaTypeBuilder owner, JavaMethodBuilder methodBuilder, JavaMethod getMethodMatch, JavaField javaField, Context context) {
        if (getMethodMatch.getParameters().size() == 1) {
            buildSingleParameterGetMethod(methodBuilder, getMethodMatch, javaField);
        } else {
            buildMultiParameterGetMethod(owner, methodBuilder, getMethodMatch, javaField, context);
        }

    }


    protected void buildMultiParameterGetMethod(JavaTypeBuilder owner, JavaMethodBuilder methodBuilder, JavaMethod getMethodMatch, JavaField javaField, Context context) {
        if (!(javaField.getType() instanceof ParameterizedType)) return;
        String mapFieldName = javaField.getName();
        List<String> parameterNames = JavaTypeUtil.getParameterNames(getMethodMatch.getParameters());
        Type valueType = javaField.getType();
        for (int i = 0; i < parameterNames.size(); i++) {
            String keyArgumentName = parameterNames.get(i);
            if (i < parameterNames.size() - 1) {
                buildCreateMapMethod(owner, context);
                String valueForThisKeyMap = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, mapFieldName) + "_" + keyArgumentName.toUpperCase());
                Type valueMapType = ParameterizedType.class.cast(valueType).getActualTypeArguments()[1];
                String valueMapTypeLiteral = owner.getImporter().getSimpleTypeName(valueMapType);
                methodBuilder.addBodyLines(String.format("%s %s = %s.get(%s);", valueMapTypeLiteral, valueForThisKeyMap, mapFieldName, keyArgumentName));
                methodBuilder.addBodyLines(String.format("if(%s == null) {", valueForThisKeyMap));
                methodBuilder.addBodyLines("    return null;");
                methodBuilder.addBodyLines("}");
                mapFieldName = valueForThisKeyMap;
                valueType = valueMapType;
            } else {
                methodBuilder.addBodyLines("return " + mapFieldName + ".get(" + keyArgumentName + ");");
            }
        }
    }


    protected void buildSingleParameterGetMethod(JavaMethodBuilder methodBuilder, JavaMethod getMethodMatch, JavaField javaField) {
        String parameterName = getMethodMatch.getParameters().get(0).getName();
        methodBuilder.addBodyLines(String.format("return %s.get(%s);", javaField.getName(), parameterName));
    }


    protected void buildCreateMapMethod(JavaTypeBuilder owner, Context context) {
        String createMapMethodName = getCreateMapMethodName(context);
        if (owner.containsMethod(createMapMethodName)) {
            return;
        }
        JavaMethodBuilder resultMethod = new JavaMethodBuilder();
        resultMethod.addModifiers(JavaModifier.PROTECTED);
        resultMethod.setName(createMapMethodName);
        TypeVariable keyType = new TypeVariableImpl("K", Map.class);
        TypeVariableImpl valueType = new TypeVariableImpl("V", Map.class);
        resultMethod.addGenericVariables(keyType, valueType);
        resultMethod.setResultType(new ParameterizedTypeImpl(null, Map.class, keyType, valueType));
        resultMethod.addBodyLines("return new " + HashMap.class.getSimpleName() + "<K, V>();");
        owner.addMethod(resultMethod.build());
        owner.getImporter().addTypes(HashMap.class);
    }


    protected void buildIsRegisteredMethod(JavaTypeBuilder owner, JavaMethodBuilder methodBuilder, JavaMethod isRegisteredMethod, JavaField javaField, Context context) {
        if (isRegisteredMethod.getParameters().size() == 1) {
            buildSingleParameterIsRegisteredMethod(methodBuilder, isRegisteredMethod, javaField);
        } else {
            buildMultiParametersIsRegisteredMethod(owner, methodBuilder, isRegisteredMethod, javaField, context);

        }
    }

    protected void buildMultiParametersIsRegisteredMethod(JavaTypeBuilder owner, JavaMethodBuilder methodBuilder, JavaMethod isRegisteredMethod, JavaField javaField, Context context) {
        if (!(javaField.getType() instanceof ParameterizedType)) return;
        String mapFieldName = javaField.getName();
        List<String> parameterNames = JavaTypeUtil.getParameterNames(isRegisteredMethod.getParameters());
        Type valueType = javaField.getType();
        for (int i = 0; i < parameterNames.size(); i++) {
            String keyArgumentName = parameterNames.get(i);
            if (i < parameterNames.size() - 1) {
                buildCreateMapMethod(owner, context);
                String valueForThisKeyMap = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, mapFieldName) + "_" + keyArgumentName.toUpperCase());
                Type valueMapType = ParameterizedType.class.cast(valueType).getActualTypeArguments()[1];
                String valueMapTypeLiteral = owner.getImporter().getSimpleTypeName(valueMapType);
                methodBuilder.addBodyLines(String.format("if(!%s.containsKey(%s)) return false;", mapFieldName, keyArgumentName));
                methodBuilder.addBodyLines(String.format("%s %s = %s.get(%s);", valueMapTypeLiteral, valueForThisKeyMap, mapFieldName, keyArgumentName));
                methodBuilder.addBodyLines(String.format("if(%s == null) {", valueForThisKeyMap));
                methodBuilder.addBodyLines("    return false;");
                methodBuilder.addBodyLines("}");
                mapFieldName = valueForThisKeyMap;
                valueType = valueMapType;
            } else {
                methodBuilder.addBodyLines("return " + mapFieldName + ".containsKey(" + keyArgumentName + ");");
            }
        }
    }

    protected void buildSingleParameterIsRegisteredMethod(JavaMethodBuilder methodBuilder, JavaMethod isRegisteredMethod, JavaField javaField) {
        String parameterName = isRegisteredMethod.getParameters().get(0).getName();
        methodBuilder.addBodyLines(String.format("return %s.containsKey(%s);", javaField.getName(), parameterName));
    }


    public static interface Config {


        /**
         * Returns a method to create a map user be registry. Default makeMap
         *
         * @return
         */
        String getRegistryCreateMapMethodName();

        /**
         * Returns a name used to match registry field, Default 'registry'
         *
         * @return
         */
        String getRegistryFieldName();


        /**
         * Returns a field annotation name to be used as key provider discriminator.
         *
         * @return
         */
        String getRegistryKeyAnnotation();


        /**
         * Flag to use key provider
         * It uses {@link Function} as default key provider.
         *
         * @return
         */
        boolean isRegistryItemUseKeyProvider();


    }

}
