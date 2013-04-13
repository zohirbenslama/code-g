package org.abstractmeta.code.g.core.generator;

import org.abstractmeta.code.g.core.jpa.DbConnection;

import java.util.Collection;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Represents JpaClassConfig
 * <p>
 * </p>
 *
 * @author Adrian Witas
 */
public class JpaClassConfig {

    private DbConnection connection;
    private Collection<String> tablesNames;
    private Map<String, String> namedSqls;
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
