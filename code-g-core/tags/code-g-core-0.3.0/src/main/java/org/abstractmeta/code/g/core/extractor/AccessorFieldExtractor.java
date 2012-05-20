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
import org.abstractmeta.code.g.core.expression.AbstractionPatterns;
import org.abstractmeta.code.g.core.expression.MethodMatcherImpl;
import org.abstractmeta.code.g.expression.AbstractionMatch;
import org.abstractmeta.code.g.extractor.FieldExtractor;
import org.abstractmeta.code.g.expression.MethodMatcher;
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
     * <li>public Bar getFoo()</li>
     * <li>public void setFoo(Bar foo)</li>
     * <li>public boolean isBar()</li>
     * <li>public Dummy getDummy()</li>
     * <li>public Buzz getBuzz(String name)</li>
     * <p/>
     * </ul>
     * The following fields are created:
     * <ul>
     * <li>private Bar foo;</li>
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
        List<AbstractionMatch> matchedGroups = methodMatcher.match(sourceType.getMethods(), AbstractionPatterns.ACCESSOR_MUTATOR_PATTERN);
        for (AbstractionMatch match : matchedGroups) {
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
