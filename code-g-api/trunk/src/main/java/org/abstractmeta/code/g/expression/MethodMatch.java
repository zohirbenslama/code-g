/**
 * Copyright 2011 Adrian Witas
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
