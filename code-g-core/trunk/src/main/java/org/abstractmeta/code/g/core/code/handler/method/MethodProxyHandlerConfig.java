package org.abstractmeta.code.g.core.code.handler.method;

import java.util.Set;

/**
 * Simple method proxy handler.
 * This abstraction InvocationHandler
 * <p>
 * </p>
 *
 * @author Adrian Witas
 */
public class MethodProxyHandlerConfig {

    private final Set<String> addMethods;
    private final Set<String> skipMethods;
    private final String proxyOwnerInterface;

    public MethodProxyHandlerConfig(Set<String> addMethods, Set<String> skipMethods, String proxyOwnerInterface) {
        this.addMethods = addMethods;
        this.skipMethods = skipMethods;
        this.proxyOwnerInterface = proxyOwnerInterface;
    }

    public Set<String> getAddMethods() {
        return addMethods;
    }

    public Set<String> getSkipMethods() {
        return skipMethods;
    }

    public String getProxyOwnerInterface() {
        return proxyOwnerInterface;
    }

}
