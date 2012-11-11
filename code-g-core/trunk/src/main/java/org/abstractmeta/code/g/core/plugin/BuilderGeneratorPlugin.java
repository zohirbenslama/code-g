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
import org.abstractmeta.code.g.core.util.StringUtil;
import org.abstractmeta.code.g.plugin.CodeGeneratorPlugin;

import java.util.Collections;
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
 * <li>{@link org.abstractmeta.code.g.core.handler.IsFieldPresentHandler}</li>
 * <li>{@link org.abstractmeta.code.g.core.handler.BuilderTypeHandler}</li>
 * <li>{@link org.abstractmeta.code.g.core.handler.BuilderMergeHandler}</li>
 * </ul>
 * </li>
 * </ul>
 *
 * </p>
 * <p>Builder options</p>
 * <ul>
 *     <li>skipIsPresentMethod - skips adding 'is present' field and method for each non transient field </li>
 * </ul>
 *
 * @author Adrian Witas
 */
public class BuilderGeneratorPlugin extends AbstractGeneratorPlugin implements CodeGeneratorPlugin {

    public static final String SKIP_PRESENT_METHOD = "skipIsPresentMethod";

    @Override
    protected boolean isApplicable(JavaType sourceType) {
        List<JavaField> fields = sourceType.getFields();
        return !((sourceType.getModifiers().contains("abstract")
                || "enum".equals(sourceType.getKind())
                || fields.size() == 0)
                || (fields.size() == 1 && fields.get(0).getName().equals("registry")));
    }

    @Override
    protected JavaTypeBuilder generateType(JavaType sourceType, JavaTypeRegistry registry, String targetTypeName, Descriptor descriptor) {
        Map<String, String> options = getOptions();
        if (options == null) options = Collections.emptyMap();
        String skipIsPresentMethod = options.get(SKIP_PRESENT_METHOD);
        if (skipIsPresentMethod == null) skipIsPresentMethod = "false";
        boolean buildIsPresentMethod = ("false".equalsIgnoreCase(skipIsPresentMethod));
        BuilderClassBuilder builderClassBuilder = new BuilderClassBuilder(sourceType, buildIsPresentMethod);
        Map<String, String> immutableImplementation = descriptor.getImmutableImplementation();
        if (immutableImplementation != null) {
            for (String key : immutableImplementation.keySet()) {
                builderClassBuilder.addImmutableImplementation(key, immutableImplementation.get(key));
            }
        }
        builderClassBuilder.setSourceType(sourceType);
        builderClassBuilder.addModifier("public").setTypeName(targetTypeName);
        builderClassBuilder.addGenericTypeArguments(sourceType.getGenericTypeArguments());
        for (JavaField field : sourceType.getFields()) {
            JavaFieldBuilder fieldBuilder = new JavaFieldBuilder();
            fieldBuilder.addModifier("private");
            fieldBuilder.setType(field.getType());
            fieldBuilder.setName(field.getName());
            builderClassBuilder.addField(fieldBuilder.build());
            if (buildIsPresentMethod) {
                JavaFieldBuilder trackerFieldBuilder = new JavaFieldBuilder();
                trackerFieldBuilder.addModifier("private").addModifier("transient");
                trackerFieldBuilder.setType(boolean.class);
                trackerFieldBuilder.setName(StringUtil.isPresentFieldName(field.getName()));
                builderClassBuilder.addField(trackerFieldBuilder.build());
            }
        }
        return builderClassBuilder;
    }


    protected String getTargetTypeName(JavaType sourceType, Descriptor descriptor, JavaTypeRegistry registry) {
        String buildResultTypeName = JavaTypeUtil.matchDeclaringTypeName(sourceType);
        String buildResultSimpleClassName = JavaTypeUtil.getSimpleClassName(buildResultTypeName, true);
        buildResultSimpleClassName = buildResultSimpleClassName.replace(".", "");
        return getTargetTypeName(buildResultSimpleClassName, descriptor, registry);
    }




}
