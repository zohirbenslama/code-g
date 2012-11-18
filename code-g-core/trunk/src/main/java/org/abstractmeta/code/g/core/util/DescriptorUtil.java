package org.abstractmeta.code.g.core.util;

import org.abstractmeta.code.g.config.Descriptor;

/**
 * Represents  DescriptorUtil.
 *
 * @author Adrian Witas
 */
public class DescriptorUtil {

    public static boolean is(Descriptor descriptor, String optionName) {
        if(descriptor.getOptions() == null) return false;
        Object option = descriptor.getOptions().get(optionName);
        if(option == null) return false;
        return ("true".equalsIgnoreCase("" + option));
    }


    public static String get(Descriptor descriptor, String optionName) {
        if(descriptor.getOptions() == null) return null;
        Object option = descriptor.getOptions().get(optionName);
        if(option == null) return null;
        return (String)option;
    }

    public static <T> T loadInstance(Descriptor descriptor, Class<T> type, String optionName) {
        if(descriptor.getOptions() == null) return null;
        Object option = descriptor.getOptions().get(optionName);
        if(option == null) return null;
        try {
            return type.cast(Class.forName("" + option).newInstance());
        } catch (Exception e) {
            throw new IllegalStateException("Failed to createCommandInnerClass instance of "+ option, e);
        }
    }

}

        
