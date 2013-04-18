package org.abstractmeta.code.g.core.generator;

import org.abstractmeta.code.g.core.jpa.DbConnection;

import java.util.Collection;
import java.util.Map;

/**
 * Represents JpaClassConfig
 *
 * @author Adrian Witas
 */
public class JpaClassConfig {

    private DbConnection connection;
    private Collection<String> tableNames;
    private Map<String, String> namedSqls;
    private String targetPackage;
    private Map<Integer, Class> typeMapping;

    public DbConnection getConnection() {
        return connection;
    }

    public void setConnection(DbConnection connection) {
        this.connection = connection;
    }

    public Collection<String> getTableNames() {
        return tableNames;
    }

    public void setTableNames(Collection<String> tableNames) {
        this.tableNames = tableNames;
    }

    public Map<String, String> getNamedSqls() {
        return namedSqls;
    }

    public void setNamedSqls(Map<String, String> namedSqls) {
        this.namedSqls = namedSqls;
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
