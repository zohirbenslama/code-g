package org.abstractmeta.code.g.core.collection.function;


import org.abstractmeta.code.g.code.JavaMethod;
import com.google.common.base.Function;

/**
 * Represents  MethodNameKeyFunction.
 *
 * @author awitas
 * @version 0.01 29/03/2012
 */

public class MethodNameKeyFunction implements Function<JavaMethod, String> {

    @Override
    public String apply(JavaMethod method) {
         return method.getName();
    }
}
