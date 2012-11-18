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

/**
 * Represents  CommandHandlerConfiguration.
 * See {@link CommandHandlerConfigurationDecoder} for more details.
 *
 * @author Adrian Witas
 */
public class CommandHandlerConfiguration {

    private String iface;
    private String fieldName;
    private String fieldType;
    private String fieldTypeImplementation;
    private CommandConfiguration fieldCommand;
    private ImplementationKind implementationKind;

    public String getInterface() {
        return iface;
    }

    public void setInterface(String iface) {
        this.iface = iface;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getFieldTypeImplementation() {
        return fieldTypeImplementation;
    }

    public void setFieldTypeImplementation(String fieldTypeImplementation) {
        this.fieldTypeImplementation = fieldTypeImplementation;
    }

    public CommandConfiguration getFieldCommand() {
        return fieldCommand;
    }

    public void setFieldCommand(CommandConfiguration fieldCommand) {
        this.fieldCommand = fieldCommand;
    }

    public ImplementationKind getImplementationKind() {
        return implementationKind;
    }

    public void setImplementationKind(ImplementationKind implementationKind) {
        this.implementationKind = implementationKind;
    }

    public static enum ImplementationKind {
        CLASS, METHOD
    }

}