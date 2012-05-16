package org.abstractmeta.code.g.expression;

import org.abstractmeta.code.g.code.JavaMethod;

/**
 * Represents method match for the java method and given method expression.
 * <p>Assume we have the following java methods
 * <li>Foo getFoo(String name)</li>
 * <li>Collection&lt;Foo> getFoo()</li>
 * <li>boolean isFoo(String name)</li>
 * <li>boolean getFoo(Foo foo);</li>
 * The following method expression: <br/>
 *   MethodPattern({operationNames: ['is', 'get'], baseResultType = Object.class}, baseParameterTypes = Object.class) <br/>
 *  only matches all listed alternatives:
 * <li>Foo getFoo(String name)</li>
 * <li>boolean isFoo(String name)</li>
 * Although method Collection&lt;Foo> getFoo() could be matched by name, expression also specifies baseParameterTypes as one any type argument,
 * this method has zero argument thus is skipped.
 * </p>
 *
 *
 * @author Adrian Witas
 */
public interface MethodMatch {

    JavaMethod getMethod();

    MethodPattern getPattern();

}
