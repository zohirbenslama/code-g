package org.abstractmeta.code.g.core.config.builder;

import com.google.common.collect.ImmutableList;
import org.abstractmeta.code.g.config.Descriptor;
import org.abstractmeta.code.g.config.UnitDescriptor;
import org.abstractmeta.code.g.core.config.UnitDescriptorImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Represents UnitDescriptorBuilder
 *
 * @author Adrian Witas
 */
public class UnitDescriptorBuilder {

    private String sourcePackage;
    private String sourceDirectory;
    private String targetDirectory;
    private Collection<String> classPathEntries = new ArrayList<String>();
    private Collection<Descriptor> descriptors = new ArrayList<Descriptor>();

    public UnitDescriptorBuilder setSourcePackage(String sourcePackage) {
        this.sourcePackage = sourcePackage;
        return this;
    }

    public UnitDescriptorBuilder setSourceDirectory(String sourceDirectory) {
        this.sourceDirectory = sourceDirectory;
        return this;
    }

    public UnitDescriptorBuilder setTargetDirectory(String targetDirectory) {
        this.targetDirectory = targetDirectory;
        return this;
    }

    public UnitDescriptorBuilder addClassPathEntries(Collection<String> classPathEntries) {
        this.classPathEntries.addAll(classPathEntries);
        return this;
    }

    public UnitDescriptorBuilder addClassPathEntries(String ... classPathEntries) {
        Collections.addAll(this.classPathEntries, classPathEntries);
        return this;
    }

    public UnitDescriptorBuilder setDescriptors(Collection<Descriptor> descriptors) {
        this.descriptors.addAll(descriptors);
        return this;
    }

   public UnitDescriptorBuilder setDescriptors(Descriptor ... descriptors) {
        Collections.addAll(this.descriptors, descriptors);
        return this;
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

    public Iterable<String> getClassPathEntries() {
        return classPathEntries;
    }

    public Collection<Descriptor> getDescriptors() {
        return descriptors;
    }


    public UnitDescriptorBuilder merge(UnitDescriptor unitDescriptor) {
        if(unitDescriptor.getTargetDirectory() != null) {
            this.targetDirectory = unitDescriptor.getTargetDirectory();
        }
        
        if(unitDescriptor.getSourceDirectory() != null) {
            this.sourceDirectory = unitDescriptor.getSourceDirectory();
        }

        if(unitDescriptor.getSourcePackage() != null) {
            this.sourcePackage  = unitDescriptor.getSourcePackage();
        }

        if(unitDescriptor.getClassPathEntries() != null) {
            for(String classEntry: unitDescriptor.getClassPathEntries())  {
                addClassPathEntries(classEntry);
            }
        }
        if(unitDescriptor.getDescriptors() != null) {
                descriptors.addAll(unitDescriptor.getDescriptors());
        }
        return this;
    }

    public UnitDescriptor build() {
         return new UnitDescriptorImpl(sourcePackage, sourceDirectory, targetDirectory, ImmutableList.copyOf(classPathEntries), ImmutableList.copyOf(descriptors));
    }
    
    
}
