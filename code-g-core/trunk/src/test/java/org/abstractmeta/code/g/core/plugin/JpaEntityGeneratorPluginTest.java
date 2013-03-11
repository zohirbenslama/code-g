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

import org.testng.annotations.Test;

/**
 * This class test jpa entity generator plugin.
 */
@Test
public class JpaEntityGeneratorPluginTest {

//    private final UnitDescriptorsDecoder decoder;
//    private final PropertyRegistry macroRegistry;
//
//    public JpaEntityGeneratorPluginTest() {
//        this.macroRegistry = new PropertyRegistryImpl();
//        this.decoder = new UnitDescriptorsDecoder();
//
//        File baseDirectory = new File(".").getAbsoluteFile();
//        this.macroRegistry.register("${basedir}", baseDirectory.getAbsolutePath());
//    }
//
//    private static Provider<Connection> connectionProvider;
//
//
//    public void testGenerate() throws Exception {
//        Properties properties = PropertiesUtil.loadFromFile(new File("src/test/code-g/jpa-unit-provider-test.properties"));
//        List<UnitDescriptor> unitDescriptors = decoder.decode(Maps.fromProperties(properties));
//        {
//            CodeUnitGenerator unitGenerator = new CodeUnitGeneratorImpl(macroRegistry, new SourceCompilerHandler.Factory());
//            List<SourcedJavaType> generated = new ArrayList<SourcedJavaType>(unitGenerator.generate(unitDescriptors));
//            Assert.assertEquals(generated.size(), 1);
//            ClassLoader classLoader = unitDescriptors.get(0).getClassLoader();
//            Class dynamicEntityClass = classLoader.loadClassesFromSourceDirectory(generated.get(0).getType().getName());
//            Assert.assertEquals(dynamicEntityClass.getName(), "org.abstractmeta.code.g.test.jpa.UsersEntity");
//
//        }
//
//    }
//
//
//    @BeforeTest
//    public void initConnection() throws SQLException {
//        File file = new File("target/").getAbsoluteFile();
//        if (!file.exists()) {
//            file.mkdirs();
//        }
//        String dbName = String.format("jdbc:h2:%s-test", new File(file, "db"));
//        final Connection connection = DriverManager.getConnection(dbName, "sa", "");
//        connection.createStatement().execute("CREATE TABLE USERS (id INT PRIMARY KEY, username VARCHAR(64), create_time TIMESTAMP);");
//        connectionProvider = new Provider<Connection>() {
//            public Connection get() {
//                return connection;
//            }
//
//
//        };
//    }
//
//
//    @AfterTest
//    public void destroyConnection() throws SQLException {
//        Connection connection = connectionProvider.get();
//        connection.createStatement().execute("DROP TABLE USERS");
//        connection.close();
//    }

}

        
