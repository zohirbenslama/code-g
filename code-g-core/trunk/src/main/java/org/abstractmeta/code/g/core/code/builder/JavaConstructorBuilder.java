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
package org.abstractmeta.code.g.core.code.builder;


import org.abstractmeta.code.g.code.JavaConstructor;
import org.abstractmeta.code.g.code.JavaModifier;
import org.abstractmeta.code.g.code.JavaParameter;
import org.abstractmeta.code.g.core.code.JavaConstructorImpl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


/**
 * Provide generateBuilder implementation of org.abstractmeta.toolbox.code.JavaConstructor
 * This class has been auto-generated by code-g.
 */
public class JavaConstructorBuilder implements JavaConstructor {


    private List<JavaParameter> parameters = new ArrayList<JavaParameter>();

    private List<Type> exceptionTypes = new ArrayList<Type>();

    private List<String> bodyLines = new ArrayList<String>();

    private List<JavaModifier> modifiers = new ArrayList<JavaModifier>();

    private String name;

    private List<Annotation> annotations = new ArrayList<Annotation>();

    private List<String> documentation = new ArrayList<String>();


    @Override
    public List<JavaParameter> getParameters() {
        return parameters;
    }

    @Override
    public List<Type> getExceptionTypes() {
        return exceptionTypes;
    }


    public JavaConstructorBuilder addParameter(String name, Type type) {
        return addParameter(null, name, type);
    }

    public JavaConstructorBuilder addParameter(JavaModifier modifier, String name, Type type) {
        JavaParameterBuilder parameterBuilder = new JavaParameterBuilder();
        parameterBuilder.setName(name);
        parameterBuilder.setType(type);
        if (modifier != null) {
            parameterBuilder.addModifiers(modifier);
        }
        this.parameters.add(parameterBuilder.build());
        return this;
    }


    public JavaConstructorBuilder addParameters(JavaParameter... parameters) {
        Collections.addAll(this.parameters, parameters);
        return this;
    }

    public JavaConstructorBuilder addParameters(List<JavaParameter> parameters) {
        this.parameters.addAll(parameters);
        return this;
    }


    public JavaConstructorBuilder setExceptionTypes(List<Type> exceptionTypes) {
        this.exceptionTypes = exceptionTypes;
        return this;
    }

    public JavaConstructorBuilder addExceptionTypes(Collection<Type> exceptionTypes) {
        this.exceptionTypes.addAll(exceptionTypes);
        return this;
    }

    public JavaConstructorBuilder addExceptionTypes(Type... exceptionTypes) {
        Collections.addAll(this.exceptionTypes, exceptionTypes);
        return this;
    }

    public List<String> getBodyLines() {
        return this.bodyLines;
    }

    public JavaConstructorBuilder setBodyLines(List<String> bodyLines) {
        this.bodyLines = bodyLines;
        return this;
    }

    public JavaConstructorBuilder addBodyLines(String body) {
        this.bodyLines.add(body);
        return this;
    }

    public JavaConstructorBuilder addBodyLines(Collection<String> body) {
        this.bodyLines.addAll(body);
        return this;
    }

    public List<JavaModifier> getModifiers() {
        return this.modifiers;
    }


    public JavaConstructorBuilder setModifiers(List<JavaModifier> modifiers) {
        this.modifiers = modifiers;
        return this;
    }

    public JavaConstructorBuilder addModifier(JavaModifier modifier) {
        this.modifiers.add(modifier);
        return this;
    }


    public JavaConstructorBuilder addModifiers(Collection<JavaModifier> modifiers) {
        this.modifiers.addAll(modifiers);
        return this;
    }

    public JavaConstructorBuilder addModifiers(JavaModifier ... modifiers) {
           Collections.addAll(this.modifiers, modifiers);
           return this;
       }


    public String getName() {
        return this.name;
    }

    public JavaConstructorBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public List<Annotation> getAnnotations() {
        return this.annotations;
    }

    public JavaConstructorBuilder setAnnotations(List<Annotation> annotations) {
        this.annotations = annotations;
        return this;
    }

    public JavaConstructorBuilder addAnnotation(Annotation annotation) {
        this.annotations.add(annotation);
        return this;
    }

    public JavaConstructorBuilder addAnnotations(Collection<Annotation> annotations) {
        this.annotations.addAll(annotations);
        return this;
    }

    public List<String> getDocumentation() {
        return this.documentation;
    }

    public JavaConstructorBuilder setDocumentation(List<String> documentation) {
        this.documentation = documentation;
        return this;
    }

    public JavaConstructorBuilder addDocumentation(String documentation) {
        this.documentation.add(documentation);
        return this;
    }

    public JavaConstructorBuilder addDocumentation(Collection<String> documentation) {
        this.documentation.addAll(documentation);
        return this;
    }

    public JavaConstructor build() {
        return new JavaConstructorImpl(parameters, exceptionTypes, bodyLines, modifiers, name, annotations, documentation);
    }

    public void merge(JavaConstructor instance) {
        if (instance.getParameters() != null) {
            addParameters(instance.getParameters());
        }
        if (instance.getExceptionTypes() != null) {
            addExceptionTypes(instance.getExceptionTypes());
        }
        if (instance.getBodyLines() != null) {
            addBodyLines(instance.getBodyLines());
        }
        if (instance.getModifiers() != null) {
            addModifiers(instance.getModifiers());
        }
        if (instance.getName() != null) {
            setName(instance.getName());
        }
        if (instance.getAnnotations() != null) {
            addAnnotations(instance.getAnnotations());
        }
        if (instance.getDocumentation() != null) {
            addDocumentation(instance.getDocumentation());
        }

    }


}