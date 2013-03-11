package org.abstractmeta.code.g.core.config.loader;

import com.google.common.base.Predicate;
import org.abstractmeta.code.g.code.JavaType;
import org.abstractmeta.code.g.config.SourceFilter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * SourceFilter Predicate
 * It is used to filter java types.
 *
 * @author Adrian Witas
 */
public class SourceFilterPredicate implements Predicate<JavaType> {

    private final SourceFilter sourceFilter;

    public SourceFilterPredicate(SourceFilter sourceFilter) {
        this.sourceFilter = sourceFilter;
    }

    @Override
    public boolean apply(JavaType javaType) {
        if(sourceFilter.getClassNames() != null && sourceFilter.getClassNames().contains(javaType.getName())) {
            return true;
        }
        if(sourceFilter.getInclusionPatterns() != null) {
           for(String pattern: sourceFilter.getInclusionPatterns())  {
               Matcher matcher = Pattern.compile(pattern).matcher(javaType.getName());
               if(matcher.matches()) return true;
           }
            return false;
        }
        if(sourceFilter.getExclusionPatterns() != null) {
            for(String pattern: sourceFilter.getInclusionPatterns())  {
                Matcher matcher = Pattern.compile(pattern).matcher(javaType.getName());
                if(matcher.matches()) return false;
            }
            return true;
        }
        return true;
    }
}
