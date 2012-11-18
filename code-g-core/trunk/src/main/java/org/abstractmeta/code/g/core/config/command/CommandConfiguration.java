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

import java.util.List;

/**
* Represents  CommandConfiguration.
* See {@link CommandConfigurationDecoder} for more details.
 *
* @author Adrian Witas
*/
public class CommandConfiguration {
    private String superTypeName;
    private List<String> superTypeGenericTypeSources;
    private List<String> superTypeConstructorArguments;
    private String valueFieldName;
    private String idAnnotation;


    public String getSuperTypeName() {
        return superTypeName;
    }

    public void setSuperTypeName(String superTypeName) {
        this.superTypeName = superTypeName;
    }

    public List<String> getSuperTypeGenericTypeSources() {
        return superTypeGenericTypeSources;
    }

    public void setSuperTypeGenericTypeSources(List<String> superTypeGenericTypeSources) {
        this.superTypeGenericTypeSources = superTypeGenericTypeSources;
    }

    public List<String> getSuperTypeConstructorArguments() {
        return superTypeConstructorArguments;
    }

    public void setSuperTypeConstructorArguments(List<String> superTypeConstructorArgumentNames) {
        this.superTypeConstructorArguments = superTypeConstructorArgumentNames;
    }

    public String getIdAnnotation() {
        return idAnnotation;
    }

    public void setIdAnnotation(String idAnnotation) {
        this.idAnnotation = idAnnotation;
    }

    public String getValueFieldName() {
        return valueFieldName;
    }

    public void setValueFieldName(String valueFieldName) {
        this.valueFieldName = valueFieldName;
    }
}

        
