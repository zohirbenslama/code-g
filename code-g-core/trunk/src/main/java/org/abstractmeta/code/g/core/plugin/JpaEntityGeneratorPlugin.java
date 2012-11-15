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
package org.abstractmeta.code.g.core.plugin;

import com.google.common.base.CaseFormat;
import org.abstractmeta.code.g.code.JavaField;
import org.abstractmeta.code.g.code.JavaType;
import org.abstractmeta.code.g.code.JavaTypeRegistry;
import org.abstractmeta.code.g.config.Descriptor;
import org.abstractmeta.code.g.core.builder.SimpleClassBuilder;
import org.abstractmeta.code.g.core.code.builder.JavaFieldBuilder;
import org.abstractmeta.code.g.core.code.builder.JavaTypeBuilder;
import org.abstractmeta.code.g.core.config.jpa.JpaEntityGeneratorConfiguration;
import org.abstractmeta.code.g.core.config.jpa.JpaEntityGeneratorConfigurationDecoder;
import org.abstractmeta.code.g.core.jpa.ColumnFieldMap;
import org.abstractmeta.code.g.core.jpa.DbCatalogLoader;
import org.abstractmeta.code.g.core.jpa.DbConnection;
import org.abstractmeta.code.g.core.jpa.builder.*;
import org.abstractmeta.code.g.core.util.DescriptorUtil;
import org.abstractmeta.code.g.core.util.StringUtil;
import org.abstractmeta.code.g.plugin.CodeGeneratorPlugin;


import javax.persistence.GenerationType;
import javax.persistence.TemporalType;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents JpaEntityGeneratorPlugin
 *
 * @author Adrian Witas
 */
public class JpaEntityGeneratorPlugin implements CodeGeneratorPlugin {

    public static final String BUILD_INTERFACE = "buildInterface";

    private final JpaEntityGeneratorConfigurationDecoder decoder;
    private final DbCatalogLoader dbCatalogLoader;


    public JpaEntityGeneratorPlugin() {
        this(new JpaEntityGeneratorConfigurationDecoder(), new DbCatalogLoader());
    }

    public JpaEntityGeneratorPlugin(JpaEntityGeneratorConfigurationDecoder decoder, DbCatalogLoader dbCatalogLoader) {
        this.decoder = decoder;
        this.dbCatalogLoader = dbCatalogLoader;
    }

    @Override
    public List<String> generate(Collection<String> sourceTypeNames, JavaTypeRegistry registry, Descriptor descriptor) {
        JpaEntityGeneratorConfiguration configuration = decoder.decode(descriptor);
        Connection connection = connect(configuration.getConnection());
        List<String> entityTypes = addEntityTypes(configuration, connection, registry);
        for (String entityTypeName : entityTypes) {
            JavaType entityType = registry.get(entityTypeName);
            JavaTypeBuilder entityTypeBuilder = getEntityClassBuilder(entityType, descriptor);
            buildInterface(descriptor, entityTypeBuilder);
            registry.register(entityTypeBuilder.build());
        }
        return entityTypes;
    }

    private void buildInterface(Descriptor descriptor, JavaTypeBuilder entityTypeBuilder) {
         if(! DescriptorUtil.is(descriptor, BUILD_INTERFACE)) {
             return;
         }

    }


    protected JavaTypeBuilder getEntityClassBuilder(JavaType sourceType, Descriptor descriptor) {
        JavaTypeBuilder result = new SimpleClassBuilder(sourceType, descriptor);
        result.merge(sourceType);
        return result;
    }

    protected List<String> addEntityTypes(JpaEntityGeneratorConfiguration configuration, Connection connection, JavaTypeRegistry registry) {
        List<String> result = new ArrayList<String>();
        result.addAll(addEntityTypesFromTable(configuration, connection, registry));
        result.addAll(addMatchedEntityTypesFromTable(configuration, connection, registry));
        if (StringUtil.isNotEmpty(configuration.getSql())) {
            JavaType type = addEntityType(configuration.getSql(), connection, configuration);
            registry.register(type);
            result.add(type.getName());
        }
        return result;
    }

    protected List<String> addMatchedEntityTypesFromTable(JpaEntityGeneratorConfiguration configuration, Connection connection, JavaTypeRegistry registry) {
        List<String> result = new ArrayList<String>();
        Pattern tableMatchingPattern = configuration.getTableMatchingPattern();
        if (tableMatchingPattern == null) return result;
        for (String tableName : dbCatalogLoader.getTableNames(connection)) {
            Matcher matcher = tableMatchingPattern.matcher(tableName);
            if (matcher.matches()) {
                JavaType type = addEntityType(tableName, connection, configuration);
                registry.register(type);
                result.add(type.getName());
            }
        }
        return result;
    }


    protected List<String> addEntityTypesFromTable(JpaEntityGeneratorConfiguration configuration, Connection connection, JavaTypeRegistry registry) {
        List<String> result = new ArrayList<String>();
        if (configuration.getTablesNames() != null && configuration.getTablesNames().size() > 0) {
            for (String tableName : configuration.getTablesNames()) {
                JavaType type = addEntityType(tableName, connection, configuration);
                if (type != null) {
                    registry.register(type);
                    result.add(type.getName());
                }
            }
        }
        return result;
    }

    @Override
    public Map<String, String> getOptions() {
        return Collections.emptyMap();
    }


    protected Connection connect(DbConnection dbConnection) {
        try {
            if (dbConnection.getUsername() != null) {
                return DriverManager.getConnection(dbConnection.getUrl(), dbConnection.getUsername(), dbConnection.getPassword());
            }
            return DriverManager.getConnection(dbConnection.getUrl());
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to connect to " + dbConnection.getUrl() + " " + dbConnection.getUsername() + "/*****", e);
        }
    }

    protected JavaType addNativeSqlEntityType(String tableName, Connection connection, JpaEntityGeneratorConfiguration configuration) {
        throw new UnsupportedOperationException();
    }


    protected JavaType addEntityType(String tableName, Connection connection, JpaEntityGeneratorConfiguration configuration) {
        Collection<ColumnFieldMap> columnFieldMaps = dbCatalogLoader.loadColumnFieldMap(connection, tableName, configuration.getTypeMapping());
        JavaTypeBuilder resultBuilder = buildJavaType(tableName, configuration, columnFieldMaps);
        resultBuilder.addAnnotation(new TableBuilder().setName(tableName).build());
        EntityBuilder entityBuilder = new EntityBuilder();
        entityBuilder.setName(resultBuilder.getSimpleName());
        resultBuilder.addAnnotation(entityBuilder.build());
        resultBuilder.setPackageName(configuration.getTargetPackage());
        return resultBuilder.build();
    }

    protected JavaTypeBuilder buildJavaType(String name, JpaEntityGeneratorConfiguration configuration, Collection<ColumnFieldMap> columnFieldMaps) {
        JavaTypeBuilder result = new JavaTypeBuilder();
        result.addModifier("public");
        String entityName = getEntityTypeName(configuration, name);
        result.setPackageName(configuration.getTargetPackage());
        result.setName(entityName);

        for (ColumnFieldMap columnFieldMap : columnFieldMaps) {
            result.addField(getField(columnFieldMap));
        }
        return result;
    }

    private JavaField getField(ColumnFieldMap columnFieldMap) {
        JavaFieldBuilder resultBuilder = new JavaFieldBuilder();
        if (columnFieldMap.isId()) {
            resultBuilder.addAnnotation(new IdBuilder().build());
            if (columnFieldMap.isAutoIncrement()) {
                GeneratedValueBuilder generatedValueBuilder = new GeneratedValueBuilder();
                if (columnFieldMap.isSequence()) {
                    generatedValueBuilder.setStrategy(GenerationType.SEQUENCE);
                } else {
                    generatedValueBuilder.setStrategy(GenerationType.IDENTITY);
                }
            }
        } else {
            resultBuilder.addAnnotation(new BasicBuilder().build());
        }
        if (columnFieldMap.getType().getSimpleName().equals("Timestamp")) {
            resultBuilder.addAnnotation(new TemporalBuilder().setTemporalType(TemporalType.TIMESTAMP).build());
        } else if (columnFieldMap.getType().getSimpleName().equals("Date")) {
            resultBuilder.addAnnotation(new TemporalBuilder().setTemporalType(TemporalType.DATE).build());
        } else if (columnFieldMap.getType().getSimpleName().equals("Time")) {
            resultBuilder.addAnnotation(new TemporalBuilder().setTemporalType(TemporalType.TIME).build());
        }
        resultBuilder.setName(columnFieldMap.getFieldName());
        resultBuilder.setType(columnFieldMap.getType());
        resultBuilder.addModifiers(Arrays.asList("private"));
        ColumnBuilder columnBuilder = new ColumnBuilder();
        columnBuilder.setName(columnFieldMap.getColumnName());
        columnBuilder.setNullable(columnFieldMap.isNullable());
        columnBuilder.setUpdatable(columnFieldMap.isUpdateable());
        columnBuilder.setInsertable(columnFieldMap.isInsertable());
        resultBuilder.addAnnotation(columnBuilder.build());

        return resultBuilder.build();
    }

    protected String getEntityTypeName(JpaEntityGeneratorConfiguration configuration, String tableName) {
        if (tableName == null) {
            throw new IllegalArgumentException("tableName was null");
        }
        String upperCamelTypeName = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, tableName.toUpperCase());
        StringBuilder resultBuilder = new StringBuilder(configuration.getTargetPackage()).append(".");
        if (StringUtil.isNotEmpty(configuration.getEntityPrefix())) {
            resultBuilder.append(configuration.getEntityPrefix());
        }
        resultBuilder.append(upperCamelTypeName);
        if (StringUtil.isNotEmpty(configuration.getEntityPostfix())) {
            resultBuilder.append(configuration.getEntityPostfix());
        }
        return resultBuilder.toString();
    }

}

