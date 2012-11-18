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
package org.abstractmeta.code.g.core.handler.type;

import com.google.common.collect.Iterables;
import org.abstractmeta.code.g.code.JavaConstructor;
import org.abstractmeta.code.g.code.JavaField;
import org.abstractmeta.code.g.code.JavaType;
import org.abstractmeta.code.g.config.Descriptor;
import org.abstractmeta.code.g.core.builder.SimpleClassBuilder;
import org.abstractmeta.code.g.core.code.builder.JavaConstructorBuilder;
import org.abstractmeta.code.g.core.code.builder.JavaTypeBuilder;
import org.abstractmeta.code.g.core.collection.predicates.ConstructorArgumentPredicate;
import org.abstractmeta.code.g.core.util.DescriptorUtil;
import org.abstractmeta.code.g.core.util.JavaTypeUtil;
import org.abstractmeta.code.g.core.util.ReflectUtil;
import org.abstractmeta.code.g.handler.JavaPluginHandler;
import org.abstractmeta.code.g.handler.JavaTypeHandler;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * This handler creates constructor for all fields defined on the owner type.
 * In case where all fields are mutable the empty constructor is added.
 * If builder was not able to generated all methods from source class class is marked as abstract.
 *
 * @author Adrian Witas
 */
public class SimpleTypeHandler implements JavaTypeHandler {

    private final JavaTypeBuilder ownerTypeBuilder;
    private final Descriptor descriptor;

    public SimpleTypeHandler(JavaTypeBuilder ownerTypeBuilder, Descriptor descriptor) {
        this.ownerTypeBuilder = ownerTypeBuilder;
        this.descriptor = descriptor;
    }


    @Override
    public void handle(JavaType sourceType) {
        JavaPluginHandler javaPluginHandler = DescriptorUtil.loadInstance(descriptor, JavaPluginHandler.class, SimpleClassBuilder.CHANGE_HANDLER);
        if (javaPluginHandler != null) {
            javaPluginHandler.handle(descriptor, ownerTypeBuilder);
        }
        if (!hasConstructors()) {
            Map<String, String> fieldArgumentMap = new HashMap<String, String>();
            for (JavaField field : ownerTypeBuilder.getFields()) {
                fieldArgumentMap.put(field.getName(), field.getName());
            }
            JavaConstructor javaConstructor = JavaTypeUtil.buildDefaultConstructor(ownerTypeBuilder, fieldArgumentMap);
            if (javaPluginHandler != null) {
                javaPluginHandler.handle(descriptor, ownerTypeBuilder, javaConstructor);
            }

            ownerTypeBuilder.addConstructor(javaConstructor);
        }
    }

    protected boolean hasConstructors() {
        return ownerTypeBuilder.getConstructors().size() > 0;
    }


}
