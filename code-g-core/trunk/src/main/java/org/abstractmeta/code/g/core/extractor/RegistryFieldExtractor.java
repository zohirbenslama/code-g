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
package org.abstractmeta.code.g.core.extractor;


import org.abstractmeta.code.g.code.JavaField;
import org.abstractmeta.code.g.code.JavaType;
import org.abstractmeta.code.g.core.code.builder.JavaFieldBuilder;
import org.abstractmeta.code.g.core.internal.ParameterizedTypeImpl;
import org.abstractmeta.code.g.core.expression.AbstractionPatterns;
import org.abstractmeta.code.g.core.expression.MethodMatcherImpl;
import org.abstractmeta.code.g.core.util.ReflectUtil;
import org.abstractmeta.code.g.expression.AbstractionMatch;
import org.abstractmeta.code.g.extractor.FieldExtractor;
import org.abstractmeta.code.g.expression.MethodMatch;
import org.abstractmeta.code.g.expression.MethodMatcher;
import com.google.common.base.CaseFormat;

import java.lang.reflect.Type;
import java.util.*;

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
     * *
     * <p>Matches registry expression usage to extract registry field.</p>
     * <p>Matches getter setter method to generate related field.</p>
     * For instance for given methods: <ul>
     * <li>public Bar get(String id)</li>
     * <li>public void register(Bar foo)</li>
     * <li>public Buzz getBuzz(String name)</li>
     * <li>public void registerBuzz(Buzz buzz)</li>
     * <p/>
     * </ul>
     * The following fields are created:
     * <ul>
     * <li>private Map&lt;String, Bar> registry;</li>
     * <li>private Map&lt;String, Buzz> buzzRegistry;</li>
     * </ul>
     *
     * @param sourceType java source type
     * @return extracted field list
     */
    @Override
    public List<JavaField> extract(JavaType sourceType) {
        List<JavaField> result = new ArrayList<JavaField>();
        List<AbstractionMatch> matchedGroups = methodMatcher.match(sourceType.getMethods(), AbstractionPatterns.REGISTRY_PATTERN);
        for (AbstractionMatch match : matchedGroups) {
            if (!(match.containsMatch("register", Object[].class) && match.containsMatch("get", Object[].class))) {
                continue;
            }
            MethodMatch getMethodMatch = match.getMatch("get", Object[].class);
            MethodMatch registerMatch = match.getMatch("register", Object[].class);

            Type registryType = buildRegistryBackingGenericMap(registerMatch.getMethod().getParameterTypes());
            String name = match.getName();
            JavaFieldBuilder fieldBuilder = new JavaFieldBuilder();
            fieldBuilder.addModifier("private").setImmutable(true);
               fieldBuilder.setType(registryType);
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

    protected static Type buildRegistryBackingGenericMap(Collection<Type> types) {
         return buildRegistryBackingGenericMap(types.toArray(new Type[]{}));
    }

    protected static Type buildRegistryBackingGenericMap(Type[] types) {
        Type result = Map.class;
        for (int i = types.length - 2; i >= 0; i--) {
           if(i == types.length - 2) {
                result = new ParameterizedTypeImpl(null, Map.class,
                        ReflectUtil.getObjectType(types[i]),
                        ReflectUtil.getObjectType(types[i + 1]));
           } else {
               result = new ParameterizedTypeImpl(null, Map.class, ReflectUtil.getObjectType(types[i]), result);
           }
        }
        return result;
    }
    


}
