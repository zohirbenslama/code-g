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

import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.lang.annotation.Annotation;

/**
 * Builds a {@link javax.persistence.Table} annotation.
 *
 * @author Adrian Witas
 */
public class TableBuilder {

    private String name = "";
    private String catalog = "";
    private String schema = "";
    private UniqueConstraint [] uniqueConstraints = new UniqueConstraint []{};

    public String getName() {
        return name;
    }

    public TableBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public String getCatalog() {
        return catalog;
    }

    public TableBuilder setCatalog(String catalog) {
        this.catalog = catalog;
        return this;
    }

    public String getSchema() {
        return schema;
    }

    public TableBuilder setSchema(String schema) {
        this.schema = schema;
        return this;
    }

    public UniqueConstraint[] getUniqueConstraints() {
        return uniqueConstraints;
    }

    public TableBuilder setUniqueConstraints(UniqueConstraint[] uniqueConstraints) {
        this.uniqueConstraints = uniqueConstraints;
        return this;
    }


    public Table build() {
        return new TableImpl(name, catalog, schema, uniqueConstraints);
    }

    public static class TableImpl implements Table {

        private String name;
        private String catalog;
        private String schema;
        private UniqueConstraint [] uniqueConstraints;

        public TableImpl(String name, String catalog, String schema, UniqueConstraint[] uniqueConstraints) {
            this.name = name;
            this.catalog = catalog;
            this.schema = schema;
            this.uniqueConstraints = uniqueConstraints;
        }

        @Override
        public String name() {
            return name;
        }

        @Override
        public String catalog() {
            return catalog;
        }

        @Override
        public String schema() {
            return schema;
        }

        @Override
        public UniqueConstraint[] uniqueConstraints() {
            return uniqueConstraints;
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            return Table.class;
        }
    }
}
