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

import org.abstractmeta.code.g.code.JavaType;
import org.abstractmeta.code.g.code.JavaTypeImporter;
import org.abstractmeta.code.g.core.util.StringUtil;
import org.abstractmeta.code.g.core.util.TemplateUtil;
import org.abstractmeta.code.g.renderer.JavaTypeRenderer;
import com.google.common.base.Joiner;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public abstract class AbstractRenderer<T> {


    public static final String DOCUMENTATION_PARAMETER = "documentation";
    public static final String MODIFIER_PARAMETER = "modifier";
    public static final String TYPE_PARAMETER = "type";
    public static final String NAME_PARAMETER = "name";
    public static final String ANNOTATIONS_PARAMETER = "annotation";
    public static final String ARGUMENTS_PARAMETER = "arguments";
    public static final String BODY_PARAMETER = "body";


    private final String template;
    private final int templateIndent;
    private List<String> argumentNames;


    protected AbstractRenderer(String template, int templateIndent) {
        this.templateIndent = templateIndent;
        this.argumentNames = new ArrayList<String>();
        this.template = TemplateUtil.tokenizeExpressions(template.replace("%s", "%%s"), '$', '{', '}', "%s", argumentNames);
    }


    public String render(T instance, JavaTypeImporter importer, int indentSize) {
        Template template = get();
        setParameters(instance, importer, template, indentSize);
        return template.build(indentSize + templateIndent);
    }

        protected String getModifiers(List<String> modifiers) {
         String result = Joiner.on(" ").join(modifiers);
        if(result.length() > 0) {
            return result + " ";
        }
        return result;
    }

    protected String getJavaTypes(JavaTypeRenderer javaTypeRenderer, JavaTypeImporter importer, List<JavaType> javaTypes) {
        StringBuilder result = new StringBuilder();
        for(JavaType javaType: javaTypes) {
            result.append(javaTypeRenderer.render(javaType, importer, 4));
        }
        return result.toString();
    }
    

    
    abstract void setParameters(T instance, JavaTypeImporter importer, Template template, int indentSize);


    protected String getAnnotations(JavaTypeImporter importer, List<Annotation> annotations) {
        StringBuilder result = new StringBuilder();
        for(Annotation annotation: annotations) {
            if(result.length() > 0) {
                result.append("\n");
            }
            result.append(importer.getAnnotation(annotation));
        }
        if(result.length() > 0) {
            result.append("\n");
        }
        return result.toString();
    }


    protected String getMethodArguments(JavaTypeImporter importer, List<String> argumentModifiers, List<Type> argumentTypes, List<String> argumentNames) {
        StringBuilder result = new StringBuilder();
        if (argumentNames.size() == 0) {
            for (int i = 0; i < argumentTypes.size(); i++) {
                addMethodArgument(result, importer, argumentModifiers.get(i), argumentTypes.get(i), String.format("argument%s",i));
            }

        } else {
            for (int i = 0; i < argumentTypes.size(); i++) {
                addMethodArgument(result, importer, argumentModifiers.get(i), argumentTypes.get(i), argumentNames.get(i));
            }
        }
        return result.toString();
    }


    protected String getTypes(JavaTypeImporter importer, List<Type> argumentTypes) {
        StringBuilder result = new StringBuilder();
        for(Type type: argumentTypes) {
            if(result.length() > 0) {
                result.append(", ");
            }
            result.append(importer.getSimpleTypeName(type));
        }
        return result.toString();
    }


    protected void addMethodArgument(StringBuilder result, JavaTypeImporter importer, String modifier, Type type, String name) {
        if (result.length() > 0) {
            result.append(", ");
        }
        if(! modifier.isEmpty()) {
            modifier = modifier + " ";
        }
        result.append(String.format("%s%s %s", modifier,  importer.getSimpleTypeName(type), name));
    }

    protected String getDocumentation(List<String> documentation) {
        if (documentation.size() == 0) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        for (String line : documentation) {
            result.append(String.format(" * %s", line));
        }
        return String.format("/**\n%s\n */\n", result.toString());
    }

    protected String getValue(String text) {
        if (text == null) {
            return "";
        }
        return text;
    }

    protected String getValue(String text, String defaultValue) {
        if (text == null || text.isEmpty()) {
            return defaultValue;
        }
        return text;
    }

    
    protected String mergeFragment(String prefix, String fragment, String postfix) {
        if(fragment == null || fragment.isEmpty()) {
            return "";
        }
        return String.format("%s%s%s", prefix, fragment, postfix);
    }
    
    public class Template {

        private Map<String, Object> values = new HashMap<String, Object>();

        public Template set(String name, Object value) {
            values.put(name, value);
            return this;
        }


        public String build(int indentSize) {
            Object[] templateValues = new Object[argumentNames.size()];
            for (int i = 0; i < argumentNames.size(); i++) {
                String argumentName = argumentNames.get(i);
                templateValues[i] = values.containsKey(argumentName) ? values.get(argumentName) : "";
            }
            return StringUtil.indent(format(templateValues), indentSize);
        }

        protected String format(Object... arguments) {
            return String.format(template, arguments);
        }
    }

    public Template get() {
        return new Template();
    }
}
