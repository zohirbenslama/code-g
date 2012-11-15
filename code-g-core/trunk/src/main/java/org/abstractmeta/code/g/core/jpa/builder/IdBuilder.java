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

import javax.persistence.Id;
import java.lang.annotation.Annotation;

/**
 * Builds a {@link javax.persistence.Id} annotation.
 *
 * @author Adrian Witas
 */
public class IdBuilder {

    public Id build() {
        return new IdImpl();
    }

    public static class IdImpl implements Id {

        @Override
        public Class<? extends Annotation> annotationType() {
            return Id.class;
        }
    }

}

        
