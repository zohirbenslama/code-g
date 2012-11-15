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

import javax.persistence.Column;
import java.lang.annotation.Annotation;

/**
 * Builds a {@link javax.persistence.Column} annotation.
 *
 * @author Adrian Witas

 */
public class ColumnBuilder {

    private String columnDefinition = "";
    private boolean insertable = true;
    private int length = 255;
    private String name = "";
    private boolean nullable = true;
    private int precision;
    private int scale;
    private String table = "";
    private boolean unique;
    private boolean updatable = true;

    public String getColumnDefinition() {
        return columnDefinition;
    }

    public ColumnBuilder setColumnDefinition(String columnDefinition) {
        this.columnDefinition = columnDefinition;
        return this;
 
    }

    public boolean isInsertable() {
        return insertable;
    }

    public ColumnBuilder setInsertable(boolean insertable) {
        this.insertable = insertable;
        return this;
    }

    public int getLength() {
        return length;
    }

    public ColumnBuilder setLength(int length) {
        this.length = length;
        return this;
    }

    public String getName() {
        return name;
    }

    public ColumnBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public boolean isNullable() {
        return nullable;
    }

    public ColumnBuilder setNullable(boolean nullable) {
        this.nullable = nullable;
        return this;
    }

    public int getPrecision() {
        return precision;
    }

    public ColumnBuilder setPrecision(int precision) {
        this.precision = precision;
        return this;
    }

    public int getScale() {
        return scale;
    }

    public ColumnBuilder setScale(int scale) {
        this.scale = scale;
        return this;
    }

    public String getTable() {
        return table;
    }

    public ColumnBuilder setTable(String table) {
        this.table = table;
        return this;
    }

    public boolean isUnique() {
        return unique;
    }

    public ColumnBuilder setUnique(boolean unique) {
        this.unique = unique;
        return this;
    }

    public boolean isUpdatable() {
        return updatable;
    }

    public ColumnBuilder setUpdatable(boolean updatable) {
        this.updatable = updatable;
        return this;
    }

    public Column build() {
        return new ColumnImpl(columnDefinition, insertable, length, name, nullable, precision, scale, table, unique, updatable);
    }

    public static class ColumnImpl implements Column {

        private String columnDefinition;
        private boolean insertable;
        private int length;
        private String name;
        private boolean nullable;
        private int precision;
        private int scale;
        private String table;
        private boolean unique;
        private boolean updatable;

        public ColumnImpl(String columnDefinition, boolean insertable, int length, String name, boolean nullable, int precision, int scale, String table, boolean unique, boolean updatable) {
            this.columnDefinition = columnDefinition;
            this.insertable = insertable;
            this.length = length;
            this.name = name;
            this.nullable = nullable;
            this.precision = precision;
            this.scale = scale;
            this.table = table;
            this.unique = unique;
            this.updatable = updatable;
        }



        @Override
        public Class<? extends Annotation> annotationType() {
            return Column.class;
        }

        @Override
        public String name() {
            return name;
        }

        @Override
        public boolean unique() {
            return unique;
        }

        @Override
        public boolean nullable() {
            return nullable;
        }

        @Override
        public boolean insertable() {
            return insertable;
        }

        @Override
        public boolean updatable() {
            return updatable;
        }

        @Override
        public String columnDefinition() {
            return columnDefinition;
        }

        @Override
        public String table() {
            return table;
        }

        @Override
        public int length() {
            return length;
        }

        @Override
        public int precision() {
            return precision;
        }

        @Override
        public int scale() {
            return scale;
        }
    }
}
