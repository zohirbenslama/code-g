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
import org.abstractmeta.code.g.config.UnitDescriptor;

import java.util.Collection;
import java.util.List;

/**
 * Default UnitDescriptor implementation.
 *
 * @author Adrian Witas
 */
public class UnitDescriptorImpl implements UnitDescriptor {


    private Collection<String> dependencyPackages;
    private String targetSourceDirectory;
    private String targetCompilationDirectory;
    private String sourceDirectory;
    private List<String> classPathEntries;
    private List<Descriptor> descriptors;
    private Descriptor postDescriptor;


    public Collection<String> getDependencyPackages() {
        return dependencyPackages;
    }

    public void setDependencyPackages(Collection<String> dependencyPackages) {
        this.dependencyPackages = dependencyPackages;
    }

    public String getTargetSourceDirectory() {
        return targetSourceDirectory;
    }

    public void setTargetSourceDirectory(String targetSourceDirectory) {
        this.targetSourceDirectory = targetSourceDirectory;
    }

    public String getTargetCompilationDirectory() {
        return targetCompilationDirectory;
    }

    public void setTargetCompilationDirectory(String targetCompilationDirectory) {
        this.targetCompilationDirectory = targetCompilationDirectory;
    }

    public String getSourceDirectory() {
        return sourceDirectory;
    }

    public void setSourceDirectory(String sourceDirectory) {
        this.sourceDirectory = sourceDirectory;
    }

    public List<String> getClassPathEntries() {
        return classPathEntries;
    }

    public void setClassPathEntries(List<String> classPathEntries) {
        this.classPathEntries = classPathEntries;
    }

    public List<Descriptor> getDescriptors() {
        return descriptors;
    }

    public void setDescriptors(List<Descriptor> descriptors) {
        this.descriptors = descriptors;
    }

    public Descriptor getPostDescriptor() {
        return postDescriptor;
    }

    public void setPostDescriptor(Descriptor postDescriptor) {
        this.postDescriptor = postDescriptor;
    }
}
