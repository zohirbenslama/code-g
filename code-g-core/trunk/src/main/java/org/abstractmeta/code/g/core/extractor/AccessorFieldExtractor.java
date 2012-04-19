package org.abstractmeta.code.g.core.extractor;


import org.abstractmeta.code.g.code.JavaField;
import org.abstractmeta.code.g.code.JavaType;
import org.abstractmeta.code.g.core.code.builder.JavaFieldBuilder;
import org.abstractmeta.code.g.core.pattern.MethodGroupPatterns;
import org.abstractmeta.code.g.core.pattern.MethodMatcherImpl;
import org.abstractmeta.code.g.extractor.FieldExtractor;
import org.abstractmeta.code.g.pattern.MethodGroupMatch;
import org.abstractmeta.code.g.pattern.MethodMatcher;
import com.google.common.base.CaseFormat;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


/**
 * Accessor field extractor from source java type.
 *
 * @author Adrian Witas
 */
public class AccessorFieldExtractor implements FieldExtractor {

    private final MethodMatcher methodMatcher;

    public AccessorFieldExtractor() {
        this.methodMatcher = new MethodMatcherImpl();
    }


    /**
     * <p>Matches getter setter method to extract related field.</p>
     * For instance for given methods: <ul>
     * <li>public Foo getFoo()</li>
     * <li>public void setFoo(Foo foo)</li>
     * <li>public boolean isBar()</li>
     * <li>public Dummy getDummy()</li>
     * <li>public Buzz getBuzz(String name)</li>
     * <p/>
     * </ul>
     * The following fields are created:
     * <ul>
     * <li>private Foo foo;</li>
     * <li>private final boolean bar;</li>
     * <li>private final Dummy dummy;</li>
     * </ul>
     *
     * @param sourceType source type
     * @return extracted filed list
     */
    @Override
    public List<JavaField> extract(JavaType sourceType) {
        List<JavaField> result = new ArrayList<JavaField>();
        List<MethodGroupMatch> matchedGroups = methodMatcher.match(sourceType.getMethods(), MethodGroupPatterns.ACCESSOR_MUTATOR_PATTERN);
        for (MethodGroupMatch match : matchedGroups) {
            Type fieldType;
            if (match.containsMatch("get")) {
                fieldType = match.getMatch("get").getMethod().getResultType();
            } else if (match.containsMatch("is")) {
                fieldType = match.getMatch("is").getMethod().getResultType();
            } else {
                continue;
            }
            String filedName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, match.getName());
            JavaFieldBuilder fieldBuilder = new JavaFieldBuilder();
            fieldBuilder.setName(filedName);
            fieldBuilder.addModifier("private");
            fieldBuilder.setImmutable(!(match.containsMatch("set", Object.class) || match.containsMatch("add", Object.class)));
            fieldBuilder.setType(fieldType);
            result.add(fieldBuilder.build());
        }
        return result;
    }
}
