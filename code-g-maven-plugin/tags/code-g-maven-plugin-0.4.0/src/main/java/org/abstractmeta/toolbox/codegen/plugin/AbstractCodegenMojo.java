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


import org.abstractmeta.code.g.UnitGenerator;
import org.abstractmeta.code.g.config.UnitDescriptor;
import org.abstractmeta.code.g.core.UnitGeneratorImpl;
import org.abstractmeta.code.g.core.config.builder.UnitDescriptorBuilder;
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
     * List of package to generate descriptors
     *
     * @parameter
     */
    private ArrayList<Unit> units;


    /**
     * @parameter default-value="${project.build.directory}/generated-sources/code-g/"
     */
    private String targetSourceDirectory;


    public void execute() throws MojoExecutionException, MojoFailureException {
        try {

            List<String> classPathEntries = getClassPathEntries();
            getLog().debug("unit plugin class path " + classPathEntries);
            UnitGenerator unitGenerator = new UnitGeneratorImpl();
            if(units == null) {
                throw new IllegalStateException("units was null");
            }
            for (Unit unit : units) {
                UnitDescriptor unitDescriptor = buildUnitDescriptor(unit, classPathEntries);
                getLog().debug("build unit descriptor: " + unitDescriptor);
                Collection<File> generatedSources = unitGenerator.generate(unitDescriptor);
                for (File sourceFile : generatedSources) {
                    getLog().debug("generated " + sourceFile.getAbsolutePath());
                }
                if (! targetSourceDirectory.equals(unit.getTargetDirectory())) {
                    project.addCompileSourceRoot(unit.getTargetDirectory());
                }
            }
            project.addCompileSourceRoot(targetSourceDirectory);


        } catch (RuntimeException e) {
            throw new MojoExecutionException("Failed to generate code", e);
        }
    }

    protected UnitDescriptor buildUnitDescriptor(Unit unit, List<String> classPathEntries) {
        UnitDescriptorBuilder unitBuilder = new UnitDescriptorBuilder();
        unitBuilder.merge(unit);
        unitBuilder.addClassPathEntries(classPathEntries);
        setUnitTargetDirectory(unitBuilder);
        setUnitSourceDirectory(unitBuilder);
        return unitBuilder.build();
    }

    protected  void setUnitSourceDirectory(UnitDescriptorBuilder unitBuilder) {
        File source = new File(basedir, "src/main/java");
        String sourceDirectory = unitBuilder.getSourceDirectory();
        if(sourceDirectory == null) sourceDirectory = "";
        if(sourceDirectory.isEmpty()) {
            unitBuilder.setSourceDirectory(source.getAbsolutePath());
        } else {
            sourceDirectory = sourceDirectory.replace("$basedir", basedir);
            sourceDirectory = sourceDirectory.replace("$source", source.getAbsolutePath());
            unitBuilder.setSourceDirectory(sourceDirectory);
        }
    }


    protected void setUnitTargetDirectory(UnitDescriptorBuilder unitBuilder) {
        String targetDirectory = unitBuilder.getTargetDirectory();
        if(targetDirectory == null) targetDirectory = "";
        if(targetDirectory.isEmpty()) {
            unitBuilder.setTargetDirectory(targetSourceDirectory);
        } else {
           unitBuilder.setTargetDirectory(targetDirectory.replace("$basedir", basedir));
        }
    }
    
    
    
    
    protected List<String> getClassPathEntries() {
        List<String> result = new ArrayList<String>();
        for (File artifact : getDependencyArtifactFiles()) {
            result.add(artifact.getAbsolutePath());
        }
        return result;
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
        return pluginArtifacts;
    }


}
