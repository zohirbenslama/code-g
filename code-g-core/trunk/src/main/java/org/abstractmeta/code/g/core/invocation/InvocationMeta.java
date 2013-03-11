package org.abstractmeta.code.g.core.invocation;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Represents InvocationMeta
 *
 * <p>Represent one of the following calls</p>
 *
 * <ul>
 *   <li><strong>Method call</strong>  i.e. <pre>[package.]aMethod(argument0, ..., argumentX) </pre></li>
 *  <li><strong>Constructor call</strong> i.e. <pre>[package.]AConstructor&lt;GenericType0, GenericTypeX&gt;(argument0, ..., argumentX)</pre></li>
 * </ul>
 *
 * @author Adrian Witas
 */
public class InvocationMeta {

    private List<String> parameters;
    private List<String> rawParameters;
    private Type type;
    private List<Type> parameterizedTypeArguments;
    private String methodName;
    private String constructorName;
    private String ownerType;
    private InvocationType invocationType;

    public List<String> getParameters() {
        return parameters;
    }

    public void setParameters(List<String> parameters) {
        this.parameters = parameters;
    }

    public List<String> getRawParameters() {
        return rawParameters;
    }

    public void setRawParameters(List<String> rawParameters) {
        this.rawParameters = rawParameters;
    }

    public java.lang.reflect.Type getType() {
        return type;
    }

    public void setType(java.lang.reflect.Type type) {
        this.type = type;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getConstructorName() {
        return constructorName;
    }

    public void setConstructorName(String constructorName) {
        this.constructorName = constructorName;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public InvocationType getInvocationType() {
        return invocationType;
    }

    public void setInvocationType(InvocationType invocationType) {
        this.invocationType = invocationType;
    }

    public List<Type> getParameterizedTypeArguments() {
        return parameterizedTypeArguments;
    }

    public void setParameterizedTypeArguments(List<Type> parameterizedTypeArguments) {
        this.parameterizedTypeArguments = parameterizedTypeArguments;
    }

    public static enum InvocationType {
        CONSTRUCTOR, METHOD, TYPE
    }

}

