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

import org.abstractmeta.code.g.code.JavaField;
import org.abstractmeta.code.g.code.JavaType;
import org.abstractmeta.code.g.config.Descriptor;
import org.abstractmeta.code.g.core.code.builder.JavaMethodBuilder;
import org.abstractmeta.code.g.core.code.builder.JavaTypeBuilder;
import org.abstractmeta.code.g.core.internal.TypeNameWrapper;
import org.abstractmeta.code.g.core.plugin.BuilderGeneratorPlugin;
import org.abstractmeta.code.g.core.util.DescriptorUtil;
import org.abstractmeta.code.g.core.util.ReflectUtil;
import org.abstractmeta.code.g.core.util.StringUtil;
import org.abstractmeta.code.g.handler.JavaFieldHandler;
import com.google.common.base.CaseFormat;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * This handler creates 'add', 'clear' methods for any Map type field.
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
public class BuilderMapFieldHandler implements JavaFieldHandler {

    private final JavaTypeBuilder ownerTypeBuilder;
    private final Descriptor descriptor;

    public BuilderMapFieldHandler(JavaTypeBuilder ownerTypeBuilder, Descriptor descriptor) {
        this.ownerTypeBuilder = ownerTypeBuilder;
        this.descriptor = descriptor;
    }


    @Override
    public void handle(JavaType sourceType, JavaField javaField) {
        Type fieldType = javaField.getType();
        Class rawFieldType = ReflectUtil.getRawClass(fieldType);
        if (Map.class.isAssignableFrom(rawFieldType)) {
            addCollectionAddItemsMethod(ownerTypeBuilder, javaField.getName(), javaField.getType());
            addCollectionClearMethod(ownerTypeBuilder, javaField.getName());
        }
    }

    protected void addCollectionAddItemsMethod(JavaTypeBuilder typeBuilder, String fieldName, Type fieldType) {
        String methodName = StringUtil.format(CaseFormat.LOWER_CAMEL, "add", fieldName, CaseFormat.LOWER_CAMEL);
        if (!typeBuilder.containsMethod(methodName)) {
            JavaMethodBuilder methodBuilder = new JavaMethodBuilder();
            methodBuilder.addModifier("public");
            methodBuilder.setResultType(new TypeNameWrapper(typeBuilder.getName()));
            methodBuilder.setName(methodName);
            methodBuilder.addParameter(fieldName, fieldType);
            methodBuilder.addBody(String.format("this.%s.putAll(%s);", fieldName, fieldName));
            if (! DescriptorUtil.is(descriptor, BuilderGeneratorPlugin.SKIP_PRESENT_METHOD)) {
                methodBuilder.addBody(String.format("this.%s = true;", StringUtil.isPresentFieldName(fieldName)));
            }
            methodBuilder.addBody("return this;");
            typeBuilder.addMethod(methodBuilder.build());
            addCollectionAddItemsMethod(ownerTypeBuilder, fieldName, fieldType);
        }
    }


    protected void addCollectionAddMethods(JavaTypeBuilder typeBuilder, String fieldName, Type fieldType) {
        String singularName = StringUtil.getSingular(fieldName);
        String methodName = StringUtil.format(CaseFormat.LOWER_CAMEL, "add", singularName, CaseFormat.LOWER_CAMEL);
        JavaMethodBuilder methodBuilder = new JavaMethodBuilder();
        methodBuilder.addModifier("public");
        methodBuilder.setResultType(new TypeNameWrapper(typeBuilder.getName()));
        methodBuilder.setName(methodName);
        Type keyType = ReflectUtil.getGenericArgument(fieldType, 0, Object.class);
        Type valueType = ReflectUtil.getGenericArgument(fieldType, 1, Object.class);
        methodBuilder.addParameter("key", keyType);
        methodBuilder.addParameter("value", valueType);
        methodBuilder.addBody(String.format("this.%s.put(key, value);", fieldName));
        if (! DescriptorUtil.is(descriptor, BuilderGeneratorPlugin.SKIP_PRESENT_METHOD)) {
            methodBuilder.addBody(String.format("this.%s = true;", StringUtil.isPresentFieldName(fieldName)));
        }
        methodBuilder.addBody("return this;");
        typeBuilder.addMethod(methodBuilder.build());
    }


    protected void addCollectionClearMethod(JavaTypeBuilder typeBuilder, String fieldName) {
        String methodName = StringUtil.format(CaseFormat.LOWER_CAMEL, "clear", fieldName, CaseFormat.LOWER_CAMEL);
        if (!typeBuilder.containsMethod(methodName)) {
            JavaMethodBuilder methodBuilder = new JavaMethodBuilder();
            methodBuilder.addModifier("public");
            methodBuilder.setResultType(new TypeNameWrapper(typeBuilder.getName()));
            methodBuilder.setName(methodName);
            methodBuilder.addBody(String.format("this.%s.clear();", fieldName));
            methodBuilder.addBody("return this;");
            typeBuilder.addMethod(methodBuilder.build());
        }
    }
}

