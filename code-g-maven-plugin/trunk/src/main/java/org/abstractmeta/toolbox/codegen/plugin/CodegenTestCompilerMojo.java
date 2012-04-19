package org.abstractmeta.toolbox.codegen.plugin;

import org.apache.maven.artifact.Artifact;

import java.util.List;

/**
 * This mojo executes the code generator for configured packages
 * and includes them in the final artifact.
 *
 * @phase generate-sources
 * @goal compile
 * @requiresDependencyResolution compile
 */
public class CodegenTestCompilerMojo extends AbstractCodegenMojo {


    protected List<Artifact> getDependencyArtifacts() {
        @SuppressWarnings("unchecked")
        List<Artifact> testArtifacts = project.getTestArtifacts();
        return testArtifacts;
    }

}
