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
package org.abstractmeta.code.g.core.util;

import com.google.common.base.CaseFormat;
import com.google.common.base.Strings;
import org.abstractmeta.code.g.code.*;
import org.abstractmeta.code.g.config.Descriptor;
import org.abstractmeta.code.g.config.NamingConvention;
import org.abstractmeta.code.g.core.code.builder.SourcedJavaTypeBuilder;
import org.abstractmeta.code.g.core.provider.ClassTypeProvider;
import org.abstractmeta.code.g.generator.Context;
import org.abstractmeta.code.g.renderer.JavaTypeRenderer;

import javax.inject.Provider;
import java.lang.reflect.Type;

/**
 * @author Adrian Witas
 */
public class CodeGeneratorUtil {

    public static String getSetterMethodName(String fieldName) {
        return CodeGeneratorUtil.format(CaseFormat.LOWER_CAMEL, "set", fieldName, CaseFormat.LOWER_CAMEL);
    }


    public static JavaMethod getSupperGetterMethodName(JavaType owner, JavaField field) {
        if (owner.getSuperInterfaces() != null && !owner.getSuperInterfaces().isEmpty()) {
            for (Type iFace : owner.getSuperInterfaces()) {
                Class clazz = ReflectUtil.getRawClass(iFace);
                JavaMethod result = getSupperGetterMethodName(new ClassTypeProvider(clazz).get(), field);
                if (result != null) return result;
            }
        }
        return getGetterMethodName(owner, field.getName(), field.getType());
    }


    public static JavaMethod getGetterMethodName(JavaType owner, String fieldName, Type fieldType) {
        String methodName = getGetterMethodName(fieldName, fieldType);
        JavaMethod result = JavaTypeUtil.getMethod(owner.getMethods(), methodName);
        if (result == null) {
            Class rawFieldType = ReflectUtil.getRawClass(fieldType);
            if (boolean.class.equals(rawFieldType)) {
                methodName = getGetterMethodName(fieldName, Boolean.class);
            } else if(Boolean.class.equals(rawFieldType)) {
                methodName = getGetterMethodName(fieldName, boolean.class);
            }
            result = JavaTypeUtil.getMethod(owner.getMethods(), methodName);
        }
        return result;
    }


    public static String getGetterMethodName(JavaField field) {
        return getGetterMethodName(field.getName(), field.getType());
    }

    public static String getGetterMethodName(String fieldName, Type fieldType) {
        String prefix = boolean.class.equals(fieldType) ? "is" : "get";
        return CodeGeneratorUtil.format(CaseFormat.LOWER_CAMEL, prefix, fieldName, CaseFormat.LOWER_CAMEL);
    }


    public static String getPresentFieldName(String fieldName) {
        return CodeGeneratorUtil.format(CaseFormat.LOWER_CAMEL, fieldName, "Present", CaseFormat.LOWER_CAMEL);
    }


    public static String getPresentMethodName(String fieldName) {
        return CodeGeneratorUtil.format(CaseFormat.LOWER_CAMEL, "is", fieldName + "Present", CaseFormat.LOWER_CAMEL);
    }


    public static String getAddMethodName(String fieldName) {
        return CodeGeneratorUtil.format(CaseFormat.LOWER_CAMEL, "add", fieldName, CaseFormat.LOWER_CAMEL);
    }

    public static String getClearMethodName(String fieldName) {
        return CodeGeneratorUtil.format(CaseFormat.LOWER_CAMEL, "clear", fieldName, CaseFormat.LOWER_CAMEL);
    }


    public static String getClassName(String fragment, String postfix) {
        return format(CaseFormat.UPPER_CAMEL, fragment, postfix, CaseFormat.LOWER_CAMEL);
    }

    public static String format(CaseFormat resultCaseFormat, String prefix, String fragment, CaseFormat sourceCaseFormat) {
        String upperUnderscoreFragment = sourceCaseFormat.to(CaseFormat.UPPER_UNDERSCORE, fragment);
        String upperUnderscorePrefix = sourceCaseFormat.to(CaseFormat.UPPER_UNDERSCORE, prefix);
        String upperUnderscoreResult = upperUnderscorePrefix + "_" + upperUnderscoreFragment;
        return CaseFormat.UPPER_UNDERSCORE.to(resultCaseFormat, upperUnderscoreResult);
    }


    public static String indent(String textFragment, int indent) {
        StringBuilder indentationBuilder = new StringBuilder();
        for (int i = 0; i < indent; i++) {
            indentationBuilder.append(" ");
        }
        String indentation = indentationBuilder.toString();
        String[] lines = textFragment.split("\n");
        StringBuilder resultBuilder = new StringBuilder();
        for (String line : lines) {
            if (resultBuilder.length() > 0) {
                resultBuilder.append("\n");
            }
            resultBuilder.append(indentation);
            resultBuilder.append(line);
        }

        for (int i = textFragment.length() - 1; i >= 0; i--) {
            if (textFragment.charAt(i) == '\n') {
                resultBuilder.append("\n");
            } else {
                break;
            }
        }

        return resultBuilder.toString();
    }


    public static String join(Iterable<String> items, String itemPrefix, String itemSeparator, boolean appendSeparatorAfterLastItem) {
        StringBuilder result = new StringBuilder();
        for (String item : items) {
            if (result.length() > 0) {
                result.append(itemSeparator);
            }
            result.append(itemPrefix);
            result.append(item);
        }
        if (result.length() > 0 && appendSeparatorAfterLastItem) {
            result.append(itemSeparator);
        }
        return result.toString();
    }

    public static boolean isNotEmpty(String fragment) {
        return fragment != null && !fragment.isEmpty();

    }


    public static String getPlural(String singular) {
        if (singular.endsWith("y")) {
            return singular.substring(0, singular.length() - 1) + "ies";
        }
        return singular + "s";
    }

    public static String getSingular(String plural) {
        if (plural.endsWith("ies")) {
            return plural.substring(0, plural.length() - 3);
        } else if (plural.endsWith("s")) {
            return plural.substring(0, plural.length() - 1);
        }
        return plural;
    }


    /**
     * Return value, or default value if value is null.
     *
     * @param value
     * @param defaultValue
     * @return
     */
    public static String getValue(String value, String defaultValue) {
        if (value == null) return defaultValue;
        return value;
    }

    /**
     * Renders code for a given java type builder
     *
     * @param javaTypeBuilder
     * @param rendererProvider
     * @return
     */
    public static SourcedJavaType renderCode(JavaTypeBuilder javaTypeBuilder, Provider<JavaTypeRenderer> rendererProvider) {
        JavaTypeRenderer renderer = rendererProvider.get();
        JavaTypeImporter importer = javaTypeBuilder.getImporter();
        JavaType javaType = javaTypeBuilder.build();
        importer.getGenericTypeVariables().putAll(javaType.getGenericTypeVariables());
        importer.addTypes(javaTypeBuilder.getImportTypes());
        String sourceCode = renderer.render(javaType, importer, 0);
        SourcedJavaTypeBuilder sourcedJavaTypeBuilder = new SourcedJavaTypeBuilder();
        sourcedJavaTypeBuilder.setType(javaType);
        sourcedJavaTypeBuilder.setSourceCode(sourceCode);
        return sourcedJavaTypeBuilder.build();
    }


    /**
     * Formats target class name for a given NamingConvention
     * NamingConvention is read first from context->descriptor->NamingConvention or from the getNamingConvention method
     *
     * @param context               context
     * @param sourcePackageName     source package name
     * @param sourceSimpleClassName source simple classname
     * @return target class name
     */
    public static String formatTargetClassName(Context context, String sourcePackageName, String sourceSimpleClassName, NamingConvention namingConvention) {
        Descriptor descriptor = context.get(Descriptor.class);
        if (descriptor.getNamingConvention() != null) {
            namingConvention = descriptor.getNamingConvention();
        }
        StringBuilder result = new StringBuilder(sourcePackageName).append(".");
        if (!Strings.isNullOrEmpty(namingConvention.getPackagePostfix())) {
            result.append(namingConvention.getPackagePostfix()).append(".");
        }
        if (!Strings.isNullOrEmpty(namingConvention.getClassPrefix())) {
            result.append(namingConvention.getClassPrefix());
        }
        result.append(sourceSimpleClassName);
        if (!Strings.isNullOrEmpty(namingConvention.getClassPostfix())) {
            result.append(namingConvention.getClassPostfix());
        }
        return result.toString();
    }


}
