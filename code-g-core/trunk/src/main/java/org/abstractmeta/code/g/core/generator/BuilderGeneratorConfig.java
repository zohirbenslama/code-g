package org.abstractmeta.code.g.core.generator;

/**
 * BuilderGeneratorConfig
 *
 * @author Adrian Witas
 */
public class BuilderGeneratorConfig extends ClassGeneratorConfig {

    private boolean includeIsPresentField;

    public boolean isIncludeIsPresentField() {
        return includeIsPresentField;
    }

    public void setIncludeIsPresentField(boolean includeIsPresentField) {
        this.includeIsPresentField = includeIsPresentField;
    }
}
