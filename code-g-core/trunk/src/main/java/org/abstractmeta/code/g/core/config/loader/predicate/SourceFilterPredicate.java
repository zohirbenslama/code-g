package org.abstractmeta.code.g.core.config.loader.predicate;

import com.google.common.base.Predicate;
import org.abstractmeta.code.g.code.JavaType;
import org.abstractmeta.code.g.config.SourceMatcher;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * SourceMatcher Predicate
 * It is used to filter java types.
 *
 * @author Adrian Witas
 */
public class SourceFilterPredicate implements Predicate<JavaType> {

    private final SourceMatcher sourceMatcher;

    public SourceFilterPredicate(SourceMatcher sourceMatcher) {
        this.sourceMatcher = sourceMatcher;
    }

    @Override
    public boolean apply(JavaType javaType) {
        if(sourceMatcher.getClassNames() != null && sourceMatcher.getClassNames().contains(javaType.getName())) {
            return true;
        }
        if(sourceMatcher.getInclusionPatterns() != null) {
           for(String pattern: sourceMatcher.getInclusionPatterns())  {
               Matcher matcher = Pattern.compile(pattern).matcher(javaType.getName());
               if(matcher.matches()) return true;
           }
            return false;
        }
        if(sourceMatcher.getExclusionPatterns() != null) {
            for(String pattern: sourceMatcher.getInclusionPatterns())  {
                Matcher matcher = Pattern.compile(pattern).matcher(javaType.getName());
                if(matcher.matches()) return false;
            }
            return true;
        }
        if(sourceMatcher.getPackageNames() != null) {
            for(String packageName: sourceMatcher.getPackageNames())  {
                if(javaType.getName().startsWith(packageName)) {
                    if(sourceMatcher.isIncludeSubpackages()) {
                        return true;
                    } else {
                        int startPosition = Math.min( javaType.getName().length(), packageName.length() + 1);
                        String simpleClassName = javaType.getName().substring(startPosition, javaType.getName().length());
                        if(simpleClassName.indexOf('.') == -1) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
