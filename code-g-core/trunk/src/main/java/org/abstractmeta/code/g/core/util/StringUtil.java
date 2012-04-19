package org.abstractmeta.code.g.core.util;

import com.google.common.base.CaseFormat;

/**
 * @author Adrian Witas
 */
public class StringUtil {


    public static String getSingular(String pluralFragment) {
        if (pluralFragment.endsWith("ies")) {
            return String.format("%sy", pluralFragment.substring(0, pluralFragment.length() - 3));
        } else if (pluralFragment.endsWith("s")) {
            return pluralFragment.substring(0, pluralFragment.length() - 1);
        }
        return pluralFragment;
    }

    public static String getPlural(String sigularFragment) {
        if (sigularFragment.endsWith("y")) {
            return sigularFragment + "ies";
        }
        return sigularFragment + "s";
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


    public static String substringAfterLastIndexOf(String fragment, String token) {
        int lastTokenPosition = fragment.lastIndexOf(token);
        if (lastTokenPosition != -1) {
            return fragment.substring(lastTokenPosition + 1);
        }
        return fragment;
    }

    public static String substringBeforeLastIndexOf(String fragment, String token) {
        int lastTokenPosition = fragment.lastIndexOf(token);
        if (lastTokenPosition != -1) {
            return fragment.substring(0, lastTokenPosition);
        }
        return fragment;

    }

      public static String join(Iterable<String> items, String itemPrefix, String itemSeparator, boolean appendSeparatorAfterLastItem) {
        StringBuilder result = new StringBuilder();
        for(String item: items) {
            if(result.length() > 0) {
                result.append(itemSeparator);
            }
            result.append(itemPrefix);
            result.append(item);
        }
        if(result.length() > 0 && appendSeparatorAfterLastItem) {
            result.append(itemSeparator);
        }
        return result.toString();
    }

}
