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
import org.abstractmeta.code.g.code.JavaMethod;
import org.abstractmeta.code.g.code.JavaModifier;
import org.abstractmeta.code.g.code.JavaTypeBuilder;
import org.abstractmeta.code.g.code.handler.FieldHandler;
import org.abstractmeta.code.g.core.code.builder.JavaMethodBuilder;
import org.abstractmeta.code.g.core.util.CodeGeneratorUtil;
import org.abstractmeta.code.g.generator.Context;

/**
 * This handle creates get method for any field.
 * <p>
 * Take the following <code>field: Collection&lt;String> foos;</code> as example.
 * In this case the following method is added to the owner type.
 * <ul>
 * <li> getter: <pre>
 * public Collection&lt;String> getFoos() {
 *    return this.foos;
 * }</pre></li>
 * <p/>
 * </p>
 *
 * @author Adrian Witas
 */
@SuppressWarnings("unchecked")
public class GetterFieldHandler implements FieldHandler {

    @Override
    public void handle(JavaTypeBuilder owner, JavaField target, Context context) {
        JavaMethod supperGetterMethod = CodeGeneratorUtil.getSupperGetterMethodName(owner, target);
        String methodName = supperGetterMethod == null ? CodeGeneratorUtil.getGetterMethodName(target) : supperGetterMethod.getName();
        if (owner.containsMethod(methodName)) {
            return;
        }

        JavaMethod getterMethod = buildGetterMethod(target, methodName, supperGetterMethod);
        owner.addMethod(getterMethod);
    }

    private JavaMethod buildGetterMethod(JavaField target, String methodName, JavaMethod supperGetterMethod) {
        JavaMethodBuilder methodBuilder = new JavaMethodBuilder();
        methodBuilder.setName(methodName);
        methodBuilder.setResultType(supperGetterMethod == null ? target.getType() : supperGetterMethod.getResultType());
        methodBuilder.addModifier(JavaModifier.PUBLIC);
        methodBuilder.addBodyLines(String.format("return this.%s;", target.getName()));
        return methodBuilder.build();
    }
}
