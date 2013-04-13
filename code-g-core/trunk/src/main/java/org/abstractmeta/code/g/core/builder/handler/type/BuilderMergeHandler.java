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

import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import org.abstractmeta.code.g.code.*;
import org.abstractmeta.code.g.code.handler.TypeHandler;
import org.abstractmeta.code.g.core.code.builder.JavaMethodBuilder;
import org.abstractmeta.code.g.core.collection.function.MethodNameKeyFunction;
import org.abstractmeta.code.g.core.internal.TypeNameWrapper;
import org.abstractmeta.code.g.core.provider.ClassTypeProvider;
import org.abstractmeta.code.g.core.util.CodeGeneratorUtil;
import org.abstractmeta.code.g.core.util.JavaTypeUtil;
import org.abstractmeta.code.g.core.util.ReflectUtil;
import org.abstractmeta.code.g.generator.Context;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

/**
 * This handle creates merge method.
 * <p>Merge uses the following rules:
 * <ul>
 * <li>All primitives types field value is overwritten</li>
 * <li>Collection, Map, array type field values are appended</li>
 * <li>All other types are checked for null and if not then the overwritten</li>
 * </ul>
 * <p/>
 * </p>
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
 * public FooBuilder merge(Bar instance) {
 *     this.setId(instance.getId());
 *     if(instance.getBar() != null) {
 *         this.setBar(getBar());
 *      }
 *      if(instance.getDummy() != null) {
 *          this.setDummy(getDummy());
 *      }
 *      if(instance.getBarMap() != null) {
 *          this.addBarMap(instance.getBarMap());
 *      }
 *      if(instance.getBarList() != null) {
 *          this.addBarList(instance.getBarList());
 *      }
 *      if(instance.getBars() != null) {
 *          this.addBars(instance.getBars());
 *      }
 *      return this;
 * }
 * </pre></code>
 * </p>
 *
 * @author Adrian Witas
 */
public class BuilderMergeHandler implements TypeHandler {

    @Override
    public void handle(JavaTypeBuilder owner, Context context) {
        if (owner.containsMethod("merge")) {
            return;
        }
        buildSourceMerge(owner);
        buildBuilderMerge(owner);
    }

    protected void buildSourceMerge(JavaTypeBuilder owner) {
        Type interfaceOrOwnerType = JavaTypeUtil.getOwnerInterfaceOrType(owner.getSourceType());
        JavaType sourceType = owner.getSourceType();
        JavaMethodBuilder methodBuilder = new JavaMethodBuilder();
        methodBuilder.addModifier(JavaModifier.PUBLIC);
        methodBuilder.setName("merge");
        methodBuilder.addParameter("instance", interfaceOrOwnerType);
        Collection<JavaMethod> sourceMethods =  sourceType.getMethods();
        Class sourceClass =  ReflectUtil.getRawClass(interfaceOrOwnerType);
        if(Object.class.equals(sourceClass)) {
            sourceMethods = new ClassTypeProvider(sourceClass).get().getMethods();
        }
        Multimap<String, JavaMethod> sourceIndexedMethods = Multimaps.index(sourceMethods, new MethodNameKeyFunction());
        for (JavaField javaField : sourceType.getFields()) {
            generateMergeSourceFieldCode(owner, javaField, methodBuilder, sourceIndexedMethods);
        }
        methodBuilder.setResultType(new TypeNameWrapper(owner.getName()));
        methodBuilder.addBodyLines("return this;");
        owner.addMethod(methodBuilder.build());
    }



    protected void buildBuilderMerge(JavaTypeBuilder owner) {
        JavaType sourceType = owner.getSourceType();
        JavaMethodBuilder methodBuilder = new JavaMethodBuilder();
        methodBuilder.addModifier(JavaModifier.PUBLIC);
        methodBuilder.setResultType(new TypeNameWrapper(owner.getName()));
        methodBuilder.setName("merge");
        methodBuilder.addParameter("instance", new TypeNameWrapper(owner.getName()));
        Multimap<String, JavaMethod> sourceIndexedMethods = Multimaps.index(owner.getMethods(), new MethodNameKeyFunction());

        for (JavaField javaField : sourceType.getFields()) {
            generateMergeSourceFieldCode(owner, javaField, methodBuilder, sourceIndexedMethods);
        }
        methodBuilder.addBodyLines("return this;");
        owner.addMethod(methodBuilder.build());
    }


    protected void generateMergeSourceFieldCode(JavaTypeBuilder owner, JavaField javaField, JavaMethodBuilder methodBuilder, Multimap<String, JavaMethod> sourceIndexedMethods) {
        String fieldName = javaField.getName();
        Type fieldType = javaField.getType();
        Class rawType = ReflectUtil.getRawClass(fieldType);
        String setterMethodName = CodeGeneratorUtil.getSetterMethodName(javaField.getName());
        String getterMethodName = CodeGeneratorUtil.getGetterMethodName(javaField);
        boolean isPrimitive = rawType.isPrimitive();
        String isPresentMethodName = CodeGeneratorUtil.getPresentMethodName(fieldName);
        boolean isPresentMethodExists = sourceIndexedMethods.containsKey(isPresentMethodName);
        if (isPresentMethodExists) {
            methodBuilder.addBodyLines(String.format("if(instance.%s()){", isPresentMethodName));
        }

        if (isPrimitive) {
            methodBuilder.addBodyLines(String.format("this.%s(instance.%s());", setterMethodName, getterMethodName));
            if (isPresentMethodExists) {
                methodBuilder.addBodyLines("}");
            }
            return;
        }

        if (!isPresentMethodExists) {
            methodBuilder.addBodyLines(String.format("if(instance.%s() != null) {", getterMethodName));
        }
        if (Collection.class.isAssignableFrom(rawType)
                || Map.class.isAssignableFrom(Map.class)
                || rawType.isArray()) {
            String addMethodName = CodeGeneratorUtil.getAddMethodName(fieldName);
            if (owner.containsMethod(addMethodName)) {
                methodBuilder.addBodyLines(String.format("    this.%s(instance.%s());", addMethodName, getterMethodName));
                methodBuilder.addBodyLines("}");
                return;
            }
        }
        methodBuilder.addBodyLines(String.format("    this.%s(instance.%s());", setterMethodName, getterMethodName));
        methodBuilder.addBodyLines("}");
    }


}