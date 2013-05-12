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
package org.abstractmeta.toolbox.codegen.plugin;

/**
 *
 */
public class SourceMatcher {

    private String sourceDirectory;
    private String classNames;
    private String packageNames;
    private boolean includeSubpackages;
    private String inclusionPatterns;
    private String exclusionPatterns;
    private String dependencyPackages;

    public String getSourceDirectory() {
        return sourceDirectory;
    }

    public void setSourceDirectory(String sourceDirectory) {
        this.sourceDirectory = sourceDirectory;
    }

    public String getClassNames() {
        return classNames;
    }

    public void setClassNames(String classNames) {
        this.classNames = classNames;
    }

    public String getPackageNames() {
        return packageNames;
    }

    public void setPackageNames(String packageNames) {
        this.packageNames = packageNames;
    }

    public boolean isIncludeSubpackages() {
        return includeSubpackages;
    }

    public void setIncludeSubpackages(boolean includeSubpackages) {
        this.includeSubpackages = includeSubpackages;
    }

    public String getInclusionPatterns() {
        return inclusionPatterns;
    }

    public void setInclusionPatterns(String inclusionPatterns) {
        this.inclusionPatterns = inclusionPatterns;
    }

    public String getExclusionPatterns() {
        return exclusionPatterns;
    }

    public void setExclusionPatterns(String exclusionPatterns) {
        this.exclusionPatterns = exclusionPatterns;
    }

    public String getDependencyPackages() {
        return dependencyPackages;
    }

    public void setDependencyPackages(String dependencyPackages) {
        this.dependencyPackages = dependencyPackages;
    }
}
