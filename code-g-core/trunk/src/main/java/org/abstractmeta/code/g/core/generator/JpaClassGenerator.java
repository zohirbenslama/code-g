package org.abstractmeta.code.g.core.generator;

import org.abstractmeta.code.g.code.*;
import org.abstractmeta.code.g.config.NamingConvention;
import org.abstractmeta.code.g.config.loader.LoadedSource;
import org.abstractmeta.code.g.core.builder.SimpleClassBuilder;
import org.abstractmeta.code.g.core.code.builder.JavaFieldBuilder;
import org.abstractmeta.code.g.core.code.builder.JavaTypeBuilderImpl;
import org.abstractmeta.code.g.core.config.loader.JavaSourceLoaderImpl;
import org.abstractmeta.code.g.core.config.loader.LoadedSourceImpl;
import org.abstractmeta.code.g.core.jpa.ColumnFieldMap;
import org.abstractmeta.code.g.core.jpa.DbCatalogLoader;
import org.abstractmeta.code.g.core.jpa.DbConnection;
import org.abstractmeta.code.g.core.jpa.builder.*;
import org.abstractmeta.code.g.core.util.CodeGeneratorUtil;
import org.abstractmeta.code.g.generator.CodeGenerator;
import org.abstractmeta.code.g.generator.Context;
import org.abstractmeta.code.g.property.PropertyRegistry;
import org.abstractmeta.code.g.renderer.JavaTypeRenderer;

import javax.inject.Provider;
import javax.persistence.GenerationType;
import javax.persistence.TemporalType;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

/**
 * Represents JpaClassGenerator
 *
 * @author Adrian Witas
 */
public class JpaClassGenerator extends AbstractGenerator<JpaClassConfig> implements CodeGenerator<JpaClassConfig> {

    private final PropertyRegistry propertyRegistry;
    private final DbCatalogLoader dbCatalogLoader;

    public JpaClassGenerator(PropertyRegistry propertyRegistry, DbCatalogLoader dbCatalogLoader, Provider<JavaTypeRenderer> javaTypeRendererProvider) {
        super(new JavaSourceLoaderImpl(), propertyRegistry, javaTypeRendererProvider);
        this.propertyRegistry = propertyRegistry;
        this.dbCatalogLoader = dbCatalogLoader;
    }

    @Override
    protected Collection<SourcedJavaType> generate(JavaType sourceType, Context context) {
        String targetName = formatTargetClassName(context, sourceType);
        SimpleClassBuilder simpleClassBuilder = new SimpleClassBuilder(targetName, sourceType, context);
        simpleClassBuilder.addModifiers(JavaModifier.PUBLIC);
        simpleClassBuilder.addAnnotations(sourceType.getAnnotations());
        addExtractableFields(simpleClassBuilder, sourceType, context);
        addExtractableMethods(simpleClassBuilder, sourceType, context);
        SourcedJavaType result = renderCode(simpleClassBuilder);
        return Arrays.asList(result);
    }

        @Override
    protected boolean isApplicable(JavaType javaType, Context context) {
        return context.contains(JpaClassConfig.class);
    }


    /**
     * Loads soures from database
     *
     * @param context
     * @return
     */
    @Override
    protected LoadedSource loadSource(Context context) {
        JpaClassConfig config = context.get(JpaClassConfig.class);
        Connection connection = null;
        try {
            connection = connect(config.getConnection());
        } finally {
            try {
                if (connection != null) connection.close();
            } catch (SQLException ignore) {}
        }
        List<JavaType> result = new ArrayList<JavaType>();
        result.addAll(addEntityTypesFromTable(connection, context));
        result.addAll(addEntityTypesFromNamedSql(connection, context));
        LoadedSourceImpl loadedSource = new LoadedSourceImpl();
        loadedSource.setJavaTypes(result);
        if(context.contains(ClassLoader.class)) {
            loadedSource.setClassLoader(context.get(ClassLoader.class));
        }
        return loadedSource;
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




    protected List<JavaType> addEntityTypesFromTable(Connection connection, Context context) {
        List<JavaType> result = new ArrayList<JavaType>();
        JpaClassConfig config = context.get(JpaClassConfig.class);
        if (config.getTablesNames() != null && !config.getTablesNames().isEmpty()) {
            for (String tableName : config.getTablesNames()) {
                JavaType type = addEntityType(tableName, tableName, connection, context);
                result.add(type);
            }
        }
        return result;
    }

    protected List<JavaType> addEntityTypesFromNamedSql(Connection connection, Context context) {
        List<JavaType> result = new ArrayList<JavaType>();
        JpaClassConfig config = context.get(JpaClassConfig.class);
        if (config.getNamedSqls() != null && !config.getNamedSqls().isEmpty()) {
            for (Map.Entry<String, String> entry : config.getNamedSqls().entrySet()) {
                JavaType type = addEntityType(entry.getKey(), entry.getValue(), connection, context);
                result.add(type);
            }
        }
        return result;
    }


    protected JavaType addEntityType(String name, String sql, Connection connection, Context context) {
        JpaClassConfig config = context.get(JpaClassConfig.class);
        Collection<ColumnFieldMap> columnFieldMaps = dbCatalogLoader.loadColumnFieldMap(connection, sql, config.getTypeMapping());
        JavaTypeBuilder resultBuilder = buildJavaType(name, columnFieldMaps, context);
        resultBuilder.addAnnotations(new TableBuilder().setName(name).build());
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
