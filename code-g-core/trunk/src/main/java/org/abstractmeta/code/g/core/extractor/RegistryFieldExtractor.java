package org.abstractmeta.code.g.core.extractor;


import org.abstractmeta.code.g.code.JavaField;
import org.abstractmeta.code.g.code.JavaType;
import org.abstractmeta.code.g.core.code.builder.JavaFieldBuilder;
import org.abstractmeta.code.g.core.internal.ParameterizedTypeImpl;
import org.abstractmeta.code.g.core.pattern.MethodGroupPatterns;
import org.abstractmeta.code.g.core.pattern.MethodMatcherImpl;
import org.abstractmeta.code.g.core.util.ReflectUtil;
import org.abstractmeta.code.g.extractor.FieldExtractor;
import org.abstractmeta.code.g.pattern.MethodGroupMatch;
import org.abstractmeta.code.g.pattern.MethodMatch;
import org.abstractmeta.code.g.pattern.MethodMatcher;
import com.google.common.base.CaseFormat;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Registry field extractor from source java type.
 *
 * @author Adrian Witas
 */
public class RegistryFieldExtractor implements FieldExtractor {

    private final MethodMatcher methodMatcher;

    public RegistryFieldExtractor() {
        this.methodMatcher = new MethodMatcherImpl();
    }


    /**
     **
     * <p>Matches registry pattern usage to extract registry field.</p>
     * <p>Matches getter setter method to build related field.</p>
     * For instance for given methods: <ul>
     * <li>public Foo get(String id)</li>
     * <li>public void register(Foo foo)</li>
     * <li>public Buzz getBuzz(String name)</li>
     * <li>public void registerBuzz(Buzz buzz)</li>
     * <p/>
     * </ul>
     * The following fields are created:
     * <ul>
     * <li>private Map&lt;String, Foo> registry;</li>
     * <li>private Map&lt;String, Buzz> buzzRegistry;</li>
     * </ul>
     *
     * @param sourceType java source type
     * @return extracted field list
     */
    @Override
    public List<JavaField> extract(JavaType sourceType) {
        List<JavaField> result = new ArrayList<JavaField>();
        List<MethodGroupMatch> matchedGroups = methodMatcher.match(sourceType.getMethods(), MethodGroupPatterns.REGISTRY_PATTERN);
        for (MethodGroupMatch match : matchedGroups) {
            if (!(match.containsMatch("register", Object.class) && match.containsMatch("get", Object.class))) {
                continue;
            }
            MethodMatch registerMethodMatch = match.getMatch("register", Object.class);
            MethodMatch getMethodMatch = match.getMatch("get", Object.class);
            Type keyType = getMethodMatch.getMethod().getParameterTypes().get(0);
            Type valueType = registerMethodMatch.getMethod().getParameterTypes().get(0);
            String name = match.getName();
            JavaFieldBuilder fieldBuilder = new JavaFieldBuilder();
            fieldBuilder.addModifier("private").setImmutable(true);


            fieldBuilder.setType(new ParameterizedTypeImpl(null, Map.class, ReflectUtil.getObjectType(keyType), ReflectUtil.getObjectType(valueType)));
            if (name.isEmpty()) {
                fieldBuilder.setName("registry");
            } else {
                String filedName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, match.getName());
                fieldBuilder.setName(filedName + "Registry");
            }
            result.add(fieldBuilder.build());
        }
        return result;
    }
}
