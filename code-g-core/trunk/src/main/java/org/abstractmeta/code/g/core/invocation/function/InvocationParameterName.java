package org.abstractmeta.code.g.core.invocation.function;

import com.google.common.base.Function;
import org.abstractmeta.code.g.core.invocation.InvocationParameter;

/**
* Represents InvocationParameterName
* <p>
* </p>
*
* @author Adrian Witas
*/
public class InvocationParameterName implements Function<InvocationParameter, String> {

    @Override
    public String apply(InvocationParameter invocationParameter) {
        return invocationParameter.getName();
    }
}
