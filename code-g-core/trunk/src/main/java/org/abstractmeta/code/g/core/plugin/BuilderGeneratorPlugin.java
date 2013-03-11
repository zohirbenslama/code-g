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

/**
 * <p><b>Builder CodeUnitGenerator Plugin</b></p>
 * <h2>Overview</h2>
 * <p>This plugin generates code implementation for the builder. The generation process can be break down to</p>
 * <ul>
 * <li>Fields generation, all fields are extracted from source class' fields.
 * <ul>
 * <li>all fields are mutable</li>
 * <li>for each field additional starting with '_' is added to track whether original field was mutated</li>
 * </ul>
 * </li>
 * <li>Method generation - once all fields are defined and type is being generate the following
 * field handlers are fired with ability to generate relevant method
 * <ul>
 * <li>{@link org.abstractmeta.code.g.core.handler.field.BuilderSetterFieldHandler}</li>
 * <li>{@link org.abstractmeta.code.g.core.handler.field.BuilderCollectionFieldHandler}</li>
 * <li>{@link org.abstractmeta.code.g.core.handler.field.BuilderMapFieldHandler}</li>
 * <li>{@link org.abstractmeta.code.g.core.handler.field.BuilderArrayFieldHandler}</li>
 * <li>{@link org.abstractmeta.code.g.core.code.handler.field.GetterFieldHandler}</li>
 * <li>{@link org.abstractmeta.code.g.core.handler.field.IsFieldPresentHandler}</li>
 * <li>{@link org.abstractmeta.code.g.core.handler.type.BuilderTypeHandler}</li>
 * <li>{@link org.abstractmeta.code.g.core.handler.type.BuilderMergeHandler}</li>
 * </ul>
 * </li>
 * </ul>
 *
 * </p>
 * <p>Builder options</p>
 * <ul>
 *     <li>skipIsPresentMethod - skips adding 'is present' field and method for each non transient field </li>
 * </ul>
 *
 * @author Adrian Witas
 */
public class BuilderGeneratorPlugin  {
//
//
//    private static final Map<String, Descriptor> TEMPLATES = MapMaker.makeImmutable(Descriptor.class,
//            BuilderGeneratorPlugin.class.getName(),
//            new DescriptorBuilder().setTargetPackagePostfix("builder")
//                    .setTargetPostfix("Builder")
//                    .build()
//    );
//
//    public static final String ADD_PRESENT_METHOD = "addPresentMethod";
//
//    @Override
//    protected boolean isApplicable(JavaType sourceType) {
//        List<JavaField> fields = sourceType.getFields();
//        return !((sourceType.getModifiers().contains(JavaModifier.ABSTRACT)
//                || JavaKind.ENUM.equals(sourceType.getKind())
//                || fields.size() == 0)
//                || (fields.size() == 1 && fields.get(0).getName().equals("registry")));
//    }
//
//    @Override
//    protected JavaTypeBuilder generate(JavaType sourceType, JavaTypeRegistry typeRegistry, String targetTypeName, Descriptor descriptor) {
//        JavaTypeBuilder javaTypeBuilder = new JavaTypeBuilderImpl(JavaKind.CLASS, targetTypeName, sourceType, descriptor, typeRegistry);
//        addBuilderHandlers(javaTypeBuilder);
//        javaTypeBuilder.addModifiers(JavaModifier.PUBLIC);
//        javaTypeBuilder.addGenericTypeArguments(sourceType.getGenericTypeArguments());
//        for (JavaField field : sourceType.getFields()) {
//            JavaFieldBuilder fieldBuilder = new JavaFieldBuilder();
//            fieldBuilder.addModifier(JavaModifier.PRIVATE);
//            fieldBuilder.setType(field.getType());
//            fieldBuilder.setName(field.getName());
//            javaTypeBuilder.addField(fieldBuilder.build());
//
//            if (DescriptorUtil.is(descriptor, BuilderGeneratorPlugin.ADD_PRESENT_METHOD)) {
//                JavaFieldBuilder trackerFieldBuilder = new JavaFieldBuilder();
//                trackerFieldBuilder.addModifiers(JavaModifier.PRIVATE, JavaModifier.TRANSIENT);
//                trackerFieldBuilder.setType(boolean.class);
//                trackerFieldBuilder.setName(StringUtil.isPresentFieldName(field.getName()));
//                javaTypeBuilder.addField(trackerFieldBuilder.build());
//            }
//        }
//        return javaTypeBuilder;
//    }
//
//    protected void addBuilderHandlers(JavaTypeBuilder javaTypeBuilder) {
//
//    }
//
//    protected String getTargetTypeName(JavaType sourceType, Descriptor descriptor, JavaTypeRegistry registry) {
//        String buildResultTypeName = JavaTypeUtil.matchDeclaringTypeName(sourceType);
//        String buildResultSimpleClassName = JavaTypeUtil.getSimpleClassName(buildResultTypeName, true);
//        buildResultSimpleClassName = buildResultSimpleClassName.replace(".", "");
//        return getTargetTypeName(buildResultSimpleClassName, sourceType.getPackageName(),  descriptor, registry);
//    }
//
//
//    @Override
//    public Map<String, Descriptor> getTemplates() {
//        return TEMPLATES;
//    }
}
