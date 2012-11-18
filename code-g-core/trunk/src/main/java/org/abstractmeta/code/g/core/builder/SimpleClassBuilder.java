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
import org.abstractmeta.code.g.config.Descriptor;
import org.abstractmeta.code.g.core.code.builder.JavaTypeBuilder;
import org.abstractmeta.code.g.core.handler.field.CollectionFieldHandler;
import org.abstractmeta.code.g.core.handler.field.GetterFieldHandler;
import org.abstractmeta.code.g.core.handler.field.RegistryFieldHandler;
import org.abstractmeta.code.g.core.handler.field.SetterFieldHandler;
import org.abstractmeta.code.g.core.handler.type.EqualMethodHandler;
import org.abstractmeta.code.g.core.handler.type.HashCodeMethodHandler;
import org.abstractmeta.code.g.core.handler.type.SimpleTypeHandler;

/**
 * SimpleClassBuilder
 *
 * @author Adrian Witas
 */
public class SimpleClassBuilder extends JavaTypeBuilder {

    public static final String CHANGE_HANDLER = "commandHandler";


    public SimpleClassBuilder(JavaType sourceType, Descriptor descriptor) {
        super();
        setSourceType(sourceType);
        addFieldHandler(new RegistryFieldHandler(this, descriptor));
        addFieldHandler(new CollectionFieldHandler(this, descriptor));
        addFieldHandler(new SetterFieldHandler(this, descriptor));
        addFieldHandler(new GetterFieldHandler(this,descriptor));
        addTypeHandler(new HashCodeMethodHandler(this, descriptor));
        addTypeHandler(new EqualMethodHandler(this, descriptor));
        addTypeHandler(new SimpleTypeHandler(this, descriptor));
    }

}
