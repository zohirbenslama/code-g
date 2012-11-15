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

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;

/**
 * This handler creates 'add', 'clear' methods for any collection type field.
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
public class BuilderCollectionFieldHandler implements JavaFieldHandler {

    private final JavaTypeBuilder ownerTypeBuilder;
    private final Descriptor descriptor;

    public BuilderCollectionFieldHandler(JavaTypeBuilder ownerTypeBuilder,Descriptor descriptor) {
        this.ownerTypeBuilder = ownerTypeBuilder;
        this.descriptor = descriptor;

    }


    @Override
    public void handle(JavaType sourceType, JavaField javaField) {
        Type fieldType = javaField.getType();
        Class rawFieldType = ReflectUtil.getRawClass(fieldType);
        String fieldName = javaField.getName();
        String methodName = StringUtil.format(CaseFormat.LOWER_CAMEL, "clear", fieldName, CaseFormat.LOWER_CAMEL);

        if (Collection.class.isAssignableFrom(rawFieldType) && !ownerTypeBuilder.containsMethod(methodName)) {
            addCollectionAddItemMethod(ownerTypeBuilder, javaField.getName(), javaField.getType());
            addCollectionAddItemsMethod(ownerTypeBuilder, javaField.getName(), javaField.getType());
            addCollectionClearMethod(ownerTypeBuilder, javaField.getName());
        }
    }

    protected void addCollectionAddItemsMethod(JavaTypeBuilder typeBuilder, String fieldName, Type fieldType) {
        String methodName = StringUtil.format(CaseFormat.LOWER_CAMEL, "add", fieldName, CaseFormat.LOWER_CAMEL);
        JavaMethodBuilder methodBuilder = new JavaMethodBuilder();
        methodBuilder.addModifier("public");
        methodBuilder.setResultType(new TypeNameWrapper(typeBuilder.getName()));
        methodBuilder.setName(methodName);
        methodBuilder.addParameter(fieldName, fieldType);
        methodBuilder.addBody(String.format("this.%s.addAll(%s);", fieldName, fieldName));
        if(! DescriptorUtil.is(descriptor, BuilderGeneratorPlugin.SKIP_PRESENT_METHOD) ) {
            methodBuilder.addBody(String.format("this.%s = true;", StringUtil.isPresentFieldName(fieldName)));
        }
        methodBuilder.addBody("return this;");
        typeBuilder.addMethod(methodBuilder.build());
    }


    protected void addCollectionAddItemMethod(JavaTypeBuilder typeBuilder, String fieldName, Type fieldType) {
        String methodName = StringUtil.format(CaseFormat.LOWER_CAMEL, "add", fieldName, CaseFormat.LOWER_CAMEL);
        JavaMethodBuilder methodBuilder = new JavaMethodBuilder();
        methodBuilder.addModifier("public");
        methodBuilder.setResultType(new TypeNameWrapper(typeBuilder.getName()));
        methodBuilder.setName(methodName);
        Type componentType =  ReflectUtil.getGenericArgument(fieldType, 0, Object.class);
        methodBuilder.addParameter("..." + fieldName, componentType);
        ownerTypeBuilder.addImportType(Collections.class);
        methodBuilder.addBody(String.format("Collections.addAll(this.%s, %s);", fieldName, fieldName));
        if(! DescriptorUtil.is(descriptor, BuilderGeneratorPlugin.SKIP_PRESENT_METHOD)) {
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
