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

import org.abstractmeta.code.g.core.util.CodeGeneratorUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a simple template.
 *
 * @author Adrian Witas
 *
 */
public class SimpleTemplate {

    private Map<String, Object> values = new HashMap<String, Object>();
    private final List<String> argumentNames;
    private final String template;

    public SimpleTemplate(List<String> argumentNames, String template) {
        this.argumentNames = argumentNames;
        this.template = template;
    }

    public SimpleTemplate set(String name, Object value) {
        values.put(name, value);
        return this;
    }


    public String build(int indentSize) {
        Object[] templateValues = new Object[argumentNames.size()];
        for (int i = 0; i < argumentNames.size(); i++) {
            String argumentName = argumentNames.get(i);
            templateValues[i] = values.containsKey(argumentName) ? values.get(argumentName) : "";
        }
        return CodeGeneratorUtil.indent(format(templateValues), indentSize);
    }

    protected String format(Object... arguments) {
        return String.format(template, arguments);
    }
}

        
