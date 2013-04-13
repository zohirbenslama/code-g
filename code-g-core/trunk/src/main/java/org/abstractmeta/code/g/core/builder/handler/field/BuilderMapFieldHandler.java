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

import org.abstractmeta.code.g.code.JavaField;
import org.abstractmeta.code.g.code.JavaModifier;
import org.abstractmeta.code.g.code.JavaTypeBuilder;
import org.abstractmeta.code.g.code.handler.FieldHandler;
import org.abstractmeta.code.g.core.code.builder.JavaMethodBuilder;
import org.abstractmeta.code.g.core.util.CodeGeneratorUtil;
import org.abstractmeta.code.g.core.util.ReflectUtil;
import org.abstractmeta.code.g.generator.Context;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * This handle creates 'add', 'clear' methods for any Map type field.
 * For instance for the given source type
 * <code><pre>
 * class Bar {
 *      ...
 *      private Map<&lt;String, String> bars;
 *      ...
 * }
 * </code></pre>
 * The following code is generated
 * <code><pre>
 * public FooBuilder addBars(String key, String value) {
 *     this.bars.put(key, value);
 *     this._bars = true;
 *     return this;
 * }
 * <p/>
 * public FooBuilder addBars(Map&lt;String, String> bars) {
 *     this.bars.putAll(bars);
 *     this._bars = true;
 *     return this;
 * }
 * <p/>
 * public FooBuilder clearBars() {
 *    this.bars.clear();
 *    return this;
 * }
 * </pre></code>
 * </p>
 *
 * @author Adrian Witas
 */
public class BuilderMapFieldHandler implements FieldHandler {
    @Override
    public void handle(JavaTypeBuilder owner, JavaField javaField, Context context) {
        Type fieldType = javaField.getType();
        Class rawFieldType = ReflectUtil.getRawClass(fieldType);
        if (Map.class.isAssignableFrom(rawFieldType)) {
            addCollectionAddItemsMethod(owner, javaField.getName(), javaField.getType());
            addCollectionClearMethod(owner, javaField.getName());
        }
    }

    protected void addCollectionAddItemsMethod(JavaTypeBuilder owner, String fieldName, Type fieldType) {
        String methodName = CodeGeneratorUtil.getAddMethodName(fieldName);
        if (!owner.containsMethod(methodName)) {
            JavaMethodBuilder methodBuilder = new JavaMethodBuilder();
            methodBuilder.addModifier(JavaModifier.PUBLIC);
            methodBuilder.setName(methodName);
            methodBuilder.addParameter(fieldName, fieldType);
            methodBuilder.addBodyLines(String.format("this.%s.putAll(%s);", fieldName, fieldName));
            BuilderUtil.addIsPresentFlag(owner, fieldName, methodBuilder);
            BuilderUtil.addSetterResultType(owner, methodName, methodBuilder);
            owner.addMethod(methodBuilder.build());
            addCollectionAddMethods(owner, fieldName, fieldType);
        }
    }


    protected void addCollectionAddMethods(JavaTypeBuilder owner, String fieldName, Type fieldType) {
        String singularName = CodeGeneratorUtil.getSingular(fieldName);
        String methodName = CodeGeneratorUtil.getAddMethodName(singularName);
        JavaMethodBuilder methodBuilder = new JavaMethodBuilder();
        methodBuilder.addModifier(JavaModifier.PUBLIC);
        methodBuilder.setName(methodName);
        Type [] fieldGenericArguments = ReflectUtil.getGenericActualTypeArguments(fieldType);
        Type keyType = ReflectUtil.getGenericClassArgument(fieldGenericArguments, 0, Object.class);
        Type valueType = ReflectUtil.getGenericClassArgument(fieldGenericArguments, 1, Object.class);
        methodBuilder.addParameter("key", keyType);
        methodBuilder.addParameter("value", valueType);
        methodBuilder.addBodyLines(String.format("this.%s.put(key, value);", fieldName));
        BuilderUtil.addIsPresentFlag(owner, fieldName, methodBuilder);
        BuilderUtil.addSetterResultType(owner, methodName, methodBuilder);
        owner.addMethod(methodBuilder.build());
    }


    protected void addCollectionClearMethod(JavaTypeBuilder owner, String fieldName) {
        String methodName = CodeGeneratorUtil.getClearMethodName(fieldName);
        if (!owner.containsMethod(methodName)) {
            JavaMethodBuilder methodBuilder = new JavaMethodBuilder();
            methodBuilder.addModifier(JavaModifier.PUBLIC);
            methodBuilder.setName(methodName);
            methodBuilder.addBodyLines(String.format("this.%s.clear();", fieldName));
            BuilderUtil.addIsPresentFlag(owner, fieldName, methodBuilder);
            BuilderUtil.addSetterResultType(owner, methodName, methodBuilder);
            owner.addMethod(methodBuilder.build());
        }
    }
}

