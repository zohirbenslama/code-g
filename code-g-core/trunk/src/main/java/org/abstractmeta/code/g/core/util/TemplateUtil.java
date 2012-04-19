package org.abstractmeta.code.g.core.util;


import java.util.List;

/**
 * Represents  TemplateUtil.
 *
 * @author awitas
 * @version 0.01 31/01/2012
 */

public class TemplateUtil {


    public static String tokenizeExpressions(String fragment, char expressionStartChar, char expressionBlockStartChar, char expressionBlockEndChar, String token,  List<String> expressions) {
        if (fragment.indexOf(expressionStartChar) == -1) {
            return fragment;
        }
        char[] chars = fragment.toCharArray();
        StringBuilder resultBuilder = new StringBuilder();
        StringBuilder expressionBuilder = new StringBuilder();
        boolean expressionParsing = false;
        boolean expressionBlock = false;
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (c == '\\' && i + 1 < chars.length && chars[i + 1] == expressionStartChar) {
                continue;
            }

            if (!expressionParsing && ((i == 0 || (i > 0 && chars[i - 1] != '\\')) && c == expressionStartChar)) {
                expressionParsing = true;

            } else if (expressionParsing) {
                if (c == expressionBlockStartChar) {
                    expressionBlock = true;
                    continue;
                }  if ((expressionBlock && c != expressionBlockEndChar) ||  (! expressionBlock && ! (c == ' ' ||  c == '\t' || c == '\n' || c == '\r'))) {
                    expressionBuilder.append(c);

                } else {
                    resultBuilder.append(token);
                    expressions.add(expressionBuilder.toString());
                    if (! expressionBlock || c != expressionBlockEndChar) {
                        resultBuilder.append(c);
                    }
                    expressionBuilder = new StringBuilder();
                    expressionBlock = expressionParsing = false;
                }


            } else {
                resultBuilder.append(c);
            }

            if (i == chars.length - 1 && expressionBuilder.length() > 0) {
                resultBuilder.append(token);
                expressions.add(expressionBuilder.toString());
            }
        }
        return resultBuilder.toString();
    }

}
