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

import org.apache.maven.artifact.Artifact;

import java.util.List;

/**
 * This mojo executes the code plugin for configured packages
 * and includes them in in the final artifact.
 *
 * @phase generate-test-sources
 * @goal testCompile
 * @requiresDependencyResolution test
 */
public class CodegenCompilerMojo extends AbstractCodegenMojo {


    protected List<Artifact> getDependencyArtifacts() {
        @SuppressWarnings("unchecked")
        List<Artifact> compileArtifacts = project.getCompileArtifacts();
        return compileArtifacts;
    }

}
