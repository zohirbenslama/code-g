package org.abstractmeta.code.g.core.generator;

import org.abstractmeta.code.g.code.*;
import org.abstractmeta.code.g.config.NamingConvention;
import org.abstractmeta.code.g.core.code.builder.JavaFieldBuilder;
import org.abstractmeta.code.g.core.code.builder.JavaTypeBuilderImpl;
import org.abstractmeta.code.g.core.jpa.ColumnFieldMap;
import org.abstractmeta.code.g.core.jpa.DbCatalogLoader;
import org.abstractmeta.code.g.core.jpa.DbConnection;
import org.abstractmeta.code.g.core.jpa.builder.*;
import org.abstractmeta.code.g.core.util.CodeGeneratorUtil;
import org.abstractmeta.code.g.generator.CodeGenerator;
import org.abstractmeta.code.g.generator.Context;
import org.abstractmeta.code.g.property.PropertyRegistry;

import javax.persistence.GenerationType;
import javax.persistence.TemporalType;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Represents JpaClassGenerator
 *
 * @author Adrian Witas
 */
public class JpaClassGenerator implements CodeGenerator<JpaClassConfig> {

    private final PropertyRegistry propertyRegistry;
    private final DbCatalogLoader dbCatalogLoader;

    public JpaClassGenerator(PropertyRegistry propertyRegistry, DbCatalogLoader dbCatalogLoader) {
        this.propertyRegistry = propertyRegistry;
        this.dbCatalogLoader = dbCatalogLoader;
    }

    @Override
    public List<CompiledJavaType> generate(Context context) {
        return null;
    }


    @Override
    public NamingConvention getNamingConvention(Context context) {
        return null;
    }

    @Override
    public Class<JpaClassConfig> getSettingClass() {
        return JpaClassConfig.class;
    }

    @Override
    public PropertyRegistry getPropertyRegistry() {
        return propertyRegistry;
    }


//    private void buildInterface(Descriptor descriptor, JavaTypeBuilder entityTypeBuilder) {

//        if (!DescriptorUtil.is(descriptor, BUILD_INTERFACE)) {
//            return;
//        }

//        throw new UnsupportedOperationException("Yet no supported");
//        //TODO add support
//    }
//


//    protected JavaTypeBuilder getEntityClassBuilder(JavaType sourceType, Descriptor descriptor, JavaTypeRegistry registry) {
//        JavaTypeBuilder result = new JavaTypeBuilderImpl(JavaKind.CLASS, sourceType.getName(), sourceType, descriptor, registry);
//        result.merge(sourceType);
//        return result;
//    }


//
//    protected List<String> addEntityTypes(JpaEntityGeneratorConfiguration configuration, Connection connection, JavaTypeRegistry registry) {
//        List<String> result = new ArrayList<String>();
//        result.addAll(addEntityTypesFromTable(configuration, connection, registry));
//        result.addAll(addMatchedEntityTypesFromTable(configuration, connection, registry));
//        if (CodeGeneratorUtil.isNotEmpty(configuration.getSql())) {
//            JavaType type = addEntityType(configuration.getSql(), connection, configuration);
//            registry.register(type);
//            result.add(type.getName());
//        }
//        return result;
//    }
//
//    protected List<String> addMatchedEntityTypesFromTable(JpaEntityGeneratorConfiguration configuration, Connection connection, JavaTypeRegistry registry) {
//        List<String> result = new ArrayList<String>();
//        Pattern tableMatchingPattern = configuration.getTableMatchingPattern();
//        if (tableMatchingPattern == null) return result;
//        for (String tableName : dbCatalogLoader.getTableNames(connection)) {
//            Matcher matcher = tableMatchingPattern.matcher(tableName);
//            if (matcher.matches()) {
//                JavaType type = addEntityType(tableName, connection, configuration);
//                registry.register(type);
//                result.add(type.getName());
//            }
//        }
//        return result;
//    }
//
//


    protected List<String> addEntityTypesFromTable(Connection connection, JavaTypeRegistry registry, Context context) {
        List<String> result = new ArrayList<String>();
        JpaClassConfig config = context.get(JpaClassConfig.class);
        if (config.getTablesNames() != null && config.getTablesNames().size() > 0) {
            for (String tableName : config.getTablesNames()) {
                JavaType type = addEntityType(tableName, connection, context);
                if (type != null) {
                    registry.register(type);
                    result.add(type.getName());
                }
            }
        }
        return result;
    }




    protected JavaType addEntityType(String tableName, Connection connection, Context context) {
        JpaClassConfig config = context.get(JpaClassConfig.class);
        Collection<ColumnFieldMap> columnFieldMaps = dbCatalogLoader.loadColumnFieldMap(connection, tableName, config.getTypeMapping());
        JavaTypeBuilder resultBuilder = buildJavaType(tableName, columnFieldMaps, context);
        resultBuilder.addAnnotations(new TableBuilder().setName(tableName).build());
        EntityBuilder entityBuilder = new EntityBuilder();
        entityBuilder.setName(resultBuilder.getSimpleName());
        resultBuilder.addAnnotations(entityBuilder.build());
        return resultBuilder.build();
    }


    protected JavaTypeBuilder buildJavaType(String name, Collection<ColumnFieldMap> columnFieldMaps, Context context) {
        JavaTypeBuilderImpl result = new JavaTypeBuilderImpl(JavaKind.CLASS, formatTargetClassName(context, name));
        result.addModifiers(JavaModifier.PUBLIC);
        for (ColumnFieldMap columnFieldMap : columnFieldMaps) {
            result.addField(getField(columnFieldMap));
        }
        return result;
    }


    protected String formatTargetClassName(Context context, String name) {
        JpaClassConfig config = context.get(JpaClassConfig.class);
        JavaType javaType = new JavaTypeBuilderImpl(config.getTargetPackage() + "." + name);
        return CodeGeneratorUtil.formatTargetClassName(context, javaType, getNamingConvention(context));
    }


    protected JavaField getField(ColumnFieldMap columnFieldMap) {
        JavaFieldBuilder resultBuilder = new JavaFieldBuilder();
        if (columnFieldMap.isId()) {
            resultBuilder.addAnnotations(new IdBuilder().build());
            if (columnFieldMap.isAutoIncrement()) {
                GeneratedValueBuilder generatedValueBuilder = new GeneratedValueBuilder();
                if (columnFieldMap.isSequence()) {
                    generatedValueBuilder.setStrategy(GenerationType.SEQUENCE);
                } else {
                    generatedValueBuilder.setStrategy(GenerationType.IDENTITY);
                }
            }
        } else {
            resultBuilder.addAnnotations(new BasicBuilder().build());
        }
        if (columnFieldMap.getType().getSimpleName().equals("Timestamp")) {
            resultBuilder.addAnnotations(new TemporalBuilder().setTemporalType(TemporalType.TIMESTAMP).build());
        } else if (columnFieldMap.getType().getSimpleName().equals("Date")) {
            resultBuilder.addAnnotations(new TemporalBuilder().setTemporalType(TemporalType.DATE).build());
        } else if (columnFieldMap.getType().getSimpleName().equals("Time")) {
            resultBuilder.addAnnotations(new TemporalBuilder().setTemporalType(TemporalType.TIME).build());
        }
        resultBuilder.setName(columnFieldMap.getFieldName());
        resultBuilder.setType(columnFieldMap.getType());
        resultBuilder.addModifiers(JavaModifier.PRIVATE);
        ColumnBuilder columnBuilder = new ColumnBuilder();
        columnBuilder.setName(columnFieldMap.getColumnName());
        columnBuilder.setNullable(columnFieldMap.isNullable());
        columnBuilder.setUpdatable(columnFieldMap.isUpdateable());
        columnBuilder.setInsertable(columnFieldMap.isInsertable());
        resultBuilder.addAnnotations(columnBuilder.build());

        return resultBuilder.build();
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



}
