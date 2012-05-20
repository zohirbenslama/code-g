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
package org.abstractmeta.code.g.core.expression;

import org.abstractmeta.code.g.expression.AbstractionPattern;
import org.abstractmeta.code.g.expression.MethodPattern;

import java.util.Iterator;
import java.util.List;

/**
 * Represents AbstractionPattern.
 * <p><b>Usage</b>
 * <code><pre>
 * AbstractionPattern expression  = new AbstractionPatternBuilder()
 * .add(new MethodPatternBuilder()
 * .addOperationNames("get", "is")
 * .setBaseResultType(Object.class).build())
 * .add(new MethodPatternBuilder()
 * .addOperationNames("set")
 * .setBaseResultType(void.class)
 * .addBaseParameterTypes(Object.class).build()).build();
 * </p>
 *
 * @author Adrian Witas
 */
public class AbstractionPatternImpl implements AbstractionPattern {

    private final List<MethodPattern> methodPatterns;

    public AbstractionPatternImpl(List<MethodPattern> methodPatterns) {
        this.methodPatterns = methodPatterns;
    }

    @Override
    public void add(MethodPattern pattern) {
        methodPatterns.add(pattern);
    }

    @Override
    public int size() {
        return methodPatterns.size();
    }

    @Override
    public Iterator<MethodPattern> iterator() {
        return methodPatterns.iterator();
    }
}
