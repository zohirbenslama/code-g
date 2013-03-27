package org.abstractmeta.code.g.core.generator;

import org.abstractmeta.code.g.core.builder.handler.field.RegistryFieldHandler;
import org.abstractmeta.code.g.core.builder.handler.type.EqualMethodHandler;
import org.abstractmeta.code.g.core.builder.handler.type.HashCodeMethodHandler;

/**
 * Represents ClassGeneratorConfig
 *
 * @author Arian Witas
 */
public class ClassGeneratorConfig implements EqualMethodHandler.Config, HashCodeMethodHandler.Config, RegistryFieldHandler.Config {

    private boolean generateEqualMethod;
    private boolean generateHashMethod;
    private int hashMultiplier;
    private String includeInHashAnnotation;
    private boolean generateRegistry;
    private String registryCreateMapMethodName;
    private String registryFieldName;
    private boolean registryUseKeyProvider;

    public boolean isGenerateEqualMethod() {
        return generateEqualMethod;
    }

    public void setGenerateEqualMethod(boolean generateEqualMethod) {
        this.generateEqualMethod = generateEqualMethod;
    }

    public boolean isGenerateHashMethod() {
        return generateHashMethod;
    }

    public void setGenerateHashMethod(boolean generateHashMethod) {
        this.generateHashMethod = generateHashMethod;
    }

    public int getHashMultiplier() {
        return hashMultiplier;
    }

    public void setHashMultiplier(int hashMultiplier) {
        this.hashMultiplier = hashMultiplier;
    }

    public String getIncludeInHashAnnotation() {
        return includeInHashAnnotation;
    }

    public void setIncludeInHashAnnotation(String includeInHashAnnotation) {
        this.includeInHashAnnotation = includeInHashAnnotation;
    }

    public boolean isGenerateRegistry() {
        return generateRegistry;
    }

    public void setGenerateRegistry(boolean generateRegistry) {
        this.generateRegistry = generateRegistry;
    }

    public String getRegistryCreateMapMethodName() {
        return registryCreateMapMethodName;
    }

    public void setRegistryCreateMapMethodName(String registryCreateMapMethodName) {
        this.registryCreateMapMethodName = registryCreateMapMethodName;
    }

    public String getRegistryFieldName() {
        return registryFieldName;
    }

    public void setRegistryFieldName(String registryFieldName) {
        this.registryFieldName = registryFieldName;
    }

    public boolean isRegistryUseKeyProvider() {
        return registryUseKeyProvider;
    }

    public void setRegistryUseKeyProvider(boolean registryUseKeyProvider) {
        this.registryUseKeyProvider = registryUseKeyProvider;
    }
}
