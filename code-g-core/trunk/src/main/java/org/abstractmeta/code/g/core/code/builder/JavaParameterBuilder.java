package org.abstractmeta.code.g.core.code.builder;

import org.abstractmeta.code.g.code.JavaModifier;
import org.abstractmeta.code.g.code.JavaParameter;
import org.abstractmeta.code.g.core.code.JavaParameterImpl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents JavaParameterBuilder
 * <p>
 * </p>
 *
 * @author Adrian Witas
 */
public class JavaParameterBuilder implements JavaParameter {

    private String name;
    private Type type;
    private List<Annotation> annotations = new ArrayList<Annotation>();
    private List<JavaModifier> modifiers = new ArrayList<JavaModifier>();
    private boolean varTypeArgument;


    public String getName() {
        return name;
    }

    public JavaParameterBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public Type getType() {
        return type;
    }

    public JavaParameterBuilder setType(Type type) {
        this.type = type;
        return this;
    }

    public List<Annotation> getAnnotations() {
        return annotations;
    }

    public JavaParameterBuilder setAnnotations(List<Annotation> annotations) {
        this.annotations = annotations;
        return this;
    }

    public JavaParameterBuilder addAnnotations(List<Annotation> annotations) {
        this.annotations.addAll(annotations);
        return this;
    }


    public JavaParameterBuilder addAnnotations(Annotation ... annotations) {
        Collections.addAll(this.annotations, annotations);
        return this;
    }


    public List<JavaModifier> getModifiers() {
        return modifiers;
    }

    public JavaParameterBuilder setModifiers(List<JavaModifier> modifiers) {
        this.modifiers = modifiers;
        return this;
    }


    public JavaParameterBuilder addModifiers(List<JavaModifier> modifiers) {
        this.modifiers.addAll(modifiers);
        return this;
    }


    public JavaParameterBuilder addModifiers(JavaModifier ... modifiers) {
        Collections.addAll(this.modifiers, modifiers);
        return this;
    }

    public boolean isVarTypeArgument() {
        return varTypeArgument;
    }

    public JavaParameterBuilder setVarTypeArgument(boolean varTypeArgument) {
        this.varTypeArgument = varTypeArgument;
        return this;
    }


    public JavaParameter build() {
        return new JavaParameterImpl(name,  type, annotations, modifiers, varTypeArgument);
    }
}
