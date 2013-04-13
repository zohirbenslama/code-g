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
import org.abstractmeta.code.g.core.code.builder.JavaParameterBuilder;
import org.abstractmeta.code.g.core.internal.GenericArrayTypeImpl;
import org.abstractmeta.code.g.core.util.CodeGeneratorUtil;
import org.abstractmeta.code.g.core.util.ReflectUtil;
import org.abstractmeta.code.g.generator.Context;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;

/**
 * This handle creates 'add', 'clear' methods for any collection type field.
 * <p>For instance for the given source type
 * <code><pre>
 * class Bar {
 *      ...
 *      private Collection&lt;Integer> bars;
 *      ...
 * }
 * </code></pre>
 * The following code is generated
 * <code><pre>
 * public FooBuilder setBars(List&lt;String> bars) {
 *     this.bars = bars;
 *     this._bars = true;
 *     return this;
 * }
 * <p/>
 * public FooBuilder addBars(String ...bars) {
 *     Collections.addAll(this.bars, bars);
 *     this._bars = true;
 *     return this;
 * }
 * <p/>
 * public FooBuilder addBars(List&lt;String> bars) {
 *     this.bars.addAll(bars);
 *     this._bars = true;
 *     return this;
 * }
 * <p/>
 * public FooBuilder clearBars() {
 *     this.bars.clear();
 *     return this;
 * }
 * </pre></code>
 * <p>
 * <p/>
 * </p>
 *
 * @author Adrian Witas
 */
public class BuilderCollectionFieldHandler implements FieldHandler {
    @Override
    public void handle(JavaTypeBuilder owner, JavaField javaField, Context context) {
        Type fieldType = javaField.getType();
        Class rawFieldType = ReflectUtil.getRawClass(fieldType);
        String fieldName = javaField.getName();
        String methodName = CodeGeneratorUtil.getClearMethodName(fieldName);

        if (Collection.class.isAssignableFrom(rawFieldType) && !owner.containsMethod(methodName)) {
            addCollectionAddItemMethod(owner, javaField.getName(), javaField.getType());
            addCollectionAddItemsMethod(owner, javaField.getName(), javaField.getType());
            addCollectionClearMethod(owner, javaField.getName());
        }
    }

    protected void addCollectionAddItemsMethod(JavaTypeBuilder owner, String fieldName, Type fieldType) {
        String methodName = CodeGeneratorUtil.getAddMethodName(fieldName);
        JavaMethodBuilder methodBuilder = new JavaMethodBuilder();
        methodBuilder.addModifier(JavaModifier.PUBLIC);
        methodBuilder.setName(methodName);
        methodBuilder.addParameter(fieldName, fieldType);
        methodBuilder.addBodyLines(String.format("this.%s.addAll(%s);", fieldName, fieldName));
        BuilderUtil.addIsPresentFlag(owner, fieldName, methodBuilder);
        BuilderUtil.addSetterResultType(owner, methodName, methodBuilder);
        owner.addMethod(methodBuilder.build());
    }


    protected void addCollectionAddItemMethod(JavaTypeBuilder owner, String fieldName, Type fieldType) {
        String methodName = CodeGeneratorUtil.getAddMethodName(fieldName);
        JavaMethodBuilder methodBuilder = new JavaMethodBuilder();
        methodBuilder.addModifier(JavaModifier.PUBLIC);
        methodBuilder.setName(methodName);
        Type [] fieldGenericArguments = ReflectUtil.getGenericActualTypeArguments(fieldType);
        Type componentType =  ReflectUtil.getGenericClassArgument(fieldGenericArguments, 0, Object.class);
        methodBuilder.addParameters(new JavaParameterBuilder().setName(fieldName).setType(new GenericArrayTypeImpl(componentType)).setVarTypeArgument(true).build());
        owner.getImporter().addTypes(Collections.class);
        methodBuilder.addBodyLines(String.format("Collections.addAll(this.%s, %s);", fieldName, fieldName));
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
