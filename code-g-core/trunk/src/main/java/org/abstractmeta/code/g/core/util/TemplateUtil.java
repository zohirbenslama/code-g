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


import org.abstractmeta.code.g.core.renderer.SimpleTemplate;

import java.util.ArrayList;
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


    public static SimpleTemplate compile(String template) {
        List<String> argumentNames = new ArrayList<String>();
        String formattedTemplate = TemplateUtil.tokenizeExpressions(template.replace("%s", "%%s"), '$', '{', '}', "%s", argumentNames);
        return new SimpleTemplate(argumentNames, formattedTemplate);
    }

}
