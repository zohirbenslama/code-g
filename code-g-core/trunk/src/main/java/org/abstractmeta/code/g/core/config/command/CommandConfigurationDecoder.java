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
package org.abstractmeta.code.g.core.config.command;

import org.abstractmeta.code.g.core.util.DecoderUtil;

import java.util.Map;

/**
 * Represents  CommandConfigurationDecoder.
 *
 * @author Adrian Witas
 */
public class CommandConfigurationDecoder {

    /**
     * Decodes options into CommandConfiguration
     * <p>The following options are supported
     * <ul>
     *     <li>superType - command type super class (required)</li>
     *     <li>superType.genericTypeSources - coma separated list of generic types sources (ownerType, fieldType)</li>
     *     <li>superType.constructorArguments - coma separated list of constructor arguments (ownerType, fieldType, fieldName, previousValue, value, argument, isId)</li>
     *     <li>valueFieldName - field name which stores field value - optional (default value)</li>
     *     <li>idAnnotation - id annotation default  javax.persistence.Id</li>
     * </ul>
     * </p>
     *
     * @param options map
     * @return CommandConfiguration
     */
    public CommandConfiguration decode(Map<String, String> options)  {
        CommandConfiguration result = new CommandConfiguration();
        result.setSuperTypeName(DecoderUtil.readStringRequired(options, "superType"));
        result.setSuperTypeGenericTypeSources(DecoderUtil.readStringList(options, "superType.genericTypeSources"));
        result.setSuperTypeConstructorArguments(DecoderUtil.readStringList(options, "superType.constructorArguments"));
        result.setValueFieldName(DecoderUtil.readString(options, "valueFieldName", "value"));
        result.setIdAnnotation(DecoderUtil.readString(options, "idAnnotation", javax.persistence.Id.class.getName()));
        return result;

    }
}

        
