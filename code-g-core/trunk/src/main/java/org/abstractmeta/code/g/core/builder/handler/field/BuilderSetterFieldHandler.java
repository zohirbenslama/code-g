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

import org.abstractmeta.code.g.code.*;
import org.abstractmeta.code.g.code.handler.FieldHandler;
import org.abstractmeta.code.g.core.code.builder.JavaMethodBuilder;
import org.abstractmeta.code.g.core.util.CodeGeneratorUtil;
import org.abstractmeta.code.g.generator.Context;

import java.lang.reflect.Type;

/**
 * This handle creates set method for any java field.
 * <p>
 * Take the following <code>field: Collection&lt;String> foos;</code> as example.
 * For this case the following method will be added to the owner builder type.
 * <ul>
 * <li> setter: <pre>
 * public &lt;T> setFoos(Collection&lt;String> foos) {
 *    this.foos = foos;
 *    return this;
 * }</pre></li>
 * <p/>
 * Where &lt;T>  is the field owner type.
 * </p>
 *
 * @author Adrian Witas
 */
public class BuilderSetterFieldHandler implements FieldHandler {


    @Override
    public void handle(JavaTypeBuilder owner, JavaField target, Context context) {
        String methodName = CodeGeneratorUtil.getSetterMethodName(target.getName());
        if(owner.containsMethod(methodName)) {
            return ;
        }
        addSetterMethod(owner, target.getName(), target.getType());
    }


    protected void addSetterMethod(JavaTypeBuilder owner, String fieldName, Type fieldType) {
        if(fieldName.endsWith("Present"))  {
            String baseField = fieldName.replace("Present","");
            if(owner.containsField(baseField)) return;
        }
        JavaMethodBuilder methodBuilder = new JavaMethodBuilder();
        String methodName = CodeGeneratorUtil.getSetterMethodName(fieldName);
        methodBuilder.setName(methodName);
        methodBuilder.addParameter(fieldName, fieldType);
        methodBuilder.addModifier(JavaModifier.PUBLIC);
        methodBuilder.addBodyLines(String.format("this.%s = %s;", fieldName, fieldName));
        BuilderUtil.addIsPresentFlag(owner, fieldName, methodBuilder);
        BuilderUtil.addSetterResultType(owner, methodName, methodBuilder);
        owner.addMethod(methodBuilder.build());
    }



}
