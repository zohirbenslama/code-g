package org.abstractmeta.code.g.core.collection.function;

import com.google.common.base.Function;
import org.abstractmeta.code.g.code.JavaParameter;
import org.abstractmeta.code.g.core.util.ReflectUtil;

import javax.annotation.Nullable;
import java.lang.reflect.Type;

/**
 *
 * @author Adrian Witas
 */
public class JavaParameterClassFunction implements Function<JavaParameter, Class> {

    @Override
    public Class apply(@Nullable JavaParameter parameter) {
        return ReflectUtil.getRawClass(parameter.getType());
    }
}
