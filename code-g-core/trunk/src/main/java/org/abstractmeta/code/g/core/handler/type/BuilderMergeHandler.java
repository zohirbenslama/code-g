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

import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import org.abstractmeta.code.g.code.JavaField;
import org.abstractmeta.code.g.code.JavaMethod;
import org.abstractmeta.code.g.code.JavaType;
import org.abstractmeta.code.g.core.code.builder.JavaMethodBuilder;
import org.abstractmeta.code.g.core.code.builder.JavaTypeBuilder;
import org.abstractmeta.code.g.core.collection.function.MethodNameKeyFunction;
import org.abstractmeta.code.g.core.internal.TypeNameWrapper;
import org.abstractmeta.code.g.core.util.ReflectUtil;
import org.abstractmeta.code.g.core.util.StringUtil;
import org.abstractmeta.code.g.handler.JavaTypeHandler;
import com.google.common.base.CaseFormat;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

/**
 * This handler creates merge method.
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
public class BuilderMergeHandler implements JavaTypeHandler {


    private final JavaTypeBuilder ownerTypeBuilder;

    public BuilderMergeHandler(JavaTypeBuilder ownerTypeBuilder) {
        this.ownerTypeBuilder = ownerTypeBuilder;
    }


    @Override
    public void handle(JavaType sourceType) {
        JavaMethod buildMethod = ownerTypeBuilder.getMethod("build");
        if (buildMethod == null || ownerTypeBuilder.containsMethod("merge")) {
            return;
        }

        Multimap<String, JavaMethod> indexMethods = Multimaps.index(sourceType.getMethods(), new MethodNameKeyFunction());

        JavaMethodBuilder methodBuilder = new JavaMethodBuilder();
        methodBuilder.addModifier("public");
        methodBuilder.setName("merge");
        methodBuilder.setResultType(new TypeNameWrapper(ownerTypeBuilder.getName()));
        methodBuilder.addParameter("instance", buildMethod.getResultType());



        for (JavaField javaField : sourceType.getFields()) {
            String fieldName = javaField.getName();
            Type fieldType = javaField.getType();
            Class rawType = ReflectUtil.getRawClass(fieldType);
            String setterMethodName = StringUtil.format(CaseFormat.LOWER_CAMEL, "set", fieldName, CaseFormat.LOWER_CAMEL);
            String getterMethodPrefix = boolean.class.equals(javaField.getType()) || Boolean.class.equals(javaField.getType()) ? "is" : "get";
            String getterMethodName = StringUtil.format(CaseFormat.LOWER_CAMEL, getterMethodPrefix, fieldName, CaseFormat.LOWER_CAMEL);
            boolean isPrimitive = rawType.isPrimitive();
            if (isPrimitive) {
                methodBuilder.addBody(String.format("this.%s(instance.%s());", setterMethodName, getterMethodName));
                continue;
            }

            methodBuilder.addBody(String.format("if(instance.%s() != null) {", getterMethodName));
            if (Collection.class.isAssignableFrom(rawType)
                    || Map.class.isAssignableFrom(Map.class)
                    || rawType.isArray()) {
                String addMethodName = StringUtil.format(CaseFormat.LOWER_CAMEL, "add", fieldName, CaseFormat.LOWER_CAMEL);
                if (ownerTypeBuilder.containsMethod(addMethodName)) {
                    methodBuilder.addBody(String.format("    this.%s(instance.%s());", addMethodName, getterMethodName));
                    methodBuilder.addBody("}");
                    continue;
                }
            }

            methodBuilder.addBody(String.format("    this.%s(instance.%s());", setterMethodName, getterMethodName));
            methodBuilder.addBody("}");
        }
        methodBuilder.addBody("return this;");
        ownerTypeBuilder.addMethod(methodBuilder.build());

    }
}