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

/**
 * Represents JpaEntityGeneratorPlugin
 *
 * @author Adrian Witas
 */
public class JpaEntityGeneratorPlugin  {

//    public static final String BUILD_INTERFACE = "buildInterface";
//
//    private static final Map<String, Descriptor> TEMPLATES = MapMaker.makeImmutable(Descriptor.class,
//            ClassGeneratorPlugin.class.getName(),
//            new DescriptorBuilder().setTargetPackagePostfix("persistence")
//                    .setTargetPostfix("Entity")
//                    .addOptions("generateHashCodeMethod", "true")
//                    .addOptions("generateEqualsMethod", "true")
//                    .addOptions("hashFieldAnnotation", javax.persistence.Id.class.getName())
//                    .build()
//    );
//
//
//    private final JpaEntityGeneratorConfigurationDecoder decoder;
//    private final DbCatalogLoader dbCatalogLoader;
//
//
//    public JpaEntityGeneratorPlugin() {
//        this(new JpaEntityGeneratorConfigurationDecoder(), new DbCatalogLoader());
//    }
//
//    public JpaEntityGeneratorPlugin(JpaEntityGeneratorConfigurationDecoder decoder, DbCatalogLoader dbCatalogLoader) {
//        this.decoder = decoder;
//        this.dbCatalogLoader = dbCatalogLoader;
//    }
//
//    @Override
//    public List<String> generate(Collection<String> sourceTypeNames, JavaTypeRegistry registry, Descriptor descriptor) {
//        JpaEntityGeneratorConfiguration configuration = decoder.decode(descriptor);
//        Connection connection = connect(configuration.getConnection());
//        List<String> entityTypes = addEntityTypes(configuration, connection, registry);
//        for (String entityTypeName : entityTypes) {
//            JavaType entityType = registry.get(entityTypeName);
//            JavaTypeBuilder entityTypeBuilder = getEntityClassBuilder(entityType, descriptor, registry);
//            buildInterface(descriptor, entityTypeBuilder);
//            registry.register(entityTypeBuilder.build());
//        }
//        return entityTypes;
//    }
//
//    private void buildInterface(Descriptor descriptor, JavaTypeBuilder entityTypeBuilder) {
//        if (!DescriptorUtil.is(descriptor, BUILD_INTERFACE)) {
//            return;
//        }
//        throw new UnsupportedOperationException("Yet no supported");
//        //TODO add support
//    }
//
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
//    protected List<String> addEntityTypesFromTable(JpaEntityGeneratorConfiguration configuration, Connection connection, JavaTypeRegistry registry) {
//        List<String> result = new ArrayList<String>();
//        if (configuration.getTableNames() != null && configuration.getTableNames().size() > 0) {
//            for (String tableName : configuration.getTableNames()) {
//                JavaType type = addEntityType(tableName, connection, configuration);
//                if (type != null) {
//                    registry.register(type);
//                    result.add(type.getName());
//                }
//            }
//        }
//        return result;
//    }
//
//    @Override
//    public Map<String, String> getOptions() {
//        return Collections.emptyMap();
//    }
//
//    @Override
//    public Map<String, Descriptor> getTemplates() {
//        return TEMPLATES;
//    }
//
//
//    protected Connection connect(DbConnection dbConnection) {
//        try {
//            if (dbConnection.getUsername() != null) {
//                return DriverManager.getConnection(dbConnection.getUrl(), dbConnection.getUsername(), dbConnection.getPassword());
//            }
//            return DriverManager.getConnection(dbConnection.getUrl());
//        } catch (SQLException e) {
//            throw new IllegalStateException("Failed to connect to " + dbConnection.getUrl() + " " + dbConnection.getUsername() + "/*****", e);
//        }
//    }
//
//    protected JavaType addNativeSqlEntityType(String tableName, Connection connection, JpaEntityGeneratorConfiguration configuration) {
//        throw new UnsupportedOperationException();
//    }
//
//
//    protected JavaType addEntityType(String tableName, Connection connection, JpaEntityGeneratorConfiguration configuration) {
//        Collection<ColumnFieldMap> columnFieldMaps = dbCatalogLoader.loadColumnFieldMap(connection, tableName, configuration.getTypeMapping());
//        JavaTypeBuilder resultBuilder = buildJavaType(tableName, configuration, columnFieldMaps);
//        resultBuilder.addAnnotations(new TableBuilder().setName(tableName).build());
//        EntityBuilder entityBuilder = new EntityBuilder();
//        entityBuilder.setName(resultBuilder.getSimpleName());
//        resultBuilder.addAnnotations(entityBuilder.build());
//        return resultBuilder.build();
//    }
//
//    protected JavaTypeBuilder buildJavaType(String name, JpaEntityGeneratorConfiguration configuration, Collection<ColumnFieldMap> columnFieldMaps) {
//        JavaTypeBuilderImpl result = new JavaTypeBuilderImpl(JavaKind.CLASS, getEntityTypeName(configuration, name));
//        result.addModifiers(JavaModifier.PUBLIC);
//        for (ColumnFieldMap columnFieldMap : columnFieldMaps) {
//            result.addField(getField(columnFieldMap));
//        }
//        return result;
//    }
//
//    private JavaField getField(ColumnFieldMap columnFieldMap) {
//        JavaFieldBuilder resultBuilder = new JavaFieldBuilder();
//        if (columnFieldMap.isId()) {
//            resultBuilder.addAnnotation(new IdBuilder().build());
//            if (columnFieldMap.isAutoIncrement()) {
//                GeneratedValueBuilder generatedValueBuilder = new GeneratedValueBuilder();
//                if (columnFieldMap.isSequence()) {
//                    generatedValueBuilder.setStrategy(GenerationType.SEQUENCE);
//                } else {
//                    generatedValueBuilder.setStrategy(GenerationType.IDENTITY);
//                }
//            }
//        } else {
//            resultBuilder.addAnnotation(new BasicBuilder().build());
//        }
//        if (columnFieldMap.getType().getSimpleName().equals("Timestamp")) {
//            resultBuilder.addAnnotation(new TemporalBuilder().setTemporalType(TemporalType.TIMESTAMP).build());
//        } else if (columnFieldMap.getType().getSimpleName().equals("Date")) {
//            resultBuilder.addAnnotation(new TemporalBuilder().setTemporalType(TemporalType.DATE).build());
//        } else if (columnFieldMap.getType().getSimpleName().equals("Time")) {
//            resultBuilder.addAnnotation(new TemporalBuilder().setTemporalType(TemporalType.TIME).build());
//        }
//        resultBuilder.setName(columnFieldMap.getFieldName());
//        resultBuilder.setType(columnFieldMap.getType());
//        resultBuilder.addModifiers(JavaModifier.PRIVATE);
//        ColumnBuilder columnBuilder = new ColumnBuilder();
//        columnBuilder.setName(columnFieldMap.getColumnName());
//        columnBuilder.setNullable(columnFieldMap.isNullable());
//        columnBuilder.setUpdatable(columnFieldMap.isUpdateable());
//        columnBuilder.setInsertable(columnFieldMap.isInsertable());
//        resultBuilder.addAnnotation(columnBuilder.build());
//
//        return resultBuilder.build();
//    }
//
//    protected String getEntityTypeName(JpaEntityGeneratorConfiguration configuration, String tableName) {
//        if (tableName == null) {
//            throw new IllegalArgumentException("tableName was null");
//        }
//        String upperCamelTypeName = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, tableName.toUpperCase());
//        StringBuilder resultBuilder = new StringBuilder(configuration.getTargetPackage()).append(".");
//        if (CodeGeneratorUtil.isNotEmpty(configuration.getEntityPrefix())) {
//            resultBuilder.append(configuration.getEntityPrefix());
//        }
//        resultBuilder.append(upperCamelTypeName);
//        if (CodeGeneratorUtil.isNotEmpty(configuration.getEntityPostfix())) {
//            resultBuilder.append(configuration.getEntityPostfix());
//        }
//        return resultBuilder.toString();
//    }

}

