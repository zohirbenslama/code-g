package org.abstractmeta.code.g.core.type;

import java.util.List;

/**
* Represents a generic type literal.
*
* @author Adrian Witas
*/
public class GenericTypeLiteral {


    private final String typeName;
    private final List<GenericTypeLiteral> argumentTypes;

    public GenericTypeLiteral(String typeName, List<GenericTypeLiteral> argumentTypes) {
        this.typeName = typeName;
        this.argumentTypes = argumentTypes;
    }

    public String getTypeName() {
        return typeName;
    }

    public List<GenericTypeLiteral> getArgumentTypes() {
        return argumentTypes;
    }


    @Override
    public String toString() {
        return "GenericTypeLiteral{" +
                "typeName='" + typeName + '\'' +
                ", argumentTypes=" + argumentTypes +
                '}';
    }
}
