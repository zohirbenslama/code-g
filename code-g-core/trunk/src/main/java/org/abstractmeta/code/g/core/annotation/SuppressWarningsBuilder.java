package org.abstractmeta.code.g.core.annotation;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 *  SuppressWarningsBuilder
 *
 * @author Adrian Witas
 */
public class SuppressWarningsBuilder {

    private Collection<String> value = new ArrayList<String>();


    public SuppressWarningsBuilder addValue(String ... values) {
        Collections.addAll(this.value, values);
        return this;
    }

    public SuppressWarningsBuilder addValue(Collection<String> values) {
        this.value.addAll(values);
        return this;
    }

    public SuppressWarnings build() {
        return new SuppressWarningsImpl(value.toArray(new String[]{}));
    }

    public static class SuppressWarningsImpl implements SuppressWarnings {

        private final String [] values;

        public SuppressWarningsImpl(String[] values) {
            this.values = values;
        }

        @Override
        public String[] value() {
            return values;
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            return SuppressWarnings.class;
        }
    }
}
