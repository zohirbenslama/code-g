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
 * This handle creates a proxy to a get method.
 *
 * @author Adrian Witas
 */
public class  SupperGetterFieldHandler {

//    private final JavaTypeBuilderImpl ownerTypeBuilder;
//    private final Descriptor descriptor;
//
//    public SupperGetterFieldHandler(JavaTypeBuilderImpl ownerTypeBuilder, Descriptor descriptor) {
//        this.ownerTypeBuilder = ownerTypeBuilder;
//        this.descriptor = descriptor;
//    }
//
//
//    @Override
//    public void handle(JavaType sourceType, JavaField javaField) {
//        if (!javaField.isImmutable()) {
//            String fieldName = javaField.getName();
//            String methodName = StringUtil.format(CaseFormat.LOWER_CAMEL, "get", fieldName, CaseFormat.LOWER_CAMEL);
//            if (!ownerTypeBuilder.containsMethod(methodName)) {
//                JavaMethodBuilder methodBuilder = new JavaMethodBuilder();
//                methodBuilder.setName(methodName);
//                methodBuilder.setResultType(javaField.getType());
//                methodBuilder.addModifier("public");
//                onAccessing(descriptor, methodBuilder, sourceType, javaField);
//                methodBuilder.addBodyLines(generateBody(methodName));
//                ownerTypeBuilder.addMethod(methodBuilder.build());
//            }
//        }
//    }
//
//    protected void onAccessing(Descriptor descriptor, JavaMethodBuilder methodBuilder, JavaType sourceType, JavaField javaField) {
//        JavaPluginHandler javaPluginHandler = DescriptorUtil.newInstance(descriptor, JavaPluginHandler.class, SimpleClassBuilderImpl.CHANGE_HANDLER);
//        List<JavaType> javaTypes = new ArrayList<JavaType>();
//        List<String> javaBody = new ArrayList<String>();
//        if(javaPluginHandler != null) {
//            javaPluginHandler.handle(descriptor, ownerTypeBuilder, javaField, methodBuilder);
//        }
//        methodBuilder.addNestedJavaTypes(javaTypes);
//        if(! javaBody.isEmpty()) {
//            methodBuilder.addBodyLines(javaBody);
//        }
//   }
//
//    protected Collection<String> generateBody(String methodName) {
//        return Arrays.asList(String.format("return super.%s();", methodName));
//    }
//
//    public Descriptor getDescriptor() {
//        return descriptor;
//    }
}
