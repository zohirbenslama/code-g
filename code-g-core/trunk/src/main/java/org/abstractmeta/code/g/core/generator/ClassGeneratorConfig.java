package org.abstractmeta.code.g.core.generator;

import org.abstractmeta.code.g.core.code.handler.type.EqualMethodHandler;
import org.abstractmeta.code.g.core.code.handler.type.HashCodeMethodHandler;

/**
 * Represents ClassGeneratorConfig
 *
 * @author Adrian Witas
 */
public class ClassGeneratorConfig implements EqualMethodHandler.Config, HashCodeMethodHandler.Config {

    private boolean generateEqualMethod;
    private boolean generateHashMethod;
    private int hashMultiplier;
    private String includeInHashAnnotation;

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
}
