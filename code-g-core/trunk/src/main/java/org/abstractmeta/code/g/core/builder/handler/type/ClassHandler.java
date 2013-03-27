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
package org.abstractmeta.code.g.core.builder.handler.type;

import org.abstractmeta.code.g.code.JavaConstructor;
import org.abstractmeta.code.g.code.JavaField;
import org.abstractmeta.code.g.code.JavaType;
import org.abstractmeta.code.g.code.JavaTypeBuilder;
import org.abstractmeta.code.g.code.handler.TypeHandler;
import org.abstractmeta.code.g.core.util.JavaTypeUtil;
import org.abstractmeta.code.g.generator.Context;

import java.util.HashMap;
import java.util.Map;

/**
 * This handle creates  a constructor for all fields defined on the owner type.
 * In case where all fields are mutable the empty constructor is added.
 *
 *
 * If it is not possible to generated all methods from source class class is marked as abstract.
 *
 * @author Adrian Witas
 */
public class ClassHandler implements TypeHandler {

    @Override
    public void handle(JavaTypeBuilder owner, Context context) {
        if (! hasConstructors(owner)) {
            JavaType sourceType = owner.getSourceType();
            Map<String, String> fieldArgumentMap = new HashMap<String, String>();
            for (JavaField field : sourceType.getFields()) {
                fieldArgumentMap.put(field.getName(), field.getName());
            }
            for (JavaField field : owner.getFields()) {
                fieldArgumentMap.put(field.getName(), field.getName());
            }
            JavaConstructor javaConstructor = JavaTypeUtil.buildDefaultConstructor(owner, sourceType, fieldArgumentMap);
            owner.addConstructor(javaConstructor);
        }
    }

    protected boolean hasConstructors(JavaTypeBuilder owner) {
        return owner.getConstructors().size() > 0;
    }

}
