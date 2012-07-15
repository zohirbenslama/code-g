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
package org.abstractmeta.code.g.core.renderer;

import org.abstractmeta.code.g.code.JavaMethod;
import org.abstractmeta.code.g.code.JavaTypeImporter;
import org.abstractmeta.code.g.core.util.StringUtil;
import org.abstractmeta.code.g.renderer.JavaMethodRenderer;
import org.abstractmeta.code.g.renderer.JavaTypeRenderer;
import com.google.common.base.Joiner;


public class MethodRenderer extends AbstractRenderer<JavaMethod> implements JavaMethodRenderer {

    public static final String TEMPLATE = String.format("${%s}${%s}${%s}${%s} ${%s}(${%s}) ${%s}{\n" +
        "${%s}\n}\n\n",
        DOCUMENTATION_PARAMETER,
        ANNOTATIONS_PARAMETER,
        MODIFIER_PARAMETER,
        TYPE_PARAMETER,
        NAME_PARAMETER,
        ARGUMENTS_PARAMETER,
        EXCEPTION_PARAMETER,
        BODY_PARAMETER
    );

    private final JavaTypeRenderer javaTypeRenderer;

    
    public MethodRenderer(JavaTypeRenderer javaTypeRenderer) {
        super(TEMPLATE, 4);
        this.javaTypeRenderer = javaTypeRenderer;
    }
    



    @Override
    void setParameters(JavaMethod instance, JavaTypeImporter importer, Template template, int indentSize) {
        if(instance.getResultType() == null) {
            throw new IllegalStateException("result type was null for method " + instance.getName() + instance);
        }
        template.set(DOCUMENTATION_PARAMETER, getDocumentation(instance.getDocumentation()));
        template.set(ANNOTATIONS_PARAMETER, getAnnotations(importer, instance.getAnnotations()));
        template.set(MODIFIER_PARAMETER, getModifiers(instance.getModifiers()));
        template.set(TYPE_PARAMETER, importer.getSimpleTypeName(instance.getResultType()));
        template.set(NAME_PARAMETER, instance.getName());
        template.set(ARGUMENTS_PARAMETER, getMethodArguments(importer, instance.getParameterModifiers(), instance.getParameterTypes(), instance.getParameterNames()));
        template.set(EXCEPTION_PARAMETER, getMethodExceptions(importer, instance.getExceptionTypes()));
        String javaInlineTypes = getJavaTypes(javaTypeRenderer, importer, instance.getNestedJavaTypes());
        String body = Joiner.on("\n").join(instance.getBody());
        template.set(BODY_PARAMETER, StringUtil.indent(String.format("%s%s", javaInlineTypes, body), indentSize + 4));
    }

   


}
