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

/**
 * This handle is experimental, it checks for a collection type, and matches methods like add, get to
 * generate implementation code. Please see also {@link org.abstractmeta.code.g.core.handler.field.RegistryFieldHandler}
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
public class CollectionFieldHandler  {

//    private final JavaTypeBuilderImpl ownerTypeBuilder;
//    private final Descriptor descriptor;
//
//
//    public CollectionFieldHandler(JavaTypeBuilderImpl ownerTypeBuilder, Descriptor descriptor) {
//        this.ownerTypeBuilder = ownerTypeBuilder;
//        this.descriptor = descriptor;
//    }
//
//
//    @Override
//    public void handle(JavaType sourceType, JavaField javaField) {
//        String fieldName = javaField.getName();
//        if (!Collection.class.isAssignableFrom(ReflectUtil.getRawClass(javaField.getType()))) {
//            return;
//        }
//        String getMethodName = StringUtil.format(CaseFormat.LOWER_CAMEL, "get", fieldName, CaseFormat.LOWER_CAMEL);
//        if (ownerTypeBuilder.containsMethod(getMethodName)) {
//            return;
//        }
//        buildGetMethod(getMethodName, javaField);
//        buildAddMethod(fieldName, sourceType, javaField);
//        String singularFieldName = StringUtil.getSingular(fieldName);
//        if (!singularFieldName.equals(fieldName)) {
//            buildAddMethod(singularFieldName, sourceType, javaField);
//            buildGetMethod(singularFieldName, sourceType, javaField);
//
//        }
//    }
//
//    private void buildGetMethod(String name, JavaType sourceType, JavaField javaField) {
//        String methodName = StringUtil.format(CaseFormat.LOWER_CAMEL, "get", name, CaseFormat.LOWER_CAMEL);
//
//        if (ownerTypeBuilder.containsMethod(methodName)) {
//            return;
//        }
//
//        Iterable<JavaMethod> filteredMethods = Iterables.filter(sourceType.getMethods(), new MethodNamePredicate(methodName));
//        for (JavaMethod method : filteredMethods) {
//            if (method.getParameters().size() == 0 || method.getParameters().size() > 1) {
//                continue;
//            }
//            Type resultType = method.getResultType();
//            Class rawResultType = ReflectUtil.getRawClass(resultType);
//            Type argumentType = method.getParameters().get(0).getType();
//            JavaMethod accessor = JavaTypeUtil.matchFirstFieldByType(resultType, argumentType);
//            if (accessor == null) {
//                return;
//            }
//            String accessorName = accessor.getName();
//            JavaMethodBuilder methodBuilder = new JavaMethodBuilder();
//            methodBuilder.setName(method.getName());
//            methodBuilder.setResultType(resultType);
//            methodBuilder.addModifier("public");
//            String accessorFiledName = accessor.getName().replace("is", "").replace("get", "");
//            String argumentName = StringUtil.format(CaseFormat.LOWER_CAMEL, accessorFiledName, "", CaseFormat.LOWER_CAMEL);
//            Class rawArgumentType = ReflectUtil.getRawClass(argumentType);
//            methodBuilder.addParameter(argumentName, argumentType);
//            methodBuilder.addBodyLines(String.format("for(%s item: this.%s) { ", rawResultType.getSimpleName(), javaField.getName()));
//            if (rawArgumentType.isPrimitive()) {
//                methodBuilder.addBodyLines(String.format("    if(item.%s() == %s)  { ", accessorName, argumentName));
//            } else {
//                methodBuilder.addBodyLines(String.format("    if(item.%s().equals(%s))  { ", accessorName, argumentName));
//            }
//            methodBuilder.addBodyLines("        return item;");
//            methodBuilder.addBodyLines("    }");
//            methodBuilder.addBodyLines("}");
//            methodBuilder.addBodyLines("return null;");
//            ownerTypeBuilder.addMethod(methodBuilder.build());
//
//        }
//    }
//
//    protected void buildAddMethod(String name, JavaType sourceType, JavaField javaField) {
//        String methodName = StringUtil.format(CaseFormat.LOWER_CAMEL, "add", name, CaseFormat.LOWER_CAMEL);
//        String fieldName = javaField.getName();
//        Iterable<JavaMethod> filteredMethods = Iterables.filter(sourceType.getMethods(), new MethodNamePredicate(methodName));
//
//        for (JavaMethod method :filteredMethods) {
//            JavaMethodBuilder methodBuilder = new JavaMethodBuilder();
//            methodBuilder.setName(method.getName());
//            methodBuilder.setResultType(method.getResultType());
//            methodBuilder.addModifier("public");
//            Type argumentType = method.getParameters().get(0).getType();
//            Class rawArgumentType = ReflectUtil.getRawClass(argumentType);
//            if (Collection.class.isAssignableFrom(rawArgumentType)) {
//                ParameterizedType actualParameterizedType = ParameterizedType.class.cast(argumentType);
//                Type collectionParametrizedType = new ParameterizedTypeImpl(null, Collection.class, actualParameterizedType.getActualTypeArguments());
//                methodBuilder.addParameter(fieldName, collectionParametrizedType);
//                methodBuilder.addBodyLines(String.format("this.%s.addAll(%s);", fieldName, fieldName));
//            } else {
//                String singularName = StringUtil.getSingular(fieldName);
//                methodBuilder.addParameter(singularName, argumentType);
//                methodBuilder.addBodyLines(String.format("this.%s.add(%s);", fieldName, singularName));
//            }
//            ownerTypeBuilder.addMethod(methodBuilder.build());
//        }
//    }
//
//    protected void buildGetMethod(String methodName, JavaField javaField) {
//        JavaMethodBuilder methodBuilder = new JavaMethodBuilder();
//        methodBuilder.setName(methodName);
//        methodBuilder.setResultType(javaField.getType());
//        methodBuilder.addModifier("public");
//        methodBuilder.addBodyLines(String.format("return this.%s;", javaField.getName()));
//        ownerTypeBuilder.addMethod(methodBuilder.build());
//    }
//
//    public Descriptor getDescriptor() {
//        return descriptor;
//    }
}
