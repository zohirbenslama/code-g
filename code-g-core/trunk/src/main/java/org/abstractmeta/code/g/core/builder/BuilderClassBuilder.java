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
package org.abstractmeta.code.g.core.builder;

import org.abstractmeta.code.g.code.JavaType;
import org.abstractmeta.code.g.core.code.builder.JavaTypeBuilder;
import org.abstractmeta.code.g.core.handler.*;

import java.util.HashMap;
import java.util.Map;

/**
 * BuilderClassBuilder
 *
 * @author Adrian Witas
 */
public class BuilderClassBuilder extends JavaTypeBuilder {

    private final Map<String, String> immutableImplementation;
    private final JavaType buildType;
    
    public BuilderClassBuilder(JavaType builtType) {
        super();
        this.buildType = builtType;
        this.immutableImplementation = new HashMap<String, String>();
        addFieldHandler(new BuilderSetterFieldHandler(this));
        addFieldHandler(new BuilderCollectionFieldHandler(this));
        addFieldHandler(new BuilderMapFieldHandler(this));
        addFieldHandler(new BuilderArrayFieldHandler(this));
        addFieldHandler(new GetterFieldHandler(this));
        addFieldHandler(new HasFieldHandler(this));

        addTypeHandler(new BuilderTypeHandler(this, immutableImplementation));
        addTypeHandler(new BuilderMergeHandler(this));
    }

    public JavaType getBuildType() {
        return buildType;
    }

    public void addImmutableImplementation(String source, String target) {
        immutableImplementation.put(source, target);
    }
}
