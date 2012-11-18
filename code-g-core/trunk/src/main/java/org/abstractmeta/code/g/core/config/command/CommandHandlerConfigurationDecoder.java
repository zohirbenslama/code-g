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
 * Represents  CommandHandlerConfigurationDecoder.
 *
 * @author Adrian Witas
 */
public class CommandHandlerConfigurationDecoder {

    private final CommandConfigurationDecoder configurationDecoder;

    public CommandHandlerConfigurationDecoder() {
        this(new CommandConfigurationDecoder());
    }

    public CommandHandlerConfigurationDecoder(CommandConfigurationDecoder configurationDecoder) {
        this.configurationDecoder = configurationDecoder;
    }


    /**
     * Decodes option map into CommandHandlerConfiguration
     * <p>
     * The following options are supported
     * <ul>
     * <li>interface - command manager interface</li>
     * <li>ownerField.name - field name to store commands (transient field will be added to target class)</li>
     * <li>ownerField.type  - type of commands field</li>
     * <li>ownerField.type.implementation - implementation class of command filed</li>
     * <li>ownerField.type.implementation - implementation class of command filed</li>
     * <li>implementationKind - method (default) method based command implementation, class - public static class based implementation (this makes sens only for filed) </li>
     * <li>fieldCommand.xxx - configuration for command configuration see {@link CommandConfiguration}</li>
     * </ul>
     * </p>
     *
     * @param options map
     * @return CommandHandlerConfiguration
     */
    public CommandHandlerConfiguration decode(Map<String, String> options) {
        CommandHandlerConfiguration result = new CommandHandlerConfiguration();
        result.setInterface(DecoderUtil.readStringRequired(options, "interface"));
        result.setFieldName(DecoderUtil.readStringRequired(options, "ownerField.name"));
        result.setFieldType(DecoderUtil.readStringRequired(options, "ownerField.type"));
        result.setFieldTypeImplementation(DecoderUtil.readStringRequired(options, "ownerField.type.implementation"));
        result.setImplementationKind(DecoderUtil.readEnum(options, CommandHandlerConfiguration.ImplementationKind.class, "implementationKind", "method"));

        Map<String, String> fieldCommand = DecoderUtil.matchWithPrefix(options, "fieldCommand");
        result.setFieldCommand(configurationDecoder.decode(fieldCommand));
        return result;
    }
}

        
