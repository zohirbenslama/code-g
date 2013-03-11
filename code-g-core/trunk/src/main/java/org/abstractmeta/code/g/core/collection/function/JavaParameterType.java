package org.abstractmeta.code.g.core.collection.function;

import com.google.common.base.Function;
import org.abstractmeta.code.g.code.JavaParameter;

import javax.annotation.Nullable;
import java.lang.reflect.Type;

/**
 *
 * @author Adrian Witas
 */
public class JavaParameterType implements Function<JavaParameter, Type> {

    @Override
    public Type apply(@Nullable JavaParameter parameter) {
        return parameter.getType();
    }
}
