package org.abstractmeta.code.g.core.internal;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents TypeNameWrapper
 *
 * @author Adrian Witas
 */
public class TypeNameWrapper implements Type {
    private final String typeName;
    private final List<Type> genericArgumentTypes;

    public TypeNameWrapper(String typeName, Type... genericArgumentTypes) {
        this.genericArgumentTypes = new ArrayList<Type>();
        Collections.addAll(this.genericArgumentTypes, genericArgumentTypes);
        this.typeName = typeName;
    }

    public TypeNameWrapper(String typeName, List<Type>  genericArgumentTypes) {
        this.genericArgumentTypes = new ArrayList<Type>();
        this.genericArgumentTypes.addAll(genericArgumentTypes);
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }

    public List<Type> getGenericArgumentTypes() {
        return genericArgumentTypes;
    }

    @Override
    public String toString() {
        return "TypeNameWrapper{" +
                "typeName='" + typeName + '\'' +
                '}';
    }
}
