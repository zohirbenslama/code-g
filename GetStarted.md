# Introduction #

A code generator generates code implementation for a given abstraction.
It could be either interface or abstract class or even regular class.
Abstraction matching and generation rules are defined in specialised plugin.


## Class generator plugin ##

This plugin provides implementation

### Accessor/mutator template design pattern ###
#### Immutable field support, builder pattern support ####
#### Use case ####
```
public interface View {
    
    int getId();
    Properties getVisualProperties();
    Properties getAttributes();
    void setAttributes(Properties properties);
    Map<Integer, View> getViews();
    List<String> getSuperViews();

}

```

To generate implementation and build for the following interface at runtime and compile

```

      File sourceCodeDirectory = new File("src/test/java");
        File targetSourceCodeDirectory = new File("target/generated-test-sources/code-g");
        UnitDescriptor descriptor = UnitDescriptorUtil.getUnitDescriptorByClasses(sourceCodeDirectory, targetSourceCodeDirectory, Arrays.asList(View.class.getName()),
                ClassGenerator.class,
                "generateBuilder", "true");

        CodeUnitGenerator unitGenerator = new CodeUnitGeneratorImpl();
        GeneratedCode generatedCode = unitGenerator.generate(descriptor);
 
```

**Code-g generator** provides the following implementation

```

package com.abstractmeta.code.g.impl;

import com.abstractmeta.code.g.View;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class ViewImpl implements View {
    private final List<String> superViews;
    private Properties attributes;
    private final Map<Integer, View> views;
    private final int id;
    private final Properties visualProperties;

    public ViewImpl(List<String> superViews, Map<Integer, View> views, int id, Properties visualProperties) {
        super();
        this.superViews = superViews;
        this.views = views;
        this.id = id;
        this.visualProperties = visualProperties;
    }

    public List<String> getSuperViews() {
        return this.superViews;
    }

    public void setAttributes(Properties attributes) {
        this.attributes = attributes;
    }

    public Properties getAttributes() {
        return this.attributes;
    }

    public Map<Integer, View> getViews() {
        return this.views;
    }

    public int getId() {
        return this.id;
    }

    public Properties getVisualProperties() {
        return this.visualProperties;
    }

}

```


**Code-g generator** provides the following builder

```


import com.abstractmeta.code.g.View;
import com.abstractmeta.code.g.impl.ViewImpl;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class ViewImplBuilder {
    private List<String> superViews = new ArrayList<String>();
    private Properties attributes = new Properties();
    private Map<Integer, View> views = new HashMap<Integer, View>();
    private int id;
    private Properties visualProperties = new Properties();


    public ViewImplBuilder addSuperViews(String ... superViews) {
        Collections.addAll(this.superViews, superViews);
        return this;
    }

    public ViewImplBuilder addSuperViews(List<String> superViews) {
        this.superViews.addAll(superViews);
        return this;
    }

    public ViewImplBuilder clearSuperViews() {
        this.superViews.clear();
        return this;
    }

    public ViewImplBuilder setSuperViews(List<String> superViews) {
        this.superViews = superViews;
        return this;
    }

    public List<String> getSuperViews() {
      return this.superViews;
    }

    public ViewImplBuilder addAttributes(Properties attributes) {
        this.attributes.putAll(attributes);
        return this;
    }

    public ViewImplBuilder addAttribute(Object key, Object value) {
        this.attributes.put(key, value);
        return this;
    }

    public ViewImplBuilder clearAttributes() {
        this.attributes.clear();
        return this;
    }

    public void setAttributes(Properties attributes) {
        this.attributes = attributes;
    }

    public Properties getAttributes() {
        return this.attributes;
    }

    public ViewImplBuilder addViews(Map<Integer, View> views) {
        this.views.putAll(views);
        return this;
    }

    public ViewImplBuilder addView(Integer key, View value) {
        this.views.put(key, value);
        return this;
    }

    public ViewImplBuilder clearViews() {
        this.views.clear();
        return this;
    }

    public ViewImplBuilder setViews(Map<Integer, View> views) {
        this.views = views;
        return this;
    }

    public Map<Integer, View> getViews() {
        return this.views;
    }

    public ViewImplBuilder setId(int id) {
        this.id = id;
        return this;
    }

    public int getId() {
        return this.id;
    }

    public ViewImplBuilder addVisualProperties(Properties visualProperties) {
        this.visualProperties.putAll(visualProperties);
        return this;
    }

    public ViewImplBuilder addVisualPropert(Object key, Object value) {
        this.visualProperties.put(key, value);
        return this;
    }

    public ViewImplBuilder clearVisualProperties() {
        this.visualProperties.clear();
        return this;
    }

    public ViewImplBuilder setVisualProperties(Properties visualProperties) {
        this.visualProperties = visualProperties;
        return this;
    }

    public Properties getVisualProperties() {
        return this.visualProperties;
    }

    public View build() {
        View result = new ViewImpl(superViews, views, id, visualProperties);
        result.setAttributes(attributes);
        return result;
    }

    public ViewImplBuilder merge(View instance) {
        if(instance.getSuperViews() != null) {
            this.addSuperViews(instance.getSuperViews());
        }
        if(instance.getAttributes() != null) {
            this.addAttributes(instance.getAttributes());
        }
        if(instance.getViews() != null) {
            this.addViews(instance.getViews());
        }
        this.setId(instance.getId());
        if(instance.getVisualProperties() != null) {
            this.addVisualProperties(instance.getVisualProperties());
        }
        return this;
    }

    public ViewImplBuilder merge(ViewImplBuilder instance) {
        if(instance.getSuperViews() != null) {
            this.addSuperViews(instance.getSuperViews());
        }
        if(instance.getAttributes() != null) {
            this.addAttributes(instance.getAttributes());
        }
        if(instance.getViews() != null) {
            this.addViews(instance.getViews());
        }
        this.setId(instance.getId());
        if(instance.getVisualProperties() != null) {
            this.addVisualProperties(instance.getVisualProperties());
        }
        return this;
    }

}

```

### Registry design patterns ###

#### Use cases ####

##  ##
```

public interface ViewRegistry {
    void register(View view);
    void unregister(View view);
    View get(int id);
    Map<Integer, View> getRegistry();
}

```


```
   File sourceCodeDirectory = new File("src/test/java");
        File targetSourceCodeDirectory = new File("target/generated-test-sources/code-g");
        UnitDescriptor descriptor = UnitDescriptorUtil.getUnitDescriptorByClasses(sourceCodeDirectory, targetSourceCodeDirectory,
                Arrays.asList(ViewRegistry.class.getName()), ClassGenerator.class);
        CodeUnitGenerator unitGenerator = new CodeUnitGeneratorImpl();
        GeneratedCode generatedCode = unitGenerator.generate(descriptor);
```

**Code-g generator** provides the following implementation

```
package com.abstractmeta.code.g.helper.impl;

import com.abstractmeta.code.g.View;
import com.abstractmeta.code.g.helper.ViewRegistry;
import java.util.Map;

public class ViewRegistryImpl implements ViewRegistry {
    private final Map<Integer, View> registry;

    public ViewRegistryImpl(Map<Integer, View> registry) {
        super();
        this.registry = registry;
    }

    public Map<Integer, View> getRegistry() {
        return this.registry;
    }

    public void register(View argument0) {
        registry.put(argument0.getId(), argument0);
    }

    public void unregister(View argument0) {
        registry.remove(argument0.getId());
    }

    public View get(int argument0) {
        return registry.get(argument0);
    }

}
```



### JPA class generator ###

#### Use cases ####

Given the following table definition

```
CREATE TABLE USERS (id INT PRIMARY KEY, username VARCHAR(64), date_of_birth date, create_time TIMESTAMP);
```
```
CREATE TABLE ACCOUNTS (id INT PRIMARY KEY, user_id int, status tinyint(1));
```

```
 File sourceCodeDirectory = new File("src/testSimpleClass/java");

            File targetSourceCodeDirectory = new File("target/generated-test-sources/code-g");
            String dbUrl = String.format("jdbc:h2:%s-test", dbFileName);
            UnitDescriptor descriptor = UnitDescriptorUtil.getUnitDescriptorByClasses(sourceCodeDirectory,
                    targetSourceCodeDirectory, Arrays.<String>asList(), JpaClassGenerator.class,
                    "connection.url", dbUrl,
                    "targetPackage", "org.abstractmeta.test.jpa",
                    "connection.username", "sa",
                    "tableNames", "USERS,ACCOUNTS"
            );
            CodeUnitGenerator unitGenerator = new CodeUnitGeneratorImpl();
            GeneratedCode generatedCode = unitGenerator.generate(descriptor);

```

**Code-g generator** provides the following implementation

```

package org.abstractmeta.test.jpa.persistence;

import java.sql.Date;
import java.sql.Timestamp;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
@Table(name = "USERS")
@Entity(name = "User")

public class UserEntity {
    @Id()
    @Column(name = "ID", nullable = false)
    private int id;
    @Basic()
    @Column(name = "USERNAME")
    private String username;
    @Basic()
    @Column(name = "DATE_OF_BIRTH")
    private Date dateOfBirth;
    @Basic()
    @Column(name = "CREATE_TIME")
    private Timestamp createTime;

    public UserEntity() {
        super();
    }

    public void setId(int id) {
        this.id = id;
    }

    @Id()
    @Column(name = "ID", nullable = false)
    public int getId() {
        return this.id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Basic()
    @Column(name = "USERNAME")
    public String getUsername() {
        return this.username;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Basic()
    @Column(name = "DATE_OF_BIRTH")
    public Date getDateOfBirth() {
        return this.dateOfBirth;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Basic()
    @Column(name = "CREATE_TIME")
    public Timestamp getCreateTime() {
        return this.createTime;
    }

}

```

```

package org.abstractmeta.test.jpa.persistence;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
@Table(name = "ACCOUNTS")
@Entity(name = "Account")

public class AccountEntity {
    @Id()
    @Column(name = "ID", nullable = false)
    private int id;
    @Basic()
    @Column(name = "USER_ID")
    private Integer userId;
    @Basic()
    @Column(name = "STATUS")
    private Short status;

    public AccountEntity() {
        super();
    }

    public void setId(int id) {
        this.id = id;
    }

    @Id()
    @Column(name = "ID", nullable = false)
    public int getId() {
        return this.id;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Basic()
    @Column(name = "USER_ID")
    public Integer getUserId() {
        return this.userId;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    @Basic()
    @Column(name = "STATUS")
    public Short getStatus() {
        return this.status;
    }

}

```


### Maven plugin configuration support ###

Code-g can be implemented as part of maven build process

```

    <plugin>
                <groupId>org.abstractmeta</groupId>
                <artifactId>code-g-maven-plugin</artifactId>
                <version>1.0.0</version>
                <configuration>
                    <descriptors>
                        <descriptor>
                            <sourceMatcher>
                                <packageNames>org.abstractmeta.code.g.examples.model1</packageNames>
                                <includeSubpackages>true</includeSubpackages>
                            </sourceMatcher>
                            <generatorClass>org.abstractmeta.code.g.core.generator.ClassGenerator</generatorClass>
                            <properties>
                                <generateBuilder>true</generateBuilder>
                            </properties>
                        </descriptor>
                    </descriptors>
                </configuration>
                <executions>
                    <execution>
                        <goals>
                            <goal>compile</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>


```


or you can alternatively user properties descriptor file
src/main/code-g/unit.properties
```
descriptors_1.sourceMatcher.packageNames = org.abstractmeta.code.g.examples.model4
descriptors_1.sourceMatcher.includeSubpackages = true
descriptors_1.generatorClass = org.abstractmeta.code.g.core.generator.ClassGenerator
descriptors_1.properties.generateBuilder = true
```







## Extend and build your one class generator ##


### Reflectify ###
Generates fast reflection code at real time
[reflectify-protocol](https://code.google.com/p/reflectify-protocol/)

### Commandify ###
Generates implementation with command pattern to track object changes
[commandify](https://code.google.com/p/commandify/)


## API configuration details ##

  * Runtime generation and compilation
  * Convenient code builder  [JavaTypeBuilder](http://code-g.googlecode.com/svn/code-g-api/trunk/src/main/java/org/abstractmeta/code/g/code/JavaTypeBuilder.java)
  * Generic support

### Configuration ###

  * [UnitDescriptor](http://code-g.googlecode.com/svn/code-g-api/trunk/src/main/java/org/abstractmeta/code/g/config/UnitDescriptor.java) represents  collection of descriptor.


  * Specialized class generators [ClassGenerator](http://code-g.googlecode.com/svn/code-g-core/trunk/src/main/java/org/abstractmeta/code/g/core/generator/ClassGenerator.java),
[JpaClassGenerator](http://code-g.googlecode.com/svn/code-g-core/trunk/src/main/java/org/abstractmeta/code/g/core/generator/JpaClassGenerator.java),
[BuilderGenerator](http://code-g.googlecode.com/svn/code-g-core/trunk/src/main/java/org/abstractmeta/code/g/core/generator/BuilderGenerator.java)
use generic [Descriptor](http://code-g.googlecode.com/svn/code-g-api/trunk/src/main/java/org/abstractmeta/code/g/config/Descriptor.java)