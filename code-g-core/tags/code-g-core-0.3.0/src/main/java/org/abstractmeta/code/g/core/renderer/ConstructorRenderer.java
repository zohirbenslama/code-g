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

import org.abstractmeta.code.g.code.JavaConstructor;
import org.abstractmeta.code.g.code.JavaTypeImporter;
import org.abstractmeta.code.g.core.util.StringUtil;
import org.abstractmeta.code.g.renderer.JavaConstructorRenderer;
import com.google.common.base.Joiner;


public class ConstructorRenderer extends AbstractRenderer<JavaConstructor> implements JavaConstructorRenderer {

    public static final String TEMPLATE = String.format("${%s}${%s}${%s}(${%s}) {\n" +
        "${%s}\n}\n",
        DOCUMENTATION_PARAMETER,
        MODIFIER_PARAMETER,
        NAME_PARAMETER,
        ARGUMENTS_PARAMETER,
        BODY_PARAMETER
    );

    public ConstructorRenderer() {
        super(TEMPLATE, 4);
    }

    @Override
    void setParameters(JavaConstructor instance, JavaTypeImporter importer, Template template, int indentSize) {

        template.set(DOCUMENTATION_PARAMETER, getDocumentation(instance.getDocumentation()));
        template.set(MODIFIER_PARAMETER, getModifiers(instance.getModifiers()));
        template.set(NAME_PARAMETER, instance.getName());
        template.set(ARGUMENTS_PARAMETER, getMethodArguments(importer, instance.getParameterModifiers(), instance.getParameterTypes(), instance.getParameterNames()));
        template.set(BODY_PARAMETER, StringUtil.indent(Joiner.on("\n").join(instance.getBody()), indentSize + 4));
    }

}
