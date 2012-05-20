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
package org.abstractmeta.code.g.core.config;


import org.abstractmeta.code.g.config.Descriptor;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Default descriptor implementation.
 *
 * @author Adrian Witas
 */

public class DescriptorImpl implements Descriptor {

    private String sourcePackage;
    private String sourceClass;
    private String targetPackage;
    private String targetPrefix;
    private String targetPostfix;
    private String superType;
    private String interfaces;
    private Set<String> exclusions;
    private Set<String> inclusions;
    private Map<String, String> immutableImplementation;

    private String plugin;
    private List<String> compilationSources;
    private Map<String, String> options;

    public DescriptorImpl() {
    }

    public DescriptorImpl(String sourcePackage, String sourceClass, String targetPackage, String targetPrefix, String targetPostfix, String superType, String interfaces, Set<String> exclusions, Set<String> inclusions, String plugin, List<String> compilationSources, Map<String, String> options, Map<String, String> immutableImplementation) {
        this.sourcePackage = sourcePackage;
        this.sourceClass = sourceClass;
        this.targetPackage = targetPackage;
        this.targetPrefix = targetPrefix;
        this.targetPostfix = targetPostfix;
        this.superType = superType;
        this.interfaces = interfaces;
        this.exclusions = exclusions;
        this.inclusions = inclusions;
        this.plugin = plugin;
        this.compilationSources = compilationSources;
        this.options = options;
        this.immutableImplementation = immutableImplementation;
    }

    public void setSourceClass(String sourceClass) {
        this.sourceClass = sourceClass;
    }

    public void setTargetPackage(String targetPackage) {
        this.targetPackage = targetPackage;
    }

    public void setTargetPostfix(String targetPostfix) {
        this.targetPostfix = targetPostfix;
    }

    public void setExclusions(Set<String> exclusions) {
        this.exclusions = exclusions;
    }

    public void setPlugin(String plugin) {
        this.plugin = plugin;
    }

    public void setCompilationSources(List<String> compilationSources) {
        this.compilationSources = compilationSources;
    }

    public void setOptions(Map<String, String> options) {
        this.options = options;
    }

    public void setSuperType(String superType) {
        this.superType = superType;
    }

    public String getInterfaces() {
        return interfaces;
    }

    public void setInterfaces(String interfaces) {
        this.interfaces = interfaces;
    }

    public void setInclusions(Set<String> inclusions) {
        this.inclusions = inclusions;
    }

    public void setImmutableImplementation(Map<String, String> immutableImplementation) {
        this.immutableImplementation = immutableImplementation;
    }

    public String getSourcePackage() {
        return sourcePackage;
    }

    public String getSourceClass() {
        return sourceClass;
    }

    public String getTargetPackage() {
        return targetPackage;
    }

    public String getTargetPrefix() {
        return targetPrefix;
    }

    public String getTargetPostfix() {
        return targetPostfix;
    }


    
    public String getSuperType() {
        return superType;
    }

    public Set<String> getExclusions() {
        return exclusions;
    }

    public Set<String> getInclusions() {
        return inclusions;
    }

    public String getPlugin() {
        return plugin;
    }

    public List<String> getCompilationSources() {
        return compilationSources;
    }

    public Map<String, String> getOptions() {
        return options;
    }

    public Map<String, String> getImmutableImplementation() {
        return immutableImplementation;
    }

    @Override
    public String toString() {
        return
                "DescriptorImpl{" +
                        "sourcePackage='" + sourcePackage + '\'' +
                        ", sourceClass='" + sourceClass + '\'' +
                        ", targetPackage='" + targetPackage + '\'' +
                        ", targetPostfix='" + targetPostfix + '\'' +
                        ", superType='" + superType + '\'' +
                        ", interfaces='" + interfaces + '\'' +
                        ", exclusions=" + exclusions +
                        ", inclusions=" + inclusions +
                        ", immutableImplementation=" + immutableImplementation +
                        ", plugin=" + plugin +
                        ", compilationSources=" + compilationSources +
                        ", options=" + options +
                        '}';
    }
}
