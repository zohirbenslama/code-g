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

import org.abstractmeta.code.g.code.JavaField;
import org.abstractmeta.code.g.code.JavaType;
import org.abstractmeta.code.g.config.Descriptor;
import org.abstractmeta.code.g.core.builder.SimpleClassBuilder;
import org.abstractmeta.code.g.core.code.builder.JavaMethodBuilder;
import org.abstractmeta.code.g.core.code.builder.JavaTypeBuilder;
import org.abstractmeta.code.g.core.util.DescriptorUtil;
import org.abstractmeta.code.g.core.util.StringUtil;
import org.abstractmeta.code.g.handler.JavaPluginHandler;
import org.abstractmeta.code.g.handler.JavaFieldHandler;
import com.google.common.base.CaseFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * This handler creates set method.
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
 *
 * <h2>Advanced setting</h2>
 * <ul>
 *     <li>onBeforeFieldAssignmentHandler - class name which implements JavaPluginHandler</li>
 * </ul>
 *
 *
 * @author Adrian Witas
 */
public class SetterFieldHandler implements JavaFieldHandler {

    private final JavaTypeBuilder ownerTypeBuilder;
    private final Descriptor descriptor;

    public SetterFieldHandler(JavaTypeBuilder ownerTypeBuilder, Descriptor descriptor) {
        this.ownerTypeBuilder = ownerTypeBuilder;
        this.descriptor = descriptor;
    }


    @Override
    public void handle(JavaType sourceType, JavaField javaField) {
        if (!javaField.isImmutable()) {
            String fieldName = javaField.getName();
            String methodName = StringUtil.format(CaseFormat.LOWER_CAMEL, "set", fieldName, CaseFormat.LOWER_CAMEL);
            if (!ownerTypeBuilder.containsMethod(methodName)) {
                JavaMethodBuilder methodBuilder = new JavaMethodBuilder();
                methodBuilder.setName(methodName);
                methodBuilder.setResultType(void.class);
                methodBuilder.addParameter("final", fieldName, javaField.getType());
                methodBuilder.addModifier("public");
                onAssignment(descriptor, methodBuilder, sourceType, javaField);
                methodBuilder.addBody(generateBody(fieldName, fieldName));
                ownerTypeBuilder.addMethod(methodBuilder.build());
            }
        }
    }

    protected void onAssignment(Descriptor descriptor, JavaMethodBuilder methodBuilder, JavaType sourceType, JavaField javaField) {
        JavaPluginHandler javaPluginHandler = DescriptorUtil.loadInstance(descriptor, JavaPluginHandler.class, SimpleClassBuilder.CHANGE_HANDLER);
        List<JavaType> javaTypes = new ArrayList<JavaType>();
        List<String> javaBody = new ArrayList<String>();
        if(javaPluginHandler != null) {
            javaPluginHandler.handle(descriptor, ownerTypeBuilder, javaField, methodBuilder);
        }
        methodBuilder.addNestedJavaTypes(javaTypes);
        if(! javaBody.isEmpty()) {
            methodBuilder.addBody(javaBody);
        }
   }

    protected Collection<String> generateBody(String thisFieldName, String argumentFieldName) {
        return Arrays.asList(String.format("this.%s = %s;", thisFieldName, argumentFieldName));
    }

    public Descriptor getDescriptor() {
        return descriptor;
    }
}
