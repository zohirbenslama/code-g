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

package org.abstractmeta.code.g.core.jpa;

import com.google.common.base.CaseFormat;
import org.abstractmeta.code.g.core.util.ReflectUtil;

import java.sql.*;
import java.sql.Date;
import java.util.*;

/**
 * This Loader loads table and column definition details from catalog meta data.
 *
 * @author Adrian Witas
 */
public class DbCatalogLoader {

    private final byte[] EMPTY_ARRAY = new byte[]{};
    private final static String TABLE_NAME = "TABLE_NAME";
    private final static String SCHEMA_NAME = "SCHEMA_NAME";
    private final static String IS_DEFAULT = "IS_DEFAULT";


    public Collection<String> getTableNames(Connection connection) {
        try {
            Collection<String> result = new ArrayList<String>();
            DatabaseMetaData metaData = connection.getMetaData();
            String defaultSchemaName = getDefaultSchemaName(metaData);
            String catalog = metaData.getConnection().getCatalog();
            ResultSet resultSet = metaData.getTables(catalog, defaultSchemaName, "%", new String[]{"TABLE", "VIEW", "ALIAS", "SYNONYM"});
            int taleNameColumnIndex = getColumnIndex(getColumns(resultSet.getMetaData()), TABLE_NAME);
            while (resultSet.next()) {
                String tableName = resultSet.getString(taleNameColumnIndex);
                result.add(tableName);
            }
            return result;
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to fetch tables", e);
        }
    }

    public Set<String> getTableSequences(Connection connection) throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet resultSet = metaData.getTables(connection.getCatalog(), null, "%", new String[]{"SEQUENCE"});
        int taleNameColumnIndex = getColumnIndex(getColumns(resultSet.getMetaData()), TABLE_NAME);
        Set<String> result = new HashSet<String>();
        while (resultSet.next()) {
            String tableName = resultSet.getString(taleNameColumnIndex);
            result.add(tableName);
        }
        return result;

    }

    public Collection<ColumnFieldMap> loadColumnFieldMap(Connection connection, String tableName, Map<Integer, Class> typeMapping) {
        Collection<ColumnFieldMap> result = new ArrayList<ColumnFieldMap>();
        try {
            ResultSet resultSet = getTableResultSet(connection, tableName);
            Set<String> sequences = getTableSequences(connection);
            DatabaseMetaData metaData = connection.getMetaData();
            String defaultSchemaName = getDefaultSchemaName(metaData);
            String catalog = metaData.getConnection().getCatalog();
            Set<String> primaryKeyColumns = getPrimaryKeyColumns(metaData, catalog, defaultSchemaName, tableName);
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            for (int columnNumber = 1; columnNumber <= resultSetMetaData.getColumnCount(); columnNumber++) {
                ColumnFieldMap columnFieldMap = readColumnFieldMap(resultSetMetaData, columnNumber, primaryKeyColumns, typeMapping);
                if(sequences.contains(tableName) && columnFieldMap.isAutoIncrement()) {
                    columnFieldMap.setSequence(true);
                }
                result.add(columnFieldMap);
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to loadColumnFieldMap " + tableName, e);
        }
        return result;
    }


    protected ColumnFieldMap readColumnFieldMap(ResultSetMetaData resultSetMetaData, int columnNumber, Set<String> primaryKeyColumns, Map<Integer, Class> typeMapping) throws SQLException {
        String columnName = resultSetMetaData.getColumnName(columnNumber);
        Class columnType = getColumnType(
                resultSetMetaData.getColumnType(columnNumber),
                resultSetMetaData.getColumnClassName(columnNumber),
                typeMapping);
        ColumnFieldMap result = new ColumnFieldMap();
        result.setFieldName(CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, columnName.toUpperCase()));
        result.setType(columnType);

        result.setColumnName(columnName);
        result.setPosition(columnNumber);

        result.setAutoIncrement(resultSetMetaData.isAutoIncrement(columnNumber));
        if(resultSetMetaData.isAutoIncrement(columnNumber)) {

        }
        if (resultSetMetaData.isNullable(columnNumber) == ResultSetMetaData.columnNullable) {
            result.setNullable(true);
        } else if (resultSetMetaData.isNullable(columnNumber) == ResultSetMetaData.columnNoNulls) {
            result.setNullable(false);
            Class primitiveType = ReflectUtil.getPrimitiveType(columnType);
            if(primitiveType != null) {
                result.setType(primitiveType);
            }

        }
        if (resultSetMetaData.isReadOnly(columnNumber)) {
            result.setInsertable(false);
            result.setUpdateable(false);
        } else {
            result.setUpdateable(resultSetMetaData.isWritable(columnNumber));
        }
        if (primaryKeyColumns.contains(columnName)) {
            result.setId(true);
        }
        return result;
    }

    protected Set<String> getPrimaryKeyColumns(DatabaseMetaData metaData, String catalog, String schemaName, String tableName) throws SQLException {
        Set<String> result = new HashSet<String>();
        ResultSet resultSet = metaData.getPrimaryKeys(catalog, schemaName, tableName);
        if (resultSet.next()) {
            String columnName = resultSet.getString(4);
            result.add(columnName);
        }
        return result;
    }

    protected String getDefaultSchemaName(DatabaseMetaData metaData) throws SQLException {
        ResultSet schemaResultSet = metaData.getSchemas();
        List<String> columns = getColumns(schemaResultSet.getMetaData());
        int schemaNameIndex = getColumnIndex(columns, SCHEMA_NAME);
        int isDefaultIndex = getColumnIndex(columns, IS_DEFAULT);
        while (schemaResultSet.next()) {
            if (schemaResultSet.getBoolean(isDefaultIndex)) {
                return schemaResultSet.getString(schemaNameIndex);
            }
        }
        return null;
    }

    protected ResultSet getTableResultSet(Connection connection, String tableName) {
        String sql;
        if (!tableName.contains("SELECT")) {
            sql = "SELECT * FROM " + tableName + "  WHERE 1 = 0";
        } else {
            sql = "SELECT t.* FROM (" + tableName + ") t  WHERE 1 = 0";
        }
        return getSqlResultSet(connection, sql);
    }

    protected ResultSet getSqlResultSet(Connection connection, String sql) {
        try {
            return connection.createStatement().executeQuery(sql);
        } catch (SQLException e) {
            //ignore it -- we possible try to read table from different schema
        }
        return null;
    }

    public Class getColumnType(int sqlType, String className, Map<Integer, Class> typeMapping) {
        if (typeMapping.containsKey(sqlType)) {
            return typeMapping.get(sqlType);
        }
        switch (sqlType) {
            case Types.CHAR:
            case Types.VARCHAR:
            case Types.NCHAR:
            case Types.LONGVARCHAR:
            case Types.ROWID:
                return String.class;
            case Types.BIT:
                return Boolean.class;
            case Types.TINYINT:
            case Types.SMALLINT:
                return Short.class;
            case Types.INTEGER:
                return Integer.class;
            case Types.DECIMAL:
            case Types.NUMERIC:
            case Types.BIGINT:
                return Long.class;
            case Types.DOUBLE:
                return Double.class;
            case Types.FLOAT:
                return Float.class;
            case Types.TIMESTAMP:
                return Timestamp.class;
            case Types.TIME:
                return Time.class;
            case Types.DATE:
                return Date.class;
            case Types.LONGVARBINARY:
            case Types.BINARY:
            case Types.BLOB:
            case Types.CLOB:
                return EMPTY_ARRAY.getClass();
            default:
                try {
                    typeMapping.put(sqlType, Class.forName(className));
                } catch (ClassNotFoundException e) {
                    throw new IllegalStateException("Failed to lookup class name " + className + " for sql type " + sqlType, e);
                }
                return typeMapping.get(sqlType);
        }
    }

    private int getColumnIndex(List<String> columns, String columnName) {
        for (String candidate : columns) {
            if (candidate.equalsIgnoreCase(columnName)) {
                return columns.indexOf(candidate) + 1;
            }
        }
        throw new IllegalStateException("Failed to locate table name column");
    }


    protected List<String> getColumns(ResultSetMetaData resultSetMeta) throws SQLException {
        int columnCount = resultSetMeta.getColumnCount();
        List<String> result = new ArrayList<String>(columnCount);
        for (int i = 1; i <= columnCount; i++) {
            result.add(resultSetMeta.getColumnName(i));
        }
        return result;
    }


}
