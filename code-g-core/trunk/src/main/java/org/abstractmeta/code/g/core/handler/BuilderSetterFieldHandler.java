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

import com.google.common.base.CaseFormat;
import org.abstractmeta.code.g.code.JavaField;
import org.abstractmeta.code.g.code.JavaType;
import org.abstractmeta.code.g.core.code.builder.JavaMethodBuilder;
import org.abstractmeta.code.g.core.code.builder.JavaTypeBuilder;
import org.abstractmeta.code.g.core.internal.TypeNameWrapper;
import org.abstractmeta.code.g.core.util.StringUtil;
import org.abstractmeta.code.g.handler.JavaFieldHandler;

import java.lang.reflect.Type;

/**
 * This handler creates set method for any java field.
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
public class BuilderSetterFieldHandler implements JavaFieldHandler {

    private final JavaTypeBuilder ownerTypeBuilder;
    private final boolean generatePresentCheck;

    public BuilderSetterFieldHandler(JavaTypeBuilder ownerTypeBuilder, boolean generatePresentCheck) {
        this.ownerTypeBuilder = ownerTypeBuilder;
        this.generatePresentCheck = generatePresentCheck;
    }

    @Override
    public void handle(JavaType sourceType, JavaField javaField) {
        if(javaField.getName().startsWith("_")) return;;
        addSetterMethod(ownerTypeBuilder, javaField.getName(), javaField.getType());
    }

    protected void addSetterMethod(JavaTypeBuilder typeBuilder, String fieldName, Type fieldType) {
        JavaMethodBuilder methodBuilder = new JavaMethodBuilder();
        String methodName = StringUtil.format(CaseFormat.LOWER_CAMEL, "set", fieldName, CaseFormat.LOWER_CAMEL);
        if (! ownerTypeBuilder.containsMethod(methodName)) {
            methodBuilder.setName(methodName);
            methodBuilder.setResultType(new TypeNameWrapper(typeBuilder.getName()));
            methodBuilder.addParameter(fieldName, fieldType);
            methodBuilder.addModifier("public");
            methodBuilder.addBody(String.format("this.%s = %s;", fieldName, fieldName));
            if(generatePresentCheck) {
                methodBuilder.addBody(String.format("this.%s = true;", StringUtil.isPresentFieldName(fieldName)));
            }
            methodBuilder.addBody("return this;");
            typeBuilder.addMethod(methodBuilder.build());
        }
    }
}
