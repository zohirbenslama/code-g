package org.abstractmeta.toolbox.codegen.plugin;


import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.util.*;


public abstract class AbstractCodegenMojo extends AbstractMojo {


    /**
     * The current Maven project.
     *
     * @parameter default-value="${project}"
     * @readonly
     * @required
     */
    protected MavenProject project;


    /**
     * The current Maven project.
     *
     * @parameter default-value="${basedir}"
     * @readonly
     * @required
     */
    protected String basedir;

    /**
     * @parameter default-value="${plugin.artifacts}"
     */
    private java.util.List pluginArtifacts;


    /**
     * List of package to build descriptors
     *
     * @parameter
     */
    private ArrayList<Descriptor> descriptors;


    /**
     * List of plugin search package names
     *
     * @parameter
     */
    private ArrayList<String> pluginPackages;


    /**
     * @parameter default-value="${project.build.directory}/generated-sources/code-g/"
     */
    private String targetSourceDirectory;



    public void execute() throws MojoExecutionException, MojoFailureException {
        try {


            if (descriptors == null || descriptors.isEmpty()) {
                throw new MojoFailureException("<descriptor> is not configured " + descriptors + "  " + targetSourceDirectory);
            }



            List<String> classPathEntries = new ArrayList<String>();
            for (File artifact : getDependencyArtifactFiles()) {
                classPathEntries.add(artifact.getAbsolutePath());
            }
            getLog().debug("generation class path " + classPathEntries);
            CodeGenerator codeGenerator = new CodeGenerator();
            Collection<File> generatedSources = codeGenerator.generate(descriptors, classPathEntries, targetSourceDirectory, basedir, pluginPackages);
            for (File sourceFile : generatedSources) {
                getLog().debug("generated " + sourceFile.getAbsolutePath());
            }
            project.addCompileSourceRoot(targetSourceDirectory);


        } catch (RuntimeException e) {
            throw new MojoExecutionException("Failed to generate code", e);
        }
    }


    abstract protected List<Artifact> getDependencyArtifacts();


    /**
     * Gets the {@link File} for each dependency artifact.
     *
     * @return A set of all dependency artifacts.
     */
    protected Set<File> getDependencyArtifactFiles() {
        Set<File> dependencyArtifactFiles = new HashSet<File>();
        for (Artifact artifact : getDependencyArtifacts()) {
            dependencyArtifactFiles.add(artifact.getFile());
        }
        for (Artifact artifact : getPluginArtifacts()) {
            dependencyArtifactFiles.add(artifact.getFile());
        }
        return dependencyArtifactFiles;
    }

    @SuppressWarnings("unchecked")
    protected List<Artifact> getPluginArtifacts() {
        List<Artifact> result = pluginArtifacts;
        return result;
    }


}
