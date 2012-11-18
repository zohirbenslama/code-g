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
package org.abstractmeta.code.g.core.config.jpa;

import org.abstractmeta.code.g.core.jpa.DbConnection;

import java.util.Collection;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Represents JpaEntityGeneratorConfiguration
 *
 * @author Adrian Witas
 */
public class JpaEntityGeneratorConfiguration {


    private DbConnection connection;

    private Collection<String> tablesNames;
    private Pattern tableMatchingPattern;
    private String sql;
    private String sqlName;
    private String entityPrefix;
    private String entityPostfix;
    private String targetPackage;
    private Map<Integer, Class> typeMapping;

    public DbConnection getConnection() {
        return connection;
    }

    public void setConnection(DbConnection connection) {
        this.connection = connection;
    }

    public Collection<String> getTablesNames() {
        return tablesNames;
    }

    public void setTablesNames(Collection<String> tablesNames) {
        this.tablesNames = tablesNames;
    }

    public Pattern getTableMatchingPattern() {
        return tableMatchingPattern;
    }

    public void setTableMatchingPattern(Pattern tableMatchingPattern) {
        this.tableMatchingPattern = tableMatchingPattern;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getSqlName() {
        return sqlName;
    }

    public void setSqlName(String sqlName) {
        this.sqlName = sqlName;
    }

    public String getEntityPrefix() {
        return entityPrefix;
    }

    public void setEntityPrefix(String entityPrefix) {
        this.entityPrefix = entityPrefix;
    }

    public String getEntityPostfix() {
        return entityPostfix;
    }

    public void setEntityPostfix(String entityPostfix) {
        this.entityPostfix = entityPostfix;
    }

    public String getTargetPackage() {
        return targetPackage;
    }

    public void setTargetPackage(String targetPackage) {
        this.targetPackage = targetPackage;
    }

    public Map<Integer, Class> getTypeMapping() {
        return typeMapping;
    }

    public void setTypeMapping(Map<Integer, Class> typeMapping) {
        this.typeMapping = typeMapping;
    }
}
