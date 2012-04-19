package org.abstractmeta.code.g.core.internal;


import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

public class TypeVariableImpl implements TypeVariable {

    private final String name;

    public TypeVariableImpl(String name) {
        this.name = name;
    }

    @Override
    public Type[] getBounds() {
        return new Type[0];
    }

    @Override
    public GenericDeclaration getGenericDeclaration() {
        return null;
    }

    @Override
    public String getName() {
        return name;
    }
}
