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


import org.abstractmeta.code.g.code.CompiledJavaType;
import org.abstractmeta.code.g.code.CompiledJavaTypeRegistry;
import org.abstractmeta.code.g.config.*;
import org.abstractmeta.code.g.core.config.UnitDescriptorImpl;
import org.abstractmeta.code.g.core.config.provider.UnitDescriptorProvider;
import org.abstractmeta.code.g.core.generator.CodeUnitGeneratorImpl;
import org.abstractmeta.code.g.core.property.PropertyRegistryImpl;
import org.abstractmeta.code.g.core.util.PropertiesUtil;
import org.abstractmeta.code.g.generator.CodeUnitGenerator;
import org.abstractmeta.code.g.generator.GeneratedCode;
import org.abstractmeta.code.g.property.PropertyRegistry;
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
    private ArrayList<Descriptor> descriptors;


    /**
     * List of dependency packages
     *
     * @parameter
     */

    private List<String> dependencyPackages;

    /**
     * Post descriptor
     */
    private Descriptor postDescriptor;

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
        try {

            CodeUnitGenerator unitGenerator = new CodeUnitGeneratorImpl();
            List<String> classPathEntries = getClassPathEntries();
            getLog().debug("unit plugin class path " + classPathEntries);
            Collection<UnitDescriptor> unitDescriptors = new ArrayList<UnitDescriptor>();
            UnitDescriptor pomUnitDescriptor = loadConfigurationFromPom();
            if (pomUnitDescriptor != null) unitDescriptors.add(pomUnitDescriptor);
            UnitDescriptor propertiesUnitDescriptor = loadConfigurationFromProperties();
            if (propertiesUnitDescriptor != null) unitDescriptors.add(propertiesUnitDescriptor);
            for (UnitDescriptor unitDescriptor : unitDescriptors) {
                getLog().debug("using unit descriptor" + unitDescriptor);
                GeneratedCode generatedCode = unitGenerator.generate(unitDescriptor);
                CompiledJavaTypeRegistry registry = generatedCode.getRegistry();
                getLog().info("Generated " + registry.get().size() + " classes into " + unitDescriptor.getTargetSourceDirectory());
                for (CompiledJavaType compiledJavaType : registry.get()) {
                    getLog().debug("generated " + compiledJavaType.getCompiledType());
                }
            }
        } catch (RuntimeException e) {
            throw new MojoExecutionException("Failed to generate code", e);
        }
    }

    @SuppressWarnings("unchecked")
    protected UnitDescriptorImpl loadConfigurationFromPom() {
        if (descriptors == null || descriptors.isEmpty()) return null;
        UnitDescriptorImpl result = new UnitDescriptorImpl();
        result.setDescriptors(convert(descriptors));
        result.setPostDescriptor(convert(postDescriptor));
        updateMavenProperties(result);
        return result;

    }

    //
    @SuppressWarnings("unchecked")
    protected UnitDescriptorImpl loadConfigurationFromProperties() {
        if (this.configurationFile == null) {
            this.configurationFile = new File(new File(basedir), "/src/main/code-g/unit.properties").getAbsolutePath();
        }
        File configurationFile = new File(this.configurationFile);
        if (! configurationFile.exists()) return null;
        Properties properties = PropertiesUtil.loadFromFile(configurationFile);
        UnitDescriptorImpl result = new UnitDescriptorProvider(properties).get();
        updateMavenProperties(result);
        return result;
    }

    private List<org.abstractmeta.code.g.config.Descriptor> convert(ArrayList<Descriptor> descriptors) {
        List<org.abstractmeta.code.g.config.Descriptor> result = new ArrayList<org.abstractmeta.code.g.config.Descriptor>();
        for (Descriptor descriptor : descriptors) {
            result.add(convert(descriptor));
        }
        return result;
    }

    protected void updateMavenProperties(UnitDescriptorImpl unitDescriptor) {
        PropertyRegistry propertyRegistry = new PropertyRegistryImpl();
        unitDescriptor.setPropertyRegistry(propertyRegistry);

        unitDescriptor.setTargetSourceDirectory(targetSourceDirectory);
        propertyRegistry.register("targetSourceDirectory", targetSourceDirectory);

        unitDescriptor.setSourceDirectory(new File(basedir, "src/main/java").getAbsolutePath());
        propertyRegistry.register("sourceDirectory", unitDescriptor.getSourceDirectory());

        unitDescriptor.setTargetSourceDirectory(targetSourceDirectory);
        propertyRegistry.register("targetSourceDirectory", unitDescriptor.getTargetSourceDirectory());

        unitDescriptor.setTargetCompilationDirectory(new File(projectBuildDirectory, "classes").getAbsolutePath());
        propertyRegistry.register("targetCompilationDirectory", unitDescriptor.getTargetCompilationDirectory());

        unitDescriptor.setClassPathEntries(getClassPathEntries());
    }


    protected org.abstractmeta.code.g.config.Descriptor convert(Descriptor descriptor) {
        if (descriptor == null) return null;
        org.abstractmeta.code.g.core.config.DescriptorImpl result = new org.abstractmeta.code.g.core.config.DescriptorImpl();
        result.setGeneratorClass(descriptor.getGeneratorClass());
        result.setProperties(descriptor.getProperties());
        result.setNamingConvention(descriptor.getNamingConvention());
        result.setSourceMatcher(convert(descriptor.getSourceMatcher()));
        return result;
    }

    private org.abstractmeta.code.g.config.SourceMatcher convert(SourceMatcher sourceMatcher) {
        org.abstractmeta.code.g.core.config.SourceMatcherImpl result = new org.abstractmeta.code.g.core.config.SourceMatcherImpl();
        result.setClassNames(convert(sourceMatcher.getClassNames()));
        result.setDependencyPackages(convert(sourceMatcher.getDependencyPackages()));
        result.setExclusionPatterns(convert(sourceMatcher.getExclusionPatterns()));
        result.setIncludeSubpackages(sourceMatcher.isIncludeSubpackages());
        result.setPackageNames(convert(sourceMatcher.getPackageNames()));
        result.setSourceDirectory(sourceMatcher.getSourceDirectory());

        return result;
    }

    protected List<String> convert(String text) {
        if (text == null) return null;
        List<String> result = new ArrayList<String>();
        Collections.addAll(result, text.split(","));
        return result;
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
        if (getPluginArtifacts() != null) {
            for (Artifact artifact : getPluginArtifacts()) {
                dependencyArtifactFiles.add(artifact.getFile());
            }
        }
        return dependencyArtifactFiles;
    }

    @SuppressWarnings("unchecked")
    protected List<Artifact> getPluginArtifacts() {
        return pluginArtifacts;
    }

    public MavenProject getProject() {
        return project;
    }

    public void setProject(MavenProject project) {
        this.project = project;
    }

    public String getBasedir() {
        return basedir;
    }

    public void setBasedir(String basedir) {
        this.basedir = basedir;
    }

    public void setPluginArtifacts(List pluginArtifacts) {
        this.pluginArtifacts = pluginArtifacts;
    }

    public ArrayList<Descriptor> getDescriptors() {
        return descriptors;
    }

    public void setDescriptors(ArrayList<Descriptor> descriptors) {
        this.descriptors = descriptors;
    }

    public List<String> getDependencyPackages() {
        return dependencyPackages;
    }

    public void setDependencyPackages(List<String> dependencyPackages) {
        this.dependencyPackages = dependencyPackages;
    }

    public Descriptor getPostDescriptor() {
        return postDescriptor;
    }

    public void setPostDescriptor(Descriptor postDescriptor) {
        this.postDescriptor = postDescriptor;
    }

    public String getTargetSourceDirectory() {
        return targetSourceDirectory;
    }

    public void setTargetSourceDirectory(String targetSourceDirectory) {
        this.targetSourceDirectory = targetSourceDirectory;
    }

    public String getProjectBuildDirectory() {
        return projectBuildDirectory;
    }

    public void setProjectBuildDirectory(String projectBuildDirectory) {
        this.projectBuildDirectory = projectBuildDirectory;
    }

    public String getConfigurationFile() {
        return configurationFile;
    }

    public void setConfigurationFile(String configurationFile) {
        this.configurationFile = configurationFile;
    }
}
