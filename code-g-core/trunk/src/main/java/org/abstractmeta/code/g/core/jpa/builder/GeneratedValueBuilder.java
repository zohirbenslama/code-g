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

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.lang.annotation.Annotation;

/**
 * Builds a {@link javax.persistence.GeneratedValue} annotation.
 *
 * @author Adrian Witas
 */
public class GeneratedValueBuilder {

    private String generator = "";
    private GenerationType strategy = GenerationType.AUTO;

    public String getGenerator() {
        return generator;
    }

    public GeneratedValueBuilder setGenerator(String generator) {
        this.generator = generator;
        return this;
    }

    public GenerationType getStrategy() {
        return strategy;
    }

    public GeneratedValueBuilder setStrategy(GenerationType strategy) {
        this.strategy = strategy;
        return this;
    }

    public GeneratedValue build() {
        return new GeneratedValueImpl(generator, strategy);
    }


    public static class GeneratedValueImpl implements GeneratedValue {

        private String generator;
        private GenerationType strategy;

        public GeneratedValueImpl(String generator, GenerationType strategy) {
            this.generator = generator;
            this.strategy = strategy;
        }

        @Override
        public GenerationType strategy() {
            return strategy;
        }

        @Override
        public String generator() {
            return generator;
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            return GeneratedValue.class;
        }
    }
}





