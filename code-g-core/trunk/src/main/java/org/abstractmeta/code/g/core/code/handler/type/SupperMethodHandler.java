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
package org.abstractmeta.code.g.core.code.handler.type;

import org.abstractmeta.code.g.code.JavaMethod;
import org.abstractmeta.code.g.code.JavaParameter;
import org.abstractmeta.code.g.code.JavaType;
import org.abstractmeta.code.g.code.JavaTypeBuilder;
import org.abstractmeta.code.g.code.handler.TypeHandler;
import org.abstractmeta.code.g.core.code.builder.JavaMethodBuilder;

import java.util.Set;

/**
 * This handle a proxy to a method
 *
 * @author Adrian Witas
 */
public class SupperMethodHandler  {

//    public static final String SKIP_METHODS_OPTION = "SupperMethodHandler.skipMethods";
//
//    @Override
//    public void handle(JavaTypeBuilder owner) {
//        JavaType sourceType = owner.getSourceType();
//        if(sourceType == null) return;
//        Set<String> skipMethods = DecoderUtil.readStringSet(owner.getDescriptor().getOptions(), SKIP_METHODS_OPTION);
//        for(JavaMethod methodCandidate: sourceType.getMethods()) {
//            if(skipMethods.contains(methodCandidate.getName())) continue;
//            if(owner.containsMethod(methodCandidate.getName(), methodCandidate.getParameters().toArray(new JavaParameter[]{}))) {
//                 continue;
//            }
//            JavaMethodBuilder methodBuilder = new JavaMethodBuilder();
//            methodBuilder.merge(methodCandidate);
//            methodCandidate.getBodyLines().clear();
//
//
//
//        }
//    }

//
//    private final JavaTypeBuilderImpl ownerTypeBuilder;
//    private final Descriptor descriptor;
//
//    public SupperMethodHandler(JavaTypeBuilderImpl ownerTypeBuilder, Descriptor descriptor) {
//        this.ownerTypeBuilder = ownerTypeBuilder;
//        this.descriptor = descriptor;
//    }
//
//
//    @Override
//    public void handle(JavaType sourceType, JavaMethod javaMethod) {
//        if (!javaMethod.getBodyLines().isEmpty()) return;
//        JavaPluginHandler javaPluginHandler = DescriptorUtil.newInstance(descriptor, JavaPluginHandler.class, SimpleClassBuilderImpl.CHANGE_HANDLER);
//        if (javaPluginHandler != null) {
//            javaPluginHandler.handle(descriptor, sourceType, javaMethod);
//            StringBuilder superCallBuiler = new StringBuilder();
//            if (!void.class.equals(javaMethod.getResultType())) {
//                superCallBuiler.append("return ");
//            }
//            superCallBuiler.append("super.").append(javaMethod.getName()).append("(");
//            List<String> methodParameterNames = JavaTypeUtil.getParameterNames(javaMethod.getParameters());
//            superCallBuiler.append(Joiner.on(", ").join(methodParameterNames)).append(");");
//            javaMethod.getBodyLines().add(superCallBuiler.toString());
//        }
//    }
}
