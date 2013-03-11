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
package org.abstractmeta.code.g.core.handler.type;

/**
 * This handle creates constructor for all fields defined on the owner type.
 * In case where all fields are mutable the empty constructor is added.
 * If builder was not able to generated all methods from source class class is marked as abstract.
 *
 * @author Adrian Witas
 */
public class SimpleTypeHandler  {

//    private final JavaTypeBuilderImpl ownerTypeBuilder;
//    private final Descriptor descriptor;
//    private final JavaTypeRegistry javaTypeRegistry;
//    private final SupperSetterFieldHandler supperSetterFieldHandler;
//    private final SupperGetterFieldHandler supperGetterFieldHandler;
//
//    public SimpleTypeHandler(JavaTypeBuilderImpl ownerTypeBuilder, Descriptor descriptor, JavaTypeRegistry javaTypeRegistry) {
//        this.ownerTypeBuilder = ownerTypeBuilder;
//        this.descriptor = descriptor;
//        this.javaTypeRegistry = javaTypeRegistry;
//        this.supperSetterFieldHandler = new SupperSetterFieldHandler(ownerTypeBuilder, descriptor);
//        this.supperGetterFieldHandler = new SupperGetterFieldHandler(ownerTypeBuilder, descriptor);
//    }
//
//
//    @Override
//    public void handle(JavaType sourceType) {
//
//        buildSupperSetters(sourceType);
//        JavaPluginHandler javaPluginHandler = DescriptorUtil.newInstance(descriptor, JavaPluginHandler.class, SimpleClassBuilderImpl.CHANGE_HANDLER);
//
//
//        if (javaPluginHandler != null) {
//            javaPluginHandler.handle(descriptor, ownerTypeBuilder);
//        }
//
//        if (! hasConstructors()) {
//            Map<String, String> fieldArgumentMap = new HashMap<String, String>();
//            for (JavaField field : sourceType.getFields()) {
//                fieldArgumentMap.put(field.getName(), field.getName());
//            }
//            for (JavaField field : ownerTypeBuilder.getFields()) {
//                fieldArgumentMap.put(field.getName(), field.getName());
//            }
//            JavaConstructor javaConstructor = JavaTypeUtil.buildDefaultConstructor(ownerTypeBuilder, sourceType, fieldArgumentMap);
//            if (javaPluginHandler != null) {
//                javaPluginHandler.handle(descriptor, ownerTypeBuilder, javaConstructor);
//            }
//
//            ownerTypeBuilder.addConstructor(javaConstructor);
//        }
//    }
//
//    private void buildSupperSetters(JavaType sourceType) {
//       Multimap<String, JavaMethod> indexedMethods = Multimaps.index(sourceType.getMethods(), new MethodNameKeyFunction());
//       if ("class".equals(sourceType.getKind())) {
//            for(JavaField field: sourceType.getFields()) {
//                String setMethodName = StringUtil.format(CaseFormat.LOWER_CAMEL, "set", field.getName(), CaseFormat.LOWER_CAMEL);
//                if(indexedMethods.containsPath(setMethodName)) {
//                    supperSetterFieldHandler.handle(ownerTypeBuilder, field);
//                }
//                String getMethodName = StringUtil.format(CaseFormat.LOWER_CAMEL, "get", field.getName(), CaseFormat.LOWER_CAMEL);
//                if(indexedMethods.containsPath(setMethodName)) {
//                    supperGetterFieldHandler.handle(ownerTypeBuilder, field);
//                }
//            }
//        }
//    }
//
//
//    protected boolean hasConstructors() {
//        return ownerTypeBuilder.getConstructors().size() > 0;
//    }


}
