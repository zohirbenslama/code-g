package org.abstractmeta.code.g.core.code.handler.method;

import org.abstractmeta.code.g.code.JavaMethod;
import org.abstractmeta.code.g.code.JavaTypeBuilder;
import org.abstractmeta.code.g.code.handler.MethodHandler;
import org.abstractmeta.code.g.config.Descriptor;
import org.abstractmeta.code.g.core.util.JavaTypeUtil;

import java.util.Set;

/**
 * Simple method proxy handler.
 * This abstraction InvocationHandler
 * <p>
 * </p>
 *
 * @author Adrian Witas
 */
public class MethodProxyHandler implements MethodHandler {




    @Override
    public void handle(JavaTypeBuilder owner, JavaMethod target) {
        MethodProxyHandlerConfig configuration = getConfiguration(owner.getDescriptor());
        if (isMethodInSkipList(configuration, target)) {
            return;
        }
        if (isMethodProxyble(configuration, target)) {

        }
    }

    /**
     * Checks if method is proxible, it has to be listed, or add list has to be empty (all method by default)
     *
     * @param configuration proxy configuration
     * @param target        target method
     * @return true if method is proxyble
     */
    protected boolean isMethodProxyble(MethodProxyHandlerConfig configuration, JavaMethod target) {
        String methodSignature = JavaTypeUtil.getMethodSignature(target);
        Set<String> methods = configuration.getAddMethods();
        return (configuration.getAddMethods().isEmpty()
                || methods.contains(target.getName())
                || methods.contains(methodSignature));

    }


    protected boolean isMethodInSkipList(MethodProxyHandlerConfig configuration, JavaMethod target) {
        return configuration.getSkipMethods().contains(target.getName());
    }

    protected MethodProxyHandlerConfig getConfiguration(Descriptor descriptor) {
//        ConfigRegistry configurationRegistry = descriptor.getConfigurationRegistry();
//        if (!configurationRegistry.isRegister(MethodProxyHandlerConfig.class)) {
//            configurationRegistry.register(configurationFactory.create(descriptor));
//        }
//        return configurationRegistry.get(MethodProxyHandlerConfig.class);
//    }
        throw new UnsupportedOperationException();
    }

}
