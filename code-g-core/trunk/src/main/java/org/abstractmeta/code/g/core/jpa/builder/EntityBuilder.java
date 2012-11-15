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

import javax.persistence.Entity;
import java.lang.annotation.Annotation;

/**
 * Builds a {@link javax.persistence.Entity} annotation.
 *
 * @author Adrian Witas

 */
public class EntityBuilder {

    private String name = "";

    public String getName() {
        return name;
    }

    public EntityBuilder setName(String name) {
        this.name = name;
        return this;
    }


    public Entity build() {
        return new EntityImpl(name);
    }


    public static class EntityImpl implements Entity {
        private String name;

        public EntityImpl(String name) {
            this.name = name;
        }

        @Override
        public String name() {
            return name;
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            return Entity.class;
        }
    }
}
