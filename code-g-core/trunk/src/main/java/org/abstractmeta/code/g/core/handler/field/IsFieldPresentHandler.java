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
 * This handle creates is[field name]Present method for any field prefixed with '_'.
 * This method only returns true if original
 * field was mutated.
 * <p>
 * <ul>For 'foo' field the following method is generated.
 * <li> isXXXPresent: <pre>
 * public boolean isFooPresent() {
 *    return this._foo;
 * }
 * </pre></li>
 * </p>
 *
 * @author Adrian Witas
 */
public class IsFieldPresentHandler  {

//    private final JavaTypeBuilderImpl ownerTypeBuilder;
//    private final boolean generatePresentCheck;
//
//    public IsFieldPresentHandler(JavaTypeBuilderImpl ownerTypeBuilder, boolean generatePresentCheck) {
//        this.ownerTypeBuilder = ownerTypeBuilder;
//        this.generatePresentCheck = generatePresentCheck;
//    }
//
//
//    @Override
//    public void handle(JavaType sourceType, JavaField javaField) {
//        if (!(javaField.getName().endsWith("Present")  && javaField.getType().equals(boolean.class))) {
//            return;
//        }
//        if (!generatePresentCheck) {
//            return;
//        }
//        String fieldName = javaField.getName();
//        String methodPrefix = "is";
//        String methodName = StringUtil.format(CaseFormat.LOWER_CAMEL, methodPrefix, fieldName + "Present", CaseFormat.LOWER_CAMEL);
//        if (! ownerTypeBuilder.containsMethod(methodName)) {
//            JavaMethodBuilder methodBuilder = new JavaMethodBuilder();
//            methodBuilder.setName(methodName);
//            methodBuilder.setResultType(boolean.class);
//            methodBuilder.addModifier("public");
//            methodBuilder.addBodyLines(String.format("return this.%s;", fieldName));
//            ownerTypeBuilder.addMethod(methodBuilder.build());
//
//        }
//    }
}
