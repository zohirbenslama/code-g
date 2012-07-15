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
package org.abstractmeta.code.g.core.handler;

import org.abstractmeta.code.g.code.JavaField;
import org.abstractmeta.code.g.code.JavaMethod;
import org.abstractmeta.code.g.code.JavaType;
import org.abstractmeta.code.g.core.code.builder.JavaMethodBuilder;
import org.abstractmeta.code.g.core.code.builder.JavaTypeBuilder;
import org.abstractmeta.code.g.core.collection.predicates.MethodNamePredicate;
import org.abstractmeta.code.g.core.util.JavaTypeUtil;
import org.abstractmeta.code.g.core.util.ReflectUtil;
import org.abstractmeta.code.g.core.util.StringUtil;
import org.abstractmeta.code.g.handler.JavaFieldHandler;
import com.google.common.base.CaseFormat;
import com.google.common.collect.Iterables;

import java.lang.reflect.Type;
import java.util.Collection;

/**
 * This handler is experimental, it checks for a collection type, and matches methods like add, get to
 * generate implementation code. Please see also {@link RegistryFieldHandler}
 * <p/>
 * <p>For instance for the given source type
 * <code><pre>
 * public interface Bar {
 *    ....
 *    List<Bar> getBars();
 *    void addBar(Bar bar);
 *    Bar getBar(String id);
 * }
 *  </code></pre>
 * The following code is generated
 * <code><pre>
 * public List<Bar> getBars() {
 *     return this.bars;
 * }
 * public void addBar(Bar bar) {
 *     this.bars.add(bar);
 * }
 * public Bar getBar(String name) {
 *     for(Bar item: this.bars) {
 *          if(item.getName().equals(name))  {
 *              return item;
 *          }
 *      }
 *      return null;
 * }
 * </pre></code>
 * </p>
 *
 * @author Adrian Witas
 */
public class CollectionFieldHandler implements JavaFieldHandler {

    private final JavaTypeBuilder ownerTypeBuilder;

    public CollectionFieldHandler(JavaTypeBuilder ownerTypeBuilder) {
        this.ownerTypeBuilder = ownerTypeBuilder;
    }


    @Override
    public void handle(JavaType sourceType, JavaField javaField) {
        String fieldName = javaField.getName();
        if (!Collection.class.isAssignableFrom(ReflectUtil.getRawClass(javaField.getType()))) {
            return;
        }
        String getMethodName = StringUtil.format(CaseFormat.LOWER_CAMEL, "get", fieldName, CaseFormat.LOWER_CAMEL);
        if (ownerTypeBuilder.containsMethod(getMethodName)) {
            return;
        }
        buildGetMethod(getMethodName, javaField);
        buildAddMethod(fieldName, sourceType, javaField);
        String singularFieldName = StringUtil.getSingular(fieldName);
        if (!singularFieldName.equals(fieldName)) {
            buildAddMethod(singularFieldName, sourceType, javaField);
            buildGetMethod(singularFieldName, sourceType, javaField);

        }
    }

    private void buildGetMethod(String name, JavaType sourceType, JavaField javaField) {
        String methodName = StringUtil.format(CaseFormat.LOWER_CAMEL, "get", name, CaseFormat.LOWER_CAMEL);

        if (ownerTypeBuilder.containsMethod(methodName)) {
            return;
        }

        Iterable<JavaMethod> filteredMethods = Iterables.filter(sourceType.getMethods(), new MethodNamePredicate(methodName));
        for (JavaMethod method : filteredMethods) {
            if (method.getParameterNames().size() == 0 || method.getParameterNames().size() > 1) {
                continue;
            }
            Type resultType = method.getResultType();
            Class rawResultType = ReflectUtil.getRawClass(resultType);
            Type argumentType = method.getParameterTypes().get(0);
            JavaMethod accessor = JavaTypeUtil.matchFirstFieldByType(resultType, argumentType);
            if (accessor == null) {
                return;
            }
            String accessorName = accessor.getName();
            JavaMethodBuilder methodBuilder = new JavaMethodBuilder();
            methodBuilder.setName(method.getName());
            methodBuilder.setResultType(resultType);
            methodBuilder.addModifier("public");
            String accessorFiledName = accessor.getName().replace("is", "").replace("get", "");
            String argumentName = StringUtil.format(CaseFormat.LOWER_CAMEL, accessorFiledName, "", CaseFormat.LOWER_CAMEL);
            Class rawArgumentType = ReflectUtil.getRawClass(argumentType);
            methodBuilder.addParameter(argumentName, argumentType);
            methodBuilder.addBody(String.format("for(%s item: this.%s) { ", rawResultType.getSimpleName(), javaField.getName()));
            if (rawArgumentType.isPrimitive()) {
                methodBuilder.addBody(String.format("    if(item.%s() == %s)  { ", accessorName, argumentName));
            } else {
                methodBuilder.addBody(String.format("    if(item.%s().equals(%s))  { ", accessorName, argumentName));
            }
            methodBuilder.addBody("        return item;");
            methodBuilder.addBody("    }");
            methodBuilder.addBody("}");
            methodBuilder.addBody("return null;");
            ownerTypeBuilder.addMethod(methodBuilder.build());

        }
    }

    protected void buildAddMethod(String name, JavaType sourceType, JavaField javaField) {
        String methodName = StringUtil.format(CaseFormat.LOWER_CAMEL, "add", name, CaseFormat.LOWER_CAMEL);
        String fieldName = javaField.getName();
        Iterable<JavaMethod> filteredMethods = Iterables.filter(sourceType.getMethods(), new MethodNamePredicate(methodName));

        for (JavaMethod method :filteredMethods) {
            JavaMethodBuilder methodBuilder = new JavaMethodBuilder();
            methodBuilder.setName(method.getName());
            methodBuilder.setResultType(method.getResultType());
            methodBuilder.addModifier("public");
            Type argumentType = method.getParameterTypes().get(0);
            Class rawArgumentType = ReflectUtil.getRawClass(argumentType);
            if (Collection.class.isAssignableFrom(rawArgumentType)) {
                methodBuilder.addParameter(fieldName, argumentType);
                methodBuilder.addBody(String.format("this.%s.addAll(%s);", fieldName, fieldName));
            } else {
                String singularName = StringUtil.getSingular(fieldName);
                methodBuilder.addParameter(singularName, argumentType);
                methodBuilder.addBody(String.format("this.%s.add(%s);", fieldName, singularName));
            }
            ownerTypeBuilder.addMethod(methodBuilder.build());
        }
    }

    protected void buildGetMethod(String methodName, JavaField javaField) {
        JavaMethodBuilder methodBuilder = new JavaMethodBuilder();
        methodBuilder.setName(methodName);
        methodBuilder.setResultType(javaField.getType());
        methodBuilder.addModifier("public");
        methodBuilder.addBody(String.format("return this.%s;", javaField.getName()));
        ownerTypeBuilder.addMethod(methodBuilder.build());
    }


}
