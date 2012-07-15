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
package org.abstractmeta.code.g.core.expression.builder;

import org.abstractmeta.code.g.core.expression.AbstractionPatternImpl;
import org.abstractmeta.code.g.expression.AbstractionPattern;
import org.abstractmeta.code.g.expression.MethodPattern;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Represents AbstractionPatternBuilder
 * <p>
 * </p>
 *
 * @author Adrian Witas
 */
public class AbstractionPatternBuilder {

    private final List<MethodPattern> methodPatterns = new ArrayList<MethodPattern>();
    private final List<MethodPatternBuilder> methodPatternBuilders = new ArrayList<MethodPatternBuilder>();


    public AbstractionPatternBuilder add(MethodPattern ... pattern) {
        Collections.addAll(methodPatterns, pattern);
        return this;
    }

    public AbstractionPatternBuilder add(Collection<MethodPattern> pattern) {
        methodPatterns.addAll(pattern);
        return this;
    }

    
    public MethodPatternBuilder add() {
        MethodPatternBuilder builder = new MethodPatternBuilder();
        methodPatternBuilders.add(builder);
        return builder;
    }

    public AbstractionPattern build() {
        for(MethodPatternBuilder builder: methodPatternBuilders) {
            methodPatterns.add(builder.build());
        }
        methodPatternBuilders.clear();
        return new AbstractionPatternImpl(methodPatterns);
    }

}
