package org.abstractmeta.code.g.core.config;

import org.abstractmeta.code.g.config.Implementation;

import java.util.Collection;

/**
 *
 * Implementation implementation
 * @author Adrian Witas
 */
public class ImplementationImpl implements Implementation {

    private String superType;
    private Collection<String> interfaces;

    public String getSuperType() {
        return superType;
    }

    public void setSuperType(String superType) {
        this.superType = superType;
    }

    public Collection<String> getInterfaces() {
        return interfaces;
    }

    public void setInterfaces(Collection<String> interfaces) {
        this.interfaces = interfaces;
    }
}
