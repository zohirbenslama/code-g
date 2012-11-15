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
package org.abstractmeta.code.g.core.jpa.builder;

import javax.persistence.Basic;
import javax.persistence.FetchType;
import java.lang.annotation.Annotation;

/**
 * Builds a {@link javax.persistence.Basic} annotation.
 *
 * @author Adrian Witas
 */
public class BasicBuilder {

    private FetchType fetch = FetchType.EAGER;
    private boolean optional = true;

    public FetchType getFetch() {
        return fetch;
    }

    public BasicBuilder setFetch(FetchType fetch) {
        this.fetch = fetch;
        return this;
    }

    public boolean isOptional() {
        return optional;
    }

    public BasicBuilder setOptional(boolean optional) {
        this.optional = optional;
        return this;
    }

    public Basic build() {
        return new BasicImpl(fetch, optional);
    }

    public static class BasicImpl implements Basic {


        private FetchType fetch;
        private boolean optional;

        public BasicImpl(FetchType fetch, boolean optional) {
            this.fetch = fetch;
            this.optional = optional;
        }

        @Override
        public FetchType fetch() {
            return fetch;
        }

        @Override
        public boolean optional() {
            return optional;
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            return Basic.class;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BasicBuilder that = (BasicBuilder) o;

        if (optional != that.optional) return false;
        if (fetch != that.fetch) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = fetch != null ? fetch.hashCode() : 0;
        result = 31 * result + (optional ? 1 : 0);
        return result;
    }
}
