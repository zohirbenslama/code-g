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

import org.abstractmeta.code.g.code.JavaType;
import org.abstractmeta.code.g.code.JavaTypeRegistry;
import org.abstractmeta.code.g.core.builder.SimpleClassBuilder;
import org.abstractmeta.code.g.core.code.builder.JavaTypeBuilder;
import org.abstractmeta.code.g.core.extractor.AccessorFieldExtractor;
import org.abstractmeta.code.g.core.extractor.RegistryFieldExtractor;
import org.abstractmeta.code.g.core.internal.TypeNameWrapper;
import org.abstractmeta.code.g.config.Descriptor;
import org.abstractmeta.code.g.core.util.JavaTypeUtil;
import org.abstractmeta.code.g.extractor.MethodExtractor;
import org.abstractmeta.code.g.plugin.CodeGeneratorPlugin;

import java.util.Arrays;
import java.util.Collections;


/**
 * <p><b>Builder Generator Plugin</b></p>
 * <h2>Overview</h2>
 * <p>This plugin generates code implementation for a given interface, superclass. The generation process can be break down to</p>
 * <ul>
 * <li>Fields generation, all fields are extracted from source class' based on matching method names.
 * <ul>
 * <li>It extracts setter/getter fields. If source class has ony get method it assumes the field is immutable.</li>
 * <li>It extracts registry fields for registry like methods (register, isRegister, get, etc ...)</li>
 * </ul>
 * </li>
 * <li>Method generation - once all fields are defined and type is being generate the following
 * field handlers are fired with ability to generate relevant method
 * <ul>
 * <li>{@link org.abstractmeta.code.g.core.handler.field.RegistryFieldHandler}</li>
 * <li>{@link org.abstractmeta.code.g.core.handler.field.CollectionFieldHandler}</li>
 * <li>{@link org.abstractmeta.code.g.core.handler.field.GetterFieldHandler}</li>
 * <li>{@link org.abstractmeta.code.g.core.handler.type.SimpleTypeHandler}</li>
 * </ul>
 * </li>
 * <p/>
 * </ul>
 *
 * @author Adrian Witas
 */
public class ClassGeneratorPlugin extends AbstractGeneratorPlugin implements CodeGeneratorPlugin {


    public ClassGeneratorPlugin() {
        super(Arrays.asList(new RegistryFieldExtractor(), new AccessorFieldExtractor()), Collections.<MethodExtractor>emptyList());
    }

    @Override
    protected boolean isApplicable(JavaType sourceType) {
        return isExtractable(sourceType);
    }

    @Override
    protected JavaTypeBuilder generateType(JavaType sourceType, JavaTypeRegistry registry, String targetTypeName, Descriptor descriptor) {
        SimpleClassBuilder classBuilder = new SimpleClassBuilder(sourceType, descriptor);
        classBuilder.setSourceType(sourceType);
        classBuilder.addModifier("public").setTypeName(targetTypeName);
        if (!sourceType.getGenericTypeArguments().isEmpty()) {
            classBuilder.addGenericTypeArguments(sourceType.getGenericTypeArguments());
        }
        if ("class".equals(sourceType.getKind())) {
            classBuilder.setSuperType(new TypeNameWrapper(sourceType.getName(), sourceType.getGenericTypeArguments()));
        } else {
            classBuilder.addSuperInterface(new TypeNameWrapper(sourceType.getName(), sourceType.getGenericTypeArguments()));
        }
        addExtractableFields(classBuilder, sourceType);
        return classBuilder;
    }


    protected String getTargetTypeName(JavaType sourceType, Descriptor descriptor, JavaTypeRegistry registry) {
        String buildResultTypeName = JavaTypeUtil.matchDeclaringTypeName(sourceType);
        String buildResultSimpleClassName = JavaTypeUtil.getSimpleClassName(buildResultTypeName, true);
        buildResultSimpleClassName = buildResultSimpleClassName.replace(".", "");
        return getTargetTypeName(buildResultSimpleClassName, descriptor, registry);
    }


}
