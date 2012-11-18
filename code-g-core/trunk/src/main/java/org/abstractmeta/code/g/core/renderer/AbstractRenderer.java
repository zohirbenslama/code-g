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
import org.abstractmeta.code.g.core.util.TemplateUtil;
import org.abstractmeta.code.g.renderer.JavaTypeRenderer;
import com.google.common.base.Joiner;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public abstract class AbstractRenderer<T> {


    public static final String DOCUMENTATION_PARAMETER = "documentation";
    public static final String MODIFIER_PARAMETER = "modifier";
    public static final String TYPE_PARAMETER = "type";
    public static final String NAME_PARAMETER = "name";
    public static final String ANNOTATIONS_PARAMETER = "annotation";
    public static final String ARGUMENTS_PARAMETER = "arguments";
    public static final String EXCEPTION_PARAMETER = "exceptions";
    public static final String BODY_PARAMETER = "plugin";


    private final String formattedTemplate;
    private final int templateIndent;
    private List<String> templateArgumentNames;


    protected AbstractRenderer(String formattedTemplate, int templateIndent) {
        this.templateIndent = templateIndent;
        this.templateArgumentNames = new ArrayList<String>();
        this.formattedTemplate = TemplateUtil.tokenizeExpressions(formattedTemplate.replace("%s", "%%s"), '$', '{', '}', "%s", templateArgumentNames);
    }


    public String render(T instance, JavaTypeImporter importer, int indentSize) {
        SimpleTemplate template = get();
        setParameters(instance, importer, template, indentSize);
        return template.build(indentSize + templateIndent);
    }

    protected String getModifiers(List<String> modifiers) {
        String result = Joiner.on(" ").join(modifiers);
        if (result.length() > 0) {
            return result + " ";
        }
        return result;
    }


    protected String getJavaTypes(JavaTypeRenderer javaTypeRenderer, JavaTypeImporter importer, List<JavaType> javaTypes) {
        return getJavaTypes(javaTypeRenderer, importer, javaTypes, 4);
    }

    protected String getJavaTypes(JavaTypeRenderer javaTypeRenderer, JavaTypeImporter importer, List<JavaType> javaTypes, int indent) {
        StringBuilder result = new StringBuilder();
        for (JavaType javaType : javaTypes) {
            result.append(javaTypeRenderer.render(javaType, importer, indent));
        }
        return result.toString();
    }


    abstract void setParameters(T instance, JavaTypeImporter importer, SimpleTemplate template, int indentSize);


    protected String getAnnotations(JavaTypeImporter importer, List<Annotation> annotations) {
        StringBuilder result = new StringBuilder();
        for (Annotation annotation : annotations) {
            if (result.length() > 0) {
                result.append("\n");
            }
            result.append(importer.getAnnotation(annotation));
        }
        if (result.length() > 0) {
            result.append("\n");
        }
        return result.toString();
    }


    protected String getMethodArguments(JavaTypeImporter importer, List<String> argumentModifiers, List<Type> argumentTypes, List<String> argumentNames) {
        StringBuilder result = new StringBuilder();
        if (argumentNames.size() == 0) {
            for (int i = 0; i < argumentTypes.size(); i++) {
                addMethodArgument(result, importer, argumentModifiers.get(i), argumentTypes.get(i), String.format("argument%s", i));
            }

        } else {
            for (int i = 0; i < argumentTypes.size(); i++) {
                String modifiers = i < argumentModifiers.size() ? argumentModifiers.get(i) : "";
                addMethodArgument(result, importer, modifiers, argumentTypes.get(i), argumentNames.get(i));
            }
        }
        return result.toString();
    }

    protected String getMethodExceptions(JavaTypeImporter importer, List<Type> exceptionTypes) {
        StringBuilder result = new StringBuilder();
        for (Type exceptionType : exceptionTypes) {
            if (result.length() > 0) result.append(", ");
            result.append(importer.getSimpleTypeName(exceptionType));
        }
        if (result.length() > 0) {
            return "throws " + result.toString();
        }
        return "";
    }


    protected String getTypes(JavaTypeImporter importer, List<Type> argumentTypes) {
        StringBuilder result = new StringBuilder();
        for (Type type : argumentTypes) {
            if (result.length() > 0) {
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
        if (!modifier.isEmpty()) {
            modifier = modifier + " ";
        }
        result.append(String.format("%s%s %s", modifier, importer.getSimpleTypeName(type), name));
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
        if (fragment == null || fragment.isEmpty()) {
            return "";
        }
        return String.format("%s%s%s", prefix, fragment, postfix);
    }

    public String getTemplate() {
        return formattedTemplate;
    }


    public SimpleTemplate get() {
        return new SimpleTemplate(templateArgumentNames, formattedTemplate);
    }
}
