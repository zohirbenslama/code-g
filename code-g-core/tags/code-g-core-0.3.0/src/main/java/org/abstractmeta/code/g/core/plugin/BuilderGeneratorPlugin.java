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

import org.abstractmeta.code.g.code.JavaField;
import org.abstractmeta.code.g.code.JavaType;
import org.abstractmeta.code.g.code.JavaTypeRegistry;
import org.abstractmeta.code.g.core.builder.BuilderClassBuilder;
import org.abstractmeta.code.g.core.code.builder.JavaFieldBuilder;
import org.abstractmeta.code.g.core.code.builder.JavaTypeBuilder;
import org.abstractmeta.code.g.config.Descriptor;
import org.abstractmeta.code.g.core.util.JavaTypeUtil;
import org.abstractmeta.code.g.core.util.ReflectUtil;
import org.abstractmeta.code.g.plugin.CodeGeneratorPlugin;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * <p><b>Builder Generator Plugin</b></p>
 * <h2>Overview</h2>
 * <p>This plugin generates code implementation for the builder. The generation process can be break down to</p>
 * <ul>
 * <li>Fields generation, all fields are extracted from source class' fields.
 * <ul>
 * <li>all fields are mutable</li>
 * <li>for each field additional starting with '_' is added to track whether original field was mutated</li>
 * </ul>
 * </li>
 * <li>Method generation - once all fields are defined and type is being generate the following
 * field handlers are fired with ability to generate relevant method
 * <ul>
 * <li>{@link org.abstractmeta.code.g.core.handler.BuilderSetterFieldHandler}</li>
 * <li>{@link org.abstractmeta.code.g.core.handler.BuilderCollectionFieldHandler}</li>
 * <li>{@link org.abstractmeta.code.g.core.handler.BuilderMapFieldHandler}</li>
 * <li>{@link org.abstractmeta.code.g.core.handler.BuilderArrayFieldHandler}</li>
 * <li>{@link org.abstractmeta.code.g.core.handler.GetterFieldHandler}</li>
 * <li>{@link org.abstractmeta.code.g.core.handler.HasFieldHandler}</li>
 * <li>{@link org.abstractmeta.code.g.core.handler.BuilderTypeHandler}</li>
 * <li>{@link org.abstractmeta.code.g.core.handler.BuilderMergeHandler}</li>
 * </ul>
 * </li>
 * </ul>
 * </p>
 *
 * @author Adrian Witas
 */
public class BuilderGeneratorPlugin extends AbstractGeneratorPlugin implements CodeGeneratorPlugin {

    @Override
    protected boolean isApplicable(JavaType sourceType) {
        List<JavaField> fields = sourceType.getFields();
        return  ! ((sourceType.getModifiers().contains("abstract") || fields.size() == 0)
                || (fields.size() == 1 && fields.get(0).getName().equals("registry")));
    }

    @Override
    protected JavaTypeBuilder generateType(JavaType sourceType, JavaTypeRegistry registry, String targetTypeName, Descriptor descriptor) {
        BuilderClassBuilder builderClassBuilder = new BuilderClassBuilder(sourceType);
        Map<String, String> immutableImplementation = descriptor.getImmutableImplementation();
        if (immutableImplementation != null) {
            for (String key : immutableImplementation.keySet()) {
                builderClassBuilder.addImmutableImplementation(key, immutableImplementation.get(key));
            }
        }
        builderClassBuilder.setSourceType(sourceType);
        builderClassBuilder.addModifier("public").setTypeName(targetTypeName);
        for (JavaField field : sourceType.getFields()) {
            JavaFieldBuilder fieldBuilder = new JavaFieldBuilder();
            fieldBuilder.addModifier("private");
            fieldBuilder.setType(field.getType());
            fieldBuilder.setName(field.getName());
            builderClassBuilder.addField(fieldBuilder.build());

            JavaFieldBuilder trackerFieldBuilder = new JavaFieldBuilder();
            trackerFieldBuilder.addModifier("private");
            trackerFieldBuilder.setType(boolean.class);
            trackerFieldBuilder.setName("_" + field.getName());
            builderClassBuilder.addField(trackerFieldBuilder.build());
        }

        return builderClassBuilder;
    }



    protected String getTargetTypeName(JavaType sourceType, Descriptor descriptor, JavaTypeRegistry registry) {
        String buildResultTypeName = JavaTypeUtil.getSuperTypeName(sourceType);
        String buildResultSimpleClassName = JavaTypeUtil.getSimpleClassName(buildResultTypeName, true);
        return getTargetTypeName(buildResultSimpleClassName, descriptor, registry);
    }
    
   
}
