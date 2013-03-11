package org.abstractmeta.code.g.core.type;

import java.util.ArrayList;
import java.util.List;

/**
 * GenericTypeLiteral parser.
 * This abstraction extract a generic type literals.
 *
 *
 * @author Adrian Witas
 */
public class GenericTypeLiteralParser {


    public static GenericTypeLiteral parse(String fragment) {
        List<GenericTypeLiteral> genericLiterals = new ArrayList<GenericTypeLiteral>();
        parse(fragment, genericLiterals);
        if (genericLiterals.isEmpty()) {
            return null;
        } else if (genericLiterals.size() == 1) {
            return genericLiterals.get(0);
        } else {
            throw new IllegalStateException("Invalid generic literal:" + fragment);
        }

    }

    protected static void parse(String fragment, List<GenericTypeLiteral> arguments) {
        int genericStartPosition = fragment.indexOf('<');
        if (genericStartPosition != -1) {
            StringBuilder argumentType = new StringBuilder();
            List<StringBuilder> argumentParameterTypes = new ArrayList<StringBuilder>();
            argumentParameterTypes.add(new StringBuilder());
            int genericDepth = 0;
            for (char c : fragment.toCharArray()) {
                if (c == ' ' || c == '\t') continue;
                if (c == '<') {
                    if (genericDepth++ == 0) continue;
                } else if (c == '>') {
                    genericDepth--;
                    if (genericDepth == 0) {
                        arguments.add(buildGenericTypeLiteral(argumentType.toString(), argumentParameterTypes));
                    }
                } else if (c == ',') {
                    if (genericDepth == 1) {
                        argumentParameterTypes.add(new StringBuilder());
                        continue;
                    }
                }
                if (genericDepth == 0) {
                    argumentType.append(c);
                } else {
                    argumentParameterTypes.get(argumentParameterTypes.size() - 1).append(c);
                }
            }
            if(genericDepth != 0) {
                throw new IllegalStateException("Invalid generic literal:" + fragment);
            }
        }

    }

    protected static GenericTypeLiteral buildGenericTypeLiteral(String type, List<StringBuilder> argumentParameterTypes) {
        GenericTypeLiteral result = new GenericTypeLiteral(type, new ArrayList<GenericTypeLiteral>());
        for (StringBuilder builder : argumentParameterTypes) {
            String argumentParameter = builder.toString();
            if (argumentParameter.contains("<")) {
                parse(argumentParameter, result.getArgumentTypes());
            } else {
                result.getArgumentTypes().add(new GenericTypeLiteral(argumentParameter, new ArrayList<GenericTypeLiteral>()));
            }
        }
        return result;
    }

}
