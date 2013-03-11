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
package org.abstractmeta.code.g.core.code.handler.field;

import org.abstractmeta.code.g.code.JavaField;
import org.abstractmeta.code.g.code.JavaMethod;
import org.abstractmeta.code.g.code.JavaModifier;
import org.abstractmeta.code.g.code.JavaTypeBuilder;
import org.abstractmeta.code.g.code.handler.FieldHandler;
import org.abstractmeta.code.g.core.code.builder.JavaMethodBuilder;
import org.abstractmeta.code.g.core.util.StringUtil;
import org.abstractmeta.code.g.generator.Context;

/**
 * This handle creates set method.
 * for any given java field.
 * <p>
 * Take the following <code>field: Collection&lt;String> foos;</code> as example.
 * For this case the following method will be added to the owner type.
 * <ul>
 * <li> setter: <pre>
 * public void setFoos(Collection&lt;String> foos) {
 *    this.foos = foos;
 * }</pre></li>
 * <p/>
 * </p>
 * </ul>
 * <p/>
 * <h2>Advanced setting</h2>
 * <ul>
 * <li>onBeforeFieldAssignmentHandler - class name which implements JavaPluginHandler</li>
 * </ul>
 *
 * @author Adrian Witas
 */
public class SetterFieldHandler implements FieldHandler {

    @Override
    public void handle(JavaTypeBuilder owner, JavaField target, Context context) {
        if (target.isImmutable()) return;
        String methodName = StringUtil.getSetterName(target.getName());
        if (owner.containsMethod(methodName)) return;
        JavaMethod setterMethod = buildSetterMethod(target, methodName);
        owner.addMethod(setterMethod);
    }

    private JavaMethod buildSetterMethod(JavaField target, String methodName) {
        JavaMethodBuilder methodBuilder = new JavaMethodBuilder();
        methodBuilder.setName(methodName);
        methodBuilder.setResultType(void.class);
        methodBuilder.addParameter(target.getName(), target.getType());
        methodBuilder.addModifier(JavaModifier.PUBLIC);
        methodBuilder.addBodyLines(String.format("this.%s = %s;", target.getName(), target.getName()));
        return methodBuilder.build();
    }

}
