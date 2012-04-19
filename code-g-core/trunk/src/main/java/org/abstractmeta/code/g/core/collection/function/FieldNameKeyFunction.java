package org.abstractmeta.code.g.core.collection.function;


import com.google.common.base.Function;
import com.sun.tools.hat.internal.model.JavaField;

/**
 * Represents  MethodNameKeyFunction.
 *
 * @author awitas
 * @version 0.01 29/03/2012
 */

public class FieldNameKeyFunction implements Function<JavaField, String> {

    @Override
    public String apply(JavaField field) {
         return field.getName();
    }

}
