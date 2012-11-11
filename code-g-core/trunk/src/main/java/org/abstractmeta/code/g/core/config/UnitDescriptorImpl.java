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

import java.util.List;

/**
 * Default UnitDescriptor implementation.
 *
 * @author Adrian Witas
 */
public class UnitDescriptorImpl implements UnitDescriptor {
    
    private String sourcePackage;
    private String sourceDirectory;
    private String targetDirectory;
    private List<String> classPathEntries;
    private List<Descriptor> descriptors;
    private Descriptor postDescriptor;

    public UnitDescriptorImpl() {
    }

    public UnitDescriptorImpl(String sourcePackage, String sourceDirectory, String targetDirectory, List<String> classPathEntries, List<Descriptor> descriptors, Descriptor postDescriptor) {
        this.sourcePackage = sourcePackage;
        this.sourceDirectory = sourceDirectory;
        this.targetDirectory = targetDirectory;
        this.classPathEntries = classPathEntries;
        this.descriptors = descriptors;
        this.postDescriptor = postDescriptor;
    }

    public void setSourcePackage(String sourcePackage) {
        this.sourcePackage = sourcePackage;
    }

    public void setSourceDirectory(String sourceDirectory) {
        this.sourceDirectory = sourceDirectory;
    }

    public void setTargetDirectory(String targetDirectory) {
        this.targetDirectory = targetDirectory;
    }

    public void setClassPathEntries(List<String> classPathEntries) {
        this.classPathEntries = classPathEntries;
    }

    public void setDescriptors(List<Descriptor> descriptors) {
        this.descriptors = descriptors;
    }

    public String getSourcePackage() {
        return sourcePackage;
    }

    public String getSourceDirectory() {
        return sourceDirectory;
    }

    public String getTargetDirectory() {
        return targetDirectory;
    }

    public List<String> getClassPathEntries() {
        return classPathEntries;
    }

    public List<? extends Descriptor> getDescriptors() {
        return descriptors;
    }

    @Override
    public Descriptor getPostDescriptor() {
        return postDescriptor;
    }

    @Override
    public String toString() {
        return "UnitDescriptorImpl{" +
                "sourcePackage='" + sourcePackage + '\'' +
                ", sourceDirectory='" + sourceDirectory + '\'' +
                ", targetDirectory='" + targetDirectory + '\'' +
                ", classPathEntries=" + classPathEntries +
                ", descriptors=" + descriptors +
                ", postDescriptor=" + postDescriptor +
                '}';
    }
}
