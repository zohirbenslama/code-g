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

import org.abstractmeta.code.g.core.expression.AbstractionMatchImpl;
import org.abstractmeta.code.g.expression.AbstractionMatch;
import org.abstractmeta.code.g.expression.MethodMatch;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Represents AbstractionMatchBuilder
 * 
 * @author Adrian Witas
 */
public class AbstractionMatchBuilder {

    private String name;
    private List<MethodMatch> matches = new ArrayList<MethodMatch>();
    private List<MethodMatchBuilder> matchBuilders = new ArrayList<MethodMatchBuilder>();

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public List<MethodMatch> getMatches() {
        return matches;
    }

    public void setMatches(List<MethodMatch> matches) {
        this.matches = matches;
    }

    public void addMatches(Collection<MethodMatch> matches) {
        this.matches.addAll(matches);
    }

    public void addMatches(MethodMatch... matches) {
        Collections.addAll(this.matches, matches);
    }

    public MethodMatchBuilder addMatches() {
        MethodMatchBuilder matchBuilder = new MethodMatchBuilder();
        matchBuilders.add(matchBuilder);
        return matchBuilder;
    }


    public AbstractionMatch build() {
        for(MethodMatchBuilder builder: matchBuilders) {
            matches.add(builder.build());    
        }
        matchBuilders.clear();
        return new AbstractionMatchImpl(name, matches);
    }
}
