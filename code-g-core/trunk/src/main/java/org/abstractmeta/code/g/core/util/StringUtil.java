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

import java.util.Map;

/**
 * @author Adrian Witas
 */
public class StringUtil {

    public static String getSetterName(String fieldName) {
        return StringUtil.format(CaseFormat.LOWER_CAMEL, "set", fieldName, CaseFormat.LOWER_CAMEL);
    }

    public static String getGetterName(String fieldName) {
        return StringUtil.format(CaseFormat.LOWER_CAMEL, "get", fieldName, CaseFormat.LOWER_CAMEL);
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



    public static String getPlural(String sigularFragment) {
        if (sigularFragment.endsWith("y")) {
            return sigularFragment + "ies";
        }
        return sigularFragment + "s";
    }


}
