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
import org.abstractmeta.code.g.core.internal.ParameterizedTypeImpl;
import org.abstractmeta.code.g.core.util.CodeGeneratorUtil;
import org.abstractmeta.code.g.core.util.ReflectUtil;
import org.abstractmeta.code.g.generator.Context;

import java.lang.reflect.Type;
import java.util.Collection;

/**
 * This handle creates 'add', 'clear' methods for any arrays type field.
 * <p>For instance for the given source type
 * <code><pre>
 * class Bar {
 *      ...
 *      private int [] bars;
 *      ...
 *  }
 * </pre></code> the following code is generated
 * <code><pre>
 * public FooBuilder addBars(int ... bars) {
 *     int [] temp = new int[this.bars.length + bars.length];
 *     System.arraycopy(this.bars, 0, temp, 0, this.bars.length);
 *     System.arraycopy(bars, 0, temp, this.bars.length, bars.length);
 *     this.bars = temp;
 *     this._bars = true;
 *     return this;
 * }
 *
 * public FooBuilder addBars(Collection&lt;Integer&gt; bars) {
 *     int [] temp = new int[this.bars.length + bars.size()];
 *     System.arraycopy(this.bars, 0, temp, 0, this.bars.length);
 *     int i = this.bars.length;
 *     for(int item: bars) temp[i++] = item;
 *     this.bars = temp;
 *     this._bars = true;
 *     return this;
 * }
 *
 * public FooBuilder clearBars() {
 *     this.bars = new int[]{};
 *     return this;
 * }
 * <p/>
 * <p/>
 * </pre></code>
 * </p>
 *
 * @author Adrian Witas
 */
public class BuilderArrayFieldHandler implements FieldHandler {


    @Override
    public void handle(JavaTypeBuilder owner, JavaField javaField, Context context) {
        Type fieldType = javaField.getType();
        Class rawFieldType = ReflectUtil.getRawClass(fieldType);
        String fieldName = javaField.getName();
        if (rawFieldType.isArray()) {
            String methodName = CodeGeneratorUtil.getAddMethodName(fieldName);
            if (! owner.containsMethod(methodName)) {
                addArrayAddItemMethod(owner, javaField.getName(), javaField.getType());
                addArrayAddItemsMethod(owner, javaField.getName(), javaField.getType());
                addCollectionClearMethod(owner, javaField.getName(), javaField.getType());
            }
        }
    }

    protected void addArrayAddItemsMethod(JavaTypeBuilder owner, String fieldName, Type fieldType) {
        String methodName = CodeGeneratorUtil.getAddMethodName(fieldName);
        Class componentType = ReflectUtil.getRawClass(fieldType).getComponentType();
        JavaMethodBuilder methodBuilder =  new  JavaMethodBuilder();
        methodBuilder.addModifiers(JavaModifier.PUBLIC);
        methodBuilder.setName(methodName);
        methodBuilder.addParameter(fieldName, new ParameterizedTypeImpl(null, Collection.class, ReflectUtil.getObjectType(componentType)));
        methodBuilder.addBodyLines(String.format("%s [] temp = new %s[this.%s.length + %s.size()];", componentType.getSimpleName(), componentType.getSimpleName(), fieldName, fieldName));
        methodBuilder.addBodyLines(String.format("System.arraycopy(this.%s, 0, temp, 0, this.%s.length);", fieldName, fieldName));
        methodBuilder.addBodyLines(String.format("int i = this.%s.length;", fieldName));
        methodBuilder.addBodyLines(String.format("for(%s item: %s) temp[i++] = item;", componentType.getSimpleName(), fieldName));
        methodBuilder.addBodyLines(String.format("this.%s = temp;", fieldName));
        BuilderUtil.addIsPresentFlag(owner, fieldName, methodBuilder);
        BuilderUtil.addSetterResultType(owner, methodName, methodBuilder);
        owner.addMethod(methodBuilder.build());
    }

    protected void addArrayAddItemMethod(JavaTypeBuilder owner, String fieldName, Type fieldType) {
        String methodName = CodeGeneratorUtil.getAddMethodName(fieldName);
        Class componentType = ReflectUtil.getRawClass(fieldType).getComponentType();
        JavaMethodBuilder methodBuilder =  new  JavaMethodBuilder();
        methodBuilder.addModifiers(JavaModifier.PUBLIC);
        methodBuilder.setName(methodName);
        methodBuilder.addParameter("... " + fieldName, componentType);
        methodBuilder.addBodyLines(String.format("%s [] temp = new %s[this.%s.length + %s.length];", componentType.getSimpleName(), componentType.getSimpleName(), fieldName, fieldName));
        methodBuilder.addBodyLines(String.format("System.arraycopy(this.%s, 0, temp, 0, this.%s.length);", fieldName, fieldName));
        methodBuilder.addBodyLines(String.format("System.arraycopy(%s, 0, temp, this.%s.length, %s.length);", fieldName, fieldName, fieldName));
        methodBuilder.addBodyLines(String.format("this.%s = temp;", fieldName));
        BuilderUtil.addIsPresentFlag(owner, fieldName, methodBuilder);
        BuilderUtil.addSetterResultType(owner, methodName, methodBuilder);
        owner.addMethod(methodBuilder.build());
    }

    protected void addCollectionClearMethod(JavaTypeBuilder owner, String fieldName, Type fieldType) {
        String methodName = CodeGeneratorUtil.getClearMethodName(fieldName);
        if (!owner.containsMethod(methodName)) {
            JavaMethodBuilder methodBuilder = new JavaMethodBuilder();
            methodBuilder.addModifiers(JavaModifier.PUBLIC);
            methodBuilder.setName(methodName);
            Class rawClass = ReflectUtil.getRawClass(fieldType).getComponentType();
            methodBuilder.addBodyLines(String.format("this.%s = new %s[]{};", fieldName, rawClass.getSimpleName()));
            BuilderUtil.addIsPresentFlag(owner, fieldName, methodBuilder);
            BuilderUtil.addSetterResultType(owner, methodName, methodBuilder);
            owner.addMethod(methodBuilder.build());
        }
    }

}
