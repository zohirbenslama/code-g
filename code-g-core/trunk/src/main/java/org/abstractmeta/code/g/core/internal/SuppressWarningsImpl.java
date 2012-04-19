package org.abstractmeta.code.g.core.internal;


import java.lang.annotation.Annotation;

public class SuppressWarningsImpl implements SuppressWarnings {

    private final String [] value;

    public SuppressWarningsImpl(String... value) {
        this.value = value;
    }

    @Override
    public String[] value() {
        return value;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return SuppressWarnings.class;
    }
}
