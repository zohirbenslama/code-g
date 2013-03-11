package org.abstractmeta.code.g.core.code;

import org.abstractmeta.code.g.code.JavaModifier;
import org.abstractmeta.code.g.code.JavaParameter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Represents JavaParameterImpl
 * @author Adrian Witas
 */
public class JavaParameterImpl implements JavaParameter {


    private final String name;
    private final Type type;
    private final List<Annotation> annotations;
    private final List<JavaModifier> modifiers;
    private final boolean varTypeArgument;

    public JavaParameterImpl(String name, Type type, List<Annotation> annotations, List<JavaModifier> modifiers, boolean varTypeArgument) {
        this.name = name;
        this.type = type;
        this.annotations = annotations;
        this.modifiers = modifiers;
        this.varTypeArgument = varTypeArgument;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public List<Annotation> getAnnotations() {
        return annotations;
    }

    public List<JavaModifier> getModifiers() {
        return modifiers;
    }

    public boolean isVarTypeArgument() {
        return varTypeArgument;
    }
}
