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
package org.abstractmeta.code.g.core.builder.handler.field;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import org.abstractmeta.code.g.code.*;
import org.abstractmeta.code.g.code.handler.FieldHandler;
import org.abstractmeta.code.g.core.code.builder.JavaMethodBuilder;
import org.abstractmeta.code.g.core.expression.AbstractionPatterns;
import org.abstractmeta.code.g.core.internal.ParameterizedTypeImpl;
import org.abstractmeta.code.g.core.util.JavaTypeUtil;
import org.abstractmeta.code.g.core.util.ReflectUtil;
import org.abstractmeta.code.g.expression.AbstractionMatch;
import org.abstractmeta.code.g.expression.MethodMatch;
import org.abstractmeta.code.g.expression.MethodMatcher;
import org.abstractmeta.code.g.generator.Context;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * <p>This handler builds the following methods
 * <ul>
 * <li>add[GROUP FIELD]</li>
 * <li>remove[GROUP FIELD]</li>
 * <li>get[GROUP FIELD]</li>
 * <li>contains[GROUP FIELD]Register</li>
 * </ul>
 * <h2>Simple Registry use cases</h2>
 * <h2>Simple Registry use cases</h2>
 * For this example interface if field registry if present the followings implementations can be generated
 * <pre>
 *
 * public static interface IMessage {
 *
 * public IEntry getEntry(String key);
 *
 * public void addEntries(IEntry... entries);
 *
 * public boolean containsEntry(String key);
 *
 * public boolean removeEntry(String key);
 *
 * }
 *
 *
 * public static interface IEntry {
 *
 * int getId();
 *
 * String getName();
 *
 * String getKey();
 *
 * Integer getValue();
 *
 * }
 *
 *
 * </pre>
 * <ol>
 * <li><b>Basic implementation</b>
 * Default key provider is built based on matching a key type with the first encountered value type's field type.
 * This default matching can be customized by using annotation on value's field to point a source for a key provider
 * see {@link org.abstractmeta.code.g.core.builder.handler.field.IndexedCollectionFieldHandler.Config#getIndexedCollectionItemKeyAnnotation()}
 * <pre>
 * package com.test;
 *
 * import java.util.Collection;
 * import java.util.Collections;
 *
 * public class Message implements IMessage {
 *
 * private final Collection<IEntry> entries;
 *
 * public Message(Collection<IEntry> entries) {
 * super();
 * this.entries = entries;
 * }
 *
 * public Collection<IEntry> getEntries() {
 * return this.entries;
 * }
 *
 * public IEntry getEntry(String instance) {
 * for(IEntry candidate: this.entries) {
 * if(candidate.getName().equals(instance))  {
 * return candidate;
 * }
 * }
 * return null;
 * }
 *
 * public boolean containsEntry(String argument0) {
 * return getEntry(argument0) != null;
 * }
 *
 * public boolean removeEntry(String argument0) {
 * IEntry item = getEntry(argument0);
 * if(item == null) return false;
 * return entries.remove(item);
 * }
 *
 * public void addEntries(IEntry ... argument0) {
 * Collections.addAll(this.entries, argument0);
 * }
 *
 * }
 *
 * </pre>
 * <li><b>Predicate base Implementation</b>
 <pre>

 package com.test;

 import com.google.common.base.Optional;
 import com.google.common.base.Predicate;
 import com.google.common.collect.Iterables;
 import java.util.Collection;
 import java.util.Collections;

 public class Message implements IMessage {
     private final Collection&lt;IEntry> entries;
    
     public Message(Collection&lt;IEntry> entries) {
         super();
         this.entries = entries;
     }
    
     public Collection&lt;IEntry> getEntries() {
         return this.entries;
     }
    
     public IEntry getEntry(String instance) {
         Optional&lt;IEntry> result = Iterables.tryFind(entries, new EntryEqualPredicate(instance));
         if(result.isPresent()) return result.get();
         return null;
     }
    
     public boolean containsEntry(String argument0) {
         return getEntry(argument0) != null;
     }
    
     public boolean removeEntry(String argument0) {
         IEntry item = getEntry(argument0);
         if(item == null) return false;
         return entries.remove(item);
     }
    
     public void addEntries(IEntry ... argument0) {
         Collections.addAll(this.entries, argument0);
     }
    
    
     public static class EntryEqualPredicate implements Predicate&lt;IEntry> {
         private final String requiredValue;
        
         public EntryEqualPredicate(String requiredValue) {
            this.requiredValue = requiredValue;
         }
        
         public boolean apply(IEntry candidate) {
             return requiredValue.equals(candidate.getName());
         }
        
     }
 }
 </pre>
 *     
 *     
 * </li>
 *
 * @author Adrian Witas
 */
public class IndexedCollectionFieldHandler implements FieldHandler {


    private final MethodMatcher methodMatcher;

    public IndexedCollectionFieldHandler(MethodMatcher methodMatcher) {
        this.methodMatcher = methodMatcher;
    }

    @Override
    public void handle(JavaTypeBuilder owner, JavaField javaField, Context context) {
        if (!isApplicable(owner, javaField)) return;
        AbstractionMatch match = getMatch(owner, javaField);
        Optional<MethodMatch> getMethodMatch = Iterables.tryFind(match.getMatches(), new SingleParameterGetPredicate());
        if (!getMethodMatch.isPresent()) return;
        JavaMethod getMethod = getMethodMatch.get().getMethod();
        JavaType predicateType = null;
        if (isUseKeyProvider(context)) {
            JavaParameter theFirstParameter = getMethod.getParameters().get(0);
            JavaMethod providerMethod = JavaTypeUtil.matchOwnerFieldWithMatchingType(getMethod.getResultType(), theFirstParameter.getType(), getKeyProviderAnnotation(context));
            predicateType = buildPredicate(owner, providerMethod, getMethod);
        }

        for (MethodMatch methodMatch : match.getMatches()) {
            if (owner.containsMethod(methodMatch.getMethod().getName())) continue;
            JavaMethod matchedMethod = methodMatch.getMethod();
            if (matchedMethod.getName().startsWith("add")) {
                buildAddMethod(owner, javaField, matchedMethod);
            } else if (matchedMethod.getName().startsWith("get")) {
                buildGetMethod(owner, javaField, matchedMethod, predicateType, context);
            } else if (matchedMethod.getName().startsWith("contains")) {
                buildContainsMethod(owner, javaField, matchedMethod, getMethod, context);
            } else if (matchedMethod.getName().startsWith("remove")) {
                buildRemoveMethod(owner, javaField, matchedMethod, getMethod, context);
            } else {
                throw new UnsupportedOperationException(matchedMethod.getName());
            }

        }
    }



    protected void buildRemoveMethod(JavaTypeBuilder owner, JavaField javaField, JavaMethod matchedMethod, JavaMethod getMethod, Context context) {
        JavaMethodBuilder methodBuilder = new JavaMethodBuilder();
        methodBuilder.addModifier(JavaModifier.PUBLIC).setResultType(matchedMethod.getResultType())
                .setName(matchedMethod.getName())
                .addParameters(matchedMethod.getParameters());
        JavaParameter containsMethodTheFirstParameter = matchedMethod.getParameters().get(0);
        JavaParameter getMethodTheFirstParameter = getMethod.getParameters().get(0);
        if (javaField.getType().equals(containsMethodTheFirstParameter.getType())) {
            methodBuilder.addBodyLines(String.format("return %s.remove(%s)", javaField.getName(), getMethodTheFirstParameter.getName()));

        } else if (getMethodTheFirstParameter.getType().equals(getMethodTheFirstParameter.getType())) {
            methodBuilder.addBodyLines(String.format("%s item = %s(%s);", owner.getImporter().getSimpleTypeName(getMethod.getResultType()), getMethod.getName(), getMethodTheFirstParameter.getName()));
            methodBuilder.addBodyLines("if(item == null) return false;");
            methodBuilder.addBodyLines(String.format("return %s.remove(item);", javaField.getName()));

        }
        owner.addMethod(methodBuilder.build());
    }


    protected void buildContainsMethod(JavaTypeBuilder owner, JavaField javaField, JavaMethod matchedMethod, JavaMethod getMethod, Context context) {
        JavaMethodBuilder methodBuilder = new JavaMethodBuilder();
        methodBuilder.addModifier(JavaModifier.PUBLIC).setResultType(matchedMethod.getResultType())
                .setName(matchedMethod.getName())
                .addParameters(matchedMethod.getParameters());

        JavaParameter containsMethodTheFirstParameter = matchedMethod.getParameters().get(0);
        JavaParameter getMethodTheFirstParameter = getMethod.getParameters().get(0);
        if (javaField.getType().equals(containsMethodTheFirstParameter.getType())) {
            methodBuilder.addBodyLines(String.format("return %s.contains(%s)", javaField.getName(), getMethodTheFirstParameter.getName()));

        } else if (getMethodTheFirstParameter.getType().equals(getMethodTheFirstParameter.getType())) {
            methodBuilder.addBodyLines(String.format("return %s(%s) != null;", getMethod.getName(), getMethodTheFirstParameter.getName()));
        }
        owner.addMethod(methodBuilder.build());
    }


    protected void buildAddMethod(JavaTypeBuilder owner, JavaField javaField, JavaMethod matchedMethod) {
        if (owner.containsMethod(matchedMethod.getName())) return;
        JavaMethodBuilder methodBuilder = new JavaMethodBuilder();
        methodBuilder.setName(matchedMethod.getName()).addModifier(JavaModifier.PUBLIC);
        methodBuilder.setResultType(matchedMethod.getResultType());
        JavaParameter theFirstParameter = matchedMethod.getParameters().get(0);
        methodBuilder.addParameters(theFirstParameter);
        Class parameterType = ReflectUtil.getRawClass(theFirstParameter.getType());
        if (theFirstParameter.isVarTypeArgument()) {
            owner.getImporter().addTypes(Collections.class);
            methodBuilder.addBodyLines(String.format("Collections.addAll(this.%s, %s);", javaField.getName(), theFirstParameter.getName()));

        } else if (Collection.class.isAssignableFrom(parameterType)) {
            methodBuilder.addBodyLines(String.format("this.%s.addAll(%s);", javaField.getName(), theFirstParameter.getName()));

        }
        owner.addMethod(methodBuilder.build());

    }


    protected String getKeyProviderAnnotation(Context context) {
        Config config = context.getOptional(Config.class);
        if (config == null) return null;
        return config.getIndexedCollectionItemKeyAnnotation();
    }


    protected void buildGetMethod(JavaTypeBuilder owner, JavaField javaField, JavaMethod matchedMethod, JavaType predicateType, Context context) {
        if (owner.containsMethod(matchedMethod.getName())) {
            return;
        }
        Type resultType = matchedMethod.getResultType();
        JavaParameter theFirstParameter = matchedMethod.getParameters().get(0);
        Type componentType = theFirstParameter.getType();
        owner.getImporter().addTypes(resultType);
        owner.getImporter().addTypes(componentType);
        JavaMethod matchedAccessor = JavaTypeUtil.matchOwnerFieldWithMatchingType(resultType, componentType, getKeyProviderAnnotation(context));

        JavaMethodBuilder methodBuilder = new JavaMethodBuilder();
        methodBuilder.setName(matchedMethod.getName());
        methodBuilder.setResultType(resultType);
        methodBuilder.addModifier(JavaModifier.PUBLIC);
        methodBuilder.setResultType(resultType).addParameter("instance", theFirstParameter.getType());
        if(isUseKeyProvider(context))  {
            buildGetWithPredicate(owner, methodBuilder, javaField, predicateType);

        } else {
            buildGetWithFindingItemMethod(owner, methodBuilder, ReflectUtil.getRawClass(resultType), javaField.getName(), matchedAccessor);
        }

        owner.addMethod(methodBuilder.build());
    }

    private void buildGetWithPredicate(JavaTypeBuilder owner, JavaMethodBuilder methodBuilder, JavaField javaField, JavaType predicateType) {
        owner.getImporter().addTypes(Optional.class);
        owner.getImporter().addTypes(Iterables.class);
        String predicateSimpleName = predicateType.getSimpleName();
        String genericSimpleName = owner.getImporter().getSimpleTypeName(new ParameterizedTypeImpl(null, Optional.class, methodBuilder.getResultType()));
        methodBuilder.addBodyLines(genericSimpleName + String.format(" result = Iterables.tryFind(%s, new %s(%s));", javaField.getName(), predicateSimpleName, methodBuilder.getParameters().get(0).getName()));
        methodBuilder.addBodyLines("if(result.isPresent()) return result.get();", "return null;");

    }


    protected void buildGetWithFindingItemMethod(JavaTypeBuilder owner, JavaMethodBuilder methodBuilder, Class resultType, String fieldName, JavaMethod itemProvider) {
        methodBuilder.addBodyLines(String.format("for(%s candidate: this.%s) { ", owner.getImporter().getSimpleTypeName(resultType), fieldName));
        Class providedType = ReflectUtil.getRawClass(itemProvider.getResultType());
        if (providedType.isPrimitive()) {
            methodBuilder.addBodyLines(String.format("    if(candidate.%s() == instance)  { ", itemProvider.getName()));
        } else {
            methodBuilder.addBodyLines(String.format("    if(candidate.%s().equals(instance))  { ", itemProvider.getName()));
        }
        methodBuilder.addBodyLines("        return candidate;");
        methodBuilder.addBodyLines("    }");
        methodBuilder.addBodyLines("}");
        methodBuilder.addBodyLines("return null;");
    }



    protected boolean isUseKeyProvider(Context context) {
        return context.contains(Config.class) && Boolean.TRUE.equals(context.get(Config.class).isIndexCollectionUsePredicate());
    }


    protected boolean isApplicable(JavaTypeBuilder owner, JavaField target) {
        return Collection.class.isAssignableFrom(ReflectUtil.getRawClass(target.getType()))
                && owner.getSourceType() != null && getMatch(owner, target) != null;
    }


    protected AbstractionMatch getMatch(JavaTypeBuilder owner, JavaField target) {
        Map<String, AbstractionMatch> sourceMatches = methodMatcher.indexByName(methodMatcher.match(owner.getSourceType().getMethods(), AbstractionPatterns.INDEXED_COLLECTION_PATTERN));
        for (String groupName : sourceMatches.keySet()) {
            if (groupName.toLowerCase().endsWith(target.getName().toLowerCase())) {
                return sourceMatches.get(groupName);
            }
        }
        return null;
    }


    protected JavaType buildPredicate(JavaTypeBuilder owner, JavaMethod provideMethod, JavaMethod getMethod) {
        String prefixName = getPredicatePrefix(getMethod);
        JavaTypeBuilder simpleType = JavaTypeUtil.buildKeyProviderEqualPredicate(provideMethod, prefixName, getMethod.getResultType());
        owner.addNestedJavaTypes(simpleType);
        return simpleType;
    }

    protected String getPredicatePrefix(JavaMethod getMethod) {
        return getMethod.getName().replace("get", "");
    }

    protected static class SingleParameterGetPredicate implements Predicate<MethodMatch> {


        @Override
        public boolean apply(MethodMatch methodMatch) {
            JavaMethod javaMethod = methodMatch.getMethod();
            return javaMethod.getName().startsWith("get") && javaMethod.getParameters().size() == 1;
        }
    }


    public static interface Config {

        /**
         * Annotation to flag a field to be used as a source for a key provider
         *
         * @return
         */
        String getIndexedCollectionItemKeyAnnotation();


        /**
         * TODO implement this method
         * @return
         */
        boolean isIndexedCollectionUseCache();


        /**
         * Flag to use key predicate to match value
         * It uses {@link com.google.common.base.Predicate}
         *
         * @return
         */
        boolean isIndexCollectionUsePredicate();

    }
}
