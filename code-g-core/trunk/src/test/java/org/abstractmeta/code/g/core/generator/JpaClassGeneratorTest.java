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
package org.abstractmeta.code.g.core.generator;

import org.abstractmeta.code.g.code.CompiledJavaType;
import org.abstractmeta.code.g.code.JavaTypeRegistry;
import org.abstractmeta.code.g.config.Descriptor;
import org.abstractmeta.code.g.core.code.JavaTypeRegistryImpl;
import org.abstractmeta.code.g.core.config.DescriptorImpl;
import org.abstractmeta.code.g.core.config.SourceMatcherImpl;
import org.abstractmeta.code.g.generator.CodeGenerator;
import org.abstractmeta.code.g.generator.Context;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import javax.inject.Provider;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * This class test jpa entity generator plugin.
 */
@Test
public class JpaClassGeneratorTest {

        private static Provider<Connection> connectionProvider;




    public void testSimpleClassGenerator() throws Exception {
        CodeGenerator classGenerator = new ClassGenerator();
        Context context = getContextForTestSimpleClassGenerator();
        List<CompiledJavaType> result = classGenerator.generate(context);
        Assert.assertEquals(result.size(), 1);
        Class type = result.get(0).getCompiledType();
        Assert.assertEquals(type, "");
    }

    protected Context getContextForTestSimpleClassGenerator(){
        DescriptorImpl descriptor = new DescriptorImpl();
        Properties properties = new Properties();
        File file = new File("target/").getAbsoluteFile();
        String dbName = String.format("jdbc:h2:%s-test", new File(file, "db"));
        properties.setProperty("connection.username" , "db");
        properties.setProperty("connection.password" , "db");
        properties.setProperty("connection.url" , dbName);
        properties.setProperty("tableNames", "USERS");
        descriptor.setProperties(properties);
        Context context = new ContextImpl();
        context.put(Descriptor.class, descriptor);
        context.put(JavaTypeRegistry.class, new JavaTypeRegistryImpl());
        return context;
    }

    @BeforeTest
    public void initConnection() throws SQLException {
        File file = new File("target/").getAbsoluteFile();
        if (!file.exists()) {
            file.mkdirs();
        }
        String dbName = String.format("jdbc:h2:%s-test", new File(file, "db"));
        final Connection connection = DriverManager.getConnection(dbName, "sa", "dev");
        connection.createStatement().execute("CREATE TABLE USERS (id INT PRIMARY KEY, username VARCHAR(64), create_time TIMESTAMP);");
        connectionProvider = new Provider<Connection>() {
            public Connection get() {
                return connection;
            }


        };
    }


    @AfterTest
    public void destroyConnection() throws SQLException {
        Connection connection = connectionProvider.get();
        connection.createStatement().execute("DROP TABLE USERS");
        connection.close();
    }

}

        
