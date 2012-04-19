package org.abstractmeta.toolbox.codegen.plugin;

import org.apache.maven.artifact.Artifact;

import java.util.List;

/**
 * This mojo executes the code generator for configured packages
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
