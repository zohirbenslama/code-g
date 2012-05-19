package org.abstractmeta.code.g.core.config;

import org.abstractmeta.code.g.config.Descriptor;
import org.abstractmeta.code.g.config.UnitDescriptor;

import java.util.Collection;
import java.util.List;

/**
 * Default UnitDescriptor implementation.
 * <p>
 * </p>
 *
 * @author Adrian Witas
 */
public class UnitDescriptorImpl implements UnitDescriptor {
    
    private String sourcePackage;
    private String sourceDirectory;
    private String targetDirectory;
    private List<String> classPathEntries;
    private List<Descriptor> descriptors;

    public UnitDescriptorImpl() {
    }

    public UnitDescriptorImpl(String sourcePackage, String sourceDirectory, String targetDirectory, List<String> classPathEntries, List<Descriptor> descriptors) {
        this.sourcePackage = sourcePackage;
        this.sourceDirectory = sourceDirectory;
        this.targetDirectory = targetDirectory;
        this.classPathEntries = classPathEntries;
        this.descriptors = descriptors;
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
    public String toString() {
        return "UnitDescriptorImpl{" +
                "sourcePackage='" + sourcePackage + '\'' +
                ", sourceDirectory='" + sourceDirectory + '\'' +
                ", targetDirectory='" + targetDirectory + '\'' +
                ", classPathEntries=" + classPathEntries +
                ", descriptors=" + descriptors +
                '}';
    }
}
