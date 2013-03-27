package org.abstractmeta.code.g.core.invocation;

import com.google.common.collect.Maps;
import org.abstractmeta.code.g.core.internal.ParameterizedTypeImpl;
import org.abstractmeta.code.g.core.internal.TypeNameWrapper;
import org.abstractmeta.code.g.core.invocation.function.InvocationParameterName;
import org.abstractmeta.code.g.core.type.GenericTypeLiteral;
import org.abstractmeta.code.g.core.type.GenericTypeLiteralParser;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Invocation parser.
 * This abstraction parses a given literal to build invocation meta object.
 * List of invocation parameters is used to substitute placeholders with a given parameter.
 * <ul>
 *     <p>The following invocation calls are supported</p>
 *  <li><strong>Method call</strong>  i.e. <pre>[package.aClass]aMethod(argument0, ..., argumentX) </pre></li>
 *  <li><strong>Constructor call</strong> i.e. <pre>[package.]AConstructor&lt;GenericType0, GenericTypeX&gt;(argument0, ..., argumentX)</pre></li>
 * </ul>
 *
 * @author Adrian Witas
 */
public class InvocationParser {


    public InvocationMeta parse(String fragment, Collection<InvocationParameter> parameters) {
        Map<String, InvocationParameter> namedParameters = Maps.uniqueIndex(parameters, new InvocationParameterName());
        return parse(fragment, namedParameters);
    }


    public  InvocationMeta parse(String fragment,  Map<String, InvocationParameter> namedParameters) {
        InvocationMeta result = new InvocationMeta();
        result.setParameters(extractParameters(fragment.trim(), namedParameters));
        result.setRawParameters(extractParameters(fragment.trim(), Collections.<String, InvocationParameter>emptyMap()));
        Type parameterizedType = extractParameterizedType(fragment, namedParameters);
        if(parameterizedType instanceof ParameterizedType) {
            result.setParameterizedTypeArguments(Arrays.asList(ParameterizedType.class.cast(parameterizedType).getActualTypeArguments()));
        }
        result.setType(parameterizedType);
        extractInvoker(fragment, result);
        return result;
    }


    protected  void extractInvoker(String fragment, InvocationMeta result) {
        int genericStartPosition = fragment.indexOf('<');
        if(genericStartPosition != -1) {
            String firstPart =  fragment.substring(0, genericStartPosition);
            int genericEndPosition = fragment.lastIndexOf('>');
            fragment = firstPart + fragment.substring(genericEndPosition + 1, fragment.length());
        }
        int methodBracketStartPosition = fragment.indexOf('(');

        if (methodBracketStartPosition != -1) {
            String invocation = fragment.substring(0, methodBracketStartPosition);
            int lastDotPosition = invocation.lastIndexOf('.');
            if (lastDotPosition != -1) {
                String invocationTarget = invocation.substring(lastDotPosition + 1, invocation.length());
                if (invocationTarget.charAt(0) >= 'A' && invocationTarget.charAt(0) <= 'Z') {
                    result.setInvocationType(InvocationMeta.InvocationType.CONSTRUCTOR);
                    result.setConstructorName(invocationTarget);
                    result.setOwnerType(invocation);

                } else {
                    result.setInvocationType(InvocationMeta.InvocationType.METHOD);
                    result.setMethodName(invocation);
                }

            } else {
                result.setInvocationType(InvocationMeta.InvocationType.METHOD);
                result.setMethodName(invocation);
            }
        } else {
            result.setInvocationType(InvocationMeta.InvocationType.TYPE);
            result.setOwnerType(fragment);
        }
    }


    public  List<String> extractParameters(String fragment, Map<String, InvocationParameter> namedParameters) {
        List<String> result = new ArrayList<String>();
        int parameterCallStartBracketPosition = fragment.indexOf('(');
        if (parameterCallStartBracketPosition != -1) {
            String parametersFragment = fragment.substring(parameterCallStartBracketPosition + 1, fragment.length() - 1);
            if (!parametersFragment.isEmpty()) {
                for (String parameterName : parametersFragment.split(",")) {
                    if (namedParameters.containsKey(parameterName.trim())) {
                        InvocationParameter parameter = namedParameters.get(parameterName.trim());  
                        result.add(parameter.getValue());
                    } else {
                        result.add(parameterName.trim());
                    }
                }
            }
        }
        return result;
    }


    protected  Type extractParameterizedType(String fragment, Map<String, InvocationParameter> namedParameters) {
        GenericTypeLiteral genericTypeLiteral = GenericTypeLiteralParser.parse(fragment);
        if(genericTypeLiteral == null) {
            int argumentsBracketPosition = fragment.indexOf('(');
            if(argumentsBracketPosition != -1) {
                new TypeNameWrapper(fragment.substring(0, argumentsBracketPosition));
            }
            return new TypeNameWrapper(fragment);
        }
        return convert(genericTypeLiteral, namedParameters);
    }


    public  Type convert(GenericTypeLiteral genericLiteral, Map<String, InvocationParameter> namedParameters) {
        Type rawType = getType(genericLiteral.getTypeName(), namedParameters);
        if (genericLiteral.getArgumentTypes().isEmpty()) {
            return rawType;
        }
        Type[] genericArguments = new Type[genericLiteral.getArgumentTypes().size()];
        int i = 0;
        for (GenericTypeLiteral genericArgumentLiteral : genericLiteral.getArgumentTypes()) {
            genericArguments[i++] = convert(genericArgumentLiteral, namedParameters);
        }
        return new ParameterizedTypeImpl(null, rawType, genericArguments);
    }


    public  Type getType(String className, Map<String, InvocationParameter> namedParameters) {
        if (namedParameters.containsKey(className)) {
            return namedParameters.get(className).getType();
        }
        try {
            return Class.forName(className);
        } catch (Exception e) {
            return new TypeNameWrapper(className);
        }
    }


}
