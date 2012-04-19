package org.abstractmeta.code.g.core.pattern.builder;

import org.abstractmeta.code.g.core.pattern.MethodGroupMatchImpl;
import org.abstractmeta.code.g.pattern.MethodGroupMatch;
import org.abstractmeta.code.g.pattern.MethodMatch;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Represents MethodGroupMatchBuilder
 * 
 * @author Adrian Witas
 */
public class MethodGroupMatchBuilder {

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


    public MethodGroupMatch build() {
        for(MethodMatchBuilder builder: matchBuilders) {
            matches.add(builder.build());    
        }
        matchBuilders.clear();
        return new MethodGroupMatchImpl(name, matches);
    }
}
