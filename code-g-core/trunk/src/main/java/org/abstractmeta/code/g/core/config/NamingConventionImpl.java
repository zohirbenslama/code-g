package org.abstractmeta.code.g.core.config;

import org.abstractmeta.code.g.config.NamingConvention;

/**
 * Naming Convention
 *
 * @author Adrian Witas
 */
public class NamingConventionImpl implements NamingConvention {

    private String classPrefix;
    private String classPostfix;
    private String packagePostfix;

    public String getClassPrefix() {
        return classPrefix;
    }

    public void setClassPrefix(String classPrefix) {
        this.classPrefix = classPrefix;
    }

    public String getClassPostfix() {
        return classPostfix;
    }

    public void setClassPostfix(String classPostfix) {
        this.classPostfix = classPostfix;
    }

    public String getPackagePostfix() {
        return packagePostfix;
    }

    public void setPackagePostfix(String packagePostfix) {
        this.packagePostfix = packagePostfix;
    }
}
