package org.abstractmeta.code.g.core.util;

import org.abstractmeta.code.g.config.Descriptor;

/**
 * Represents  DescriptorUtil.
 *
 * @author awitas
 * @version 0.01 14/11/2012
 */
public class DescriptorUtil {

    public static boolean is(Descriptor descriptor, String optionName) {
        if(descriptor.getOptions() == null) return false;
        String option = descriptor.getOptions().get(optionName);
        if(option == null) return false;
        return ("true".equalsIgnoreCase(option));
    }


    public static String get(Descriptor descriptor, String optionName) {
        if(descriptor.getOptions() == null) return null;
        String option = descriptor.getOptions().get(optionName);
        if(option == null) return null;
        return option;
    }

    public static <T> T getInstance(Descriptor descriptor, Class<T> type, String optionName) {
        if(descriptor.getOptions() == null) return null;
        String option = descriptor.getOptions().get(optionName);
        if(option == null) return null;
        try {
            return type.cast(Class.forName(option).newInstance());
        } catch (Exception e) {
            throw new IllegalStateException("Failed to create instance of "+ option, e);
        }
    }

}

        
