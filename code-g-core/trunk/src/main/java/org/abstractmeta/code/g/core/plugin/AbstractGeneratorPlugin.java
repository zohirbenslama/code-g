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
package org.abstractmeta.code.g.core.plugin;

import org.abstractmeta.code.g.code.JavaType;
import org.abstractmeta.code.g.code.JavaTypeBuilder;
import org.abstractmeta.code.g.code.JavaTypeRegistry;
import org.abstractmeta.code.g.core.internal.TypeNameWrapper;
import org.abstractmeta.code.g.config.Descriptor;
import org.abstractmeta.code.g.core.util.CollectionUtil;
import org.abstractmeta.code.g.extractor.FieldExtractor;
import org.abstractmeta.code.g.extractor.MethodExtractor;

import java.lang.reflect.Type;
import java.util.*;

/**
 * Abstract plugin plugin.
 * <p>Convenience class that provides default implementation for most core cases.</p>
 * <h3>Plugin configuration</h3>
 * <ul>
 * The following option use default value from descriptor
 * <li>targetPackage</li>
 * <li>source</li>
 * <li>targetPostfix</li>
 * <li>superType</li>
 * </ul>
 * <h3>Plugin usage</h3>
 * Each sub class should implement the following methods
 * <ul>
 * <li>isApplicable - filters source classes by checking if plugin plugin can applied</li>
 * <li>generate - generates new type based on a given source type</li>
 * <p/>
 * </ul>
 *
 * @author Adrian Witas
 */
public abstract class AbstractGeneratorPlugin {
//
//    private final List<FieldExtractor> fieldExtractors;
//    private final List<MethodExtractor> methodExtractors;
//
//    protected AbstractGeneratorPlugin() {
//        this(Collections.<FieldExtractor>emptyList(), Collections.<MethodExtractor>emptyList());
//    }
//
//    protected AbstractGeneratorPlugin(List<FieldExtractor> fieldExtractors, List<MethodExtractor> methodExtractors) {
//        this.fieldExtractors = fieldExtractors;
//        this.methodExtractors = methodExtractors;
//    }
//
//
//    public List<String> generate(Collection<String> sourceTypeNames, JavaTypeRegistry typeRegistry, Descriptor descriptor) {
//        List<String> generatedTypeNames = new ArrayList<String>();
//        for (String sourceTypeName : sourceTypeNames) {
//            JavaType sourceType = typeRegistry.get(sourceTypeName);
//            if (! isApplicable(sourceType)) {
//                continue;
//            }
//            String targetTypeName = getTargetTypeName(sourceType, descriptor, typeRegistry);
//            if(typeRegistry.isRegistered(targetTypeName)) {
//                continue;
//            }
//            JavaTypeBuilder typeBuilder = generate(sourceType, typeRegistry, targetTypeName, descriptor);
//            if (typeBuilder == null) {
//                continue;
//            }
//            buildSuperType(descriptor, typeBuilder);
//            buildInterfaces(descriptor, typeBuilder);
//            typeRegistry.register(typeBuilder.build());
//            generatedTypeNames.add(targetTypeName);
//        }
//
//        return generatedTypeNames;
//    }
//
//
//    protected void buildSuperType(Descriptor descriptor, JavaTypeBuilder typeBuilder) {
//        String superTypeName = descriptor.getSuperType();
//        if (superTypeName == null) return;
//        Type superType = new TypeNameWrapper(superTypeName);
//        typeBuilder.addImportTypes(superType);
//        typeBuilder.setSuperType(superType);
//    }
//
//    protected void buildInterfaces(Descriptor descriptor, JavaTypeBuilder typeBuilder) {
//        Collection<String> interfaces = descriptor.getInterfaces();
//        if(CollectionUtil.isEmpty(interfaces)) return;
//        String[] interfaceArray = interfaces.toArray(new String[]{});
//        for (String interfaceType : interfaceArray) {
//            Type type = new TypeNameWrapper(interfaceType);
//            typeBuilder.addImportTypes(type);
//            typeBuilder.addSuperInterfaces(type);
//        }
//    }
//
//    protected String getTargetTypeName(JavaType sourceType, Descriptor descriptor, JavaTypeRegistry registry) {
//        return getTargetTypeName(sourceType.getSimpleName(), sourceType.getPackageName(), descriptor, registry);
//    }
//
//    protected String getTargetTypeName(String name, String sourcePackage, Descriptor descriptor, JavaTypeRegistry registry) {
//        String targetPackage;
//        if(descriptor.getTargetPackage() != null) {
//            targetPackage = descriptor.getTargetPackage().replace(".*", "");
//        } else if(sourcePackage != null) {
//            if(descriptor.getTargetPackagePostfix() != null) {
//                 targetPackage = sourcePackage + "." + descriptor.getTargetPackagePostfix();
//            } else {
//                targetPackage = sourcePackage;
//            }
//
//        } else {
//            throw new IllegalStateException("target/source package was null");
//        }
//
//
//        String targetPrefix = descriptor.getTargetClassPrefix();
//        if (targetPrefix == null) targetPrefix = "";
//        String targetPostfix = descriptor.getTargetClassPostfix();
//        if (targetPostfix == null) targetPostfix = "";
//        if(targetPackage.startsWith("java")) {
//            targetPackage = "org.abstractmeta." + targetPackage;
//        }
//
//        return targetPackage + "." + targetPrefix + name + targetPostfix;
//    }
//
//
//    protected boolean isExtractable(JavaType sourceType) {
//        if (fieldExtractors.size() == 0 && methodExtractors.size() == 0) return true;
//        for (FieldExtractor extractor : fieldExtractors) {
//            if (extractor.extract(sourceType).size() > 0) {
//                return true;
//            }
//        }
//        for (MethodExtractor extractor : methodExtractors) {
//            if (extractor.extract(sourceType).size() > 0) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    protected void addExtractableFields(JavaTypeBuilder builder, JavaType sourceType) {
//        for (FieldExtractor extractor : fieldExtractors) {
//            builder.addFields(extractor.extract(sourceType));
//        }
//    }
//
//
//    protected void addExtractableMethods(JavaTypeBuilder builder, JavaType sourceType) {
//        for (MethodExtractor extractor : methodExtractors) {
//            builder.addMethods(extractor.extract(sourceType));
//        }
//    }
//
//
//    /**
//     * Is a given java source type applicable to this class plugin plugin.
//     *
//     * @param sourceType java source type
//     * @return true if applicable
//     */
//    protected abstract boolean isApplicable(JavaType sourceType);
//
//
//
//    /**
//     * Generates new type for a given source type and a given target type name.
//     *
//     *
//     * @param sourceType     source java type
//     * @param registry
//     *@param targetTypeName target type name.
//     * @param descriptor   @return
//     */
//    protected abstract JavaTypeBuilder generate(JavaType sourceType, JavaTypeRegistry registry, String targetTypeName, Descriptor descriptor);
//
//
//    public Map<String, String> getOptions() {
//        return Collections.emptyMap();
//    }
    
}
