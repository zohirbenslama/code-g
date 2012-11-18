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
import org.abstractmeta.code.g.code.SourcedJavaType;
import org.abstractmeta.code.g.config.UnitDescriptor;
import org.abstractmeta.code.g.core.UnitGeneratorImpl;
import org.abstractmeta.code.g.core.config.builder.UnitDescriptorBuilder;
import org.abstractmeta.code.g.core.config.properties.UnitDescriptorsDecoder;
import org.abstractmeta.code.g.core.handler.PersistenceCodeHandler;
import org.abstractmeta.code.g.core.macro.MacroRegistryImpl;
import org.abstractmeta.code.g.core.util.PropertiesUtil;
import org.abstractmeta.code.g.macros.MacroRegistry;
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


    /**
     * @parameter default-value="${project.build.directory}"
     */
    private String projectBuildDirectory;

    /**
     * @parameter default-value="${basedir}/src/main/code-g/unit.properties"
     */
    private String configurationFile;


    public void execute() throws MojoExecutionException, MojoFailureException {
        final MacroRegistry macroRegistry = new MacroRegistryImpl();
        registerMacros(macroRegistry);
        try {

            List<String> classPathEntries = getClassPathEntries();
            getLog().debug("unit plugin class path " + classPathEntries);
            Collection<UnitDescriptor> unitDescriptors = loadConfiguration();
            UnitGenerator unitGenerator = new UnitGeneratorImpl(macroRegistry, new PersistenceCodeHandler.Factory());
            Collection<SourcedJavaType> generatedSources = unitGenerator.generate(unitDescriptors);
            for (SourcedJavaType sourceFile : generatedSources) {
                getLog().debug("generated " + sourceFile.getFile().getAbsolutePath());
            }
            for (UnitDescriptor unitDescriptor : unitDescriptors) {
                project.addCompileSourceRoot(unitDescriptor.getTargetDirectory());
            }
            project.addCompileSourceRoot(targetSourceDirectory);


        } catch (RuntimeException e) {
            throw new MojoExecutionException("Failed to generate code", e);
        }
    }

    protected Collection<UnitDescriptor> loadConfiguration() {
        File configurationFile = new File(this.configurationFile);
        if (configurationFile.exists()) {
            Properties properties = PropertiesUtil.loadFromFile(configurationFile);
            return new UnitDescriptorsDecoder().decode(properties);

        } else {
            if (units == null) {
                throw new IllegalStateException("units was or " + this.configurationFile + "was null");
            }
            Collection<UnitDescriptor> result = new ArrayList<UnitDescriptor>();
            for (Unit unit : units) {

                UnitDescriptorBuilder unitDescriptorBuilder = new UnitDescriptorBuilder();
                unitDescriptorBuilder.addClassPathEntries(getClassPathEntries());
                unitDescriptorBuilder.setSourceDirectory(new File(this.basedir, "src/main/java").getAbsolutePath());
                unitDescriptorBuilder.setTargetDirectory(targetSourceDirectory);
                unitDescriptorBuilder.merge(unit);
                result.add(unitDescriptorBuilder.build());
            }
            return result;
        }

    }

    protected void registerMacros(MacroRegistry macroRegistry) {
        macroRegistry.register("${basedir}", basedir);
        macroRegistry.register("${project.build.directory}", projectBuildDirectory);
        macroRegistry.register("${project.build.outputDirectory}", new File(projectBuildDirectory, "classes").getAbsolutePath());
        macroRegistry.register("${project.source.java.directory}", new File(basedir, "src/main/java").getAbsolutePath());
        macroRegistry.register("${project.build.generated.sources.directory}", targetSourceDirectory);
        macroRegistry.register("$source", new File(basedir, "src/main/java").getAbsolutePath());
        macroRegistry.register("$basedir", new File(basedir).getAbsolutePath());
        macroRegistry.register("${env.M2_HOME}", System.getenv("M2_HOME"));
    }


    protected void setUnitSourceDirectory(UnitDescriptorBuilder unitBuilder) {
        File source = new File(basedir, "src/main/java");
        String sourceDirectory = unitBuilder.getSourceDirectory();
        if (sourceDirectory == null) sourceDirectory = "";
        if (sourceDirectory.isEmpty()) {
            unitBuilder.setSourceDirectory(source.getAbsolutePath());
        } else {
            sourceDirectory = sourceDirectory.replace("$basedir", basedir);
            sourceDirectory = sourceDirectory.replace("$source", source.getAbsolutePath());
            unitBuilder.setSourceDirectory(sourceDirectory);
        }
    }


//    protected UnitDescriptor buildUnitDescriptor(Unit unit, List<String> classPathEntries) {
//        UnitDescriptorBuilder unitBuilder = new UnitDescriptorBuilder();
//        unitBuilder.merge(unit);
//        unitBuilder.addClassPathEntries(classPathEntries);
//        setUnitTargetDirectory(unitBuilder);
//        setUnitSourceDirectory(unitBuilder);
//        return unitBuilder.build();
//    }
//
//    protected void setUnitTargetDirectory(UnitDescriptorBuilder unitBuilder) {
//        String targetDirectory = unitBuilder.getTargetDirectory();
//        if (targetDirectory == null) targetDirectory = "";
//        if (targetDirectory.isEmpty()) {
//            unitBuilder.setTargetDirectory(targetSourceDirectory);
//        } else {
//            unitBuilder.setTargetDirectory(targetDirectory.replace("$basedir", basedir));
//        }
//    }


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
