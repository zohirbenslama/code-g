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
 * <p>This plugin generates code implementation for a given interface, superclass. The generation process can be break down to</p>
 * <ul>
 * <li>Fields generation, all fields are extracted from source class' based on matching method names.
 * <ul>
 * <li>It extracts setter/getter fields. If source class has ony get method it assumes the field is immutable.</li>
 * <li>It extracts registry fields for registry like methods (register, isRegister, get, etc ...)</li>
 * </ul>
 * </li>
 * <li>Method generation - once all fields are defined and type is being generate the following
 * field handlers are fired with ability to generate relevant method
 * <ul>
 * <li>{@link org.abstractmeta.code.g.core.handler.field.RegistryFieldHandler}</li>
 * <li>{@link org.abstractmeta.code.g.core.handler.field.CollectionFieldHandler}</li>
 * <li>{@link org.abstractmeta.code.g.core.code.handler.field.GetterFieldHandler}</li>
 * <li>{@link org.abstractmeta.code.g.core.handler.type.SimpleTypeHandler}</li>
 * </ul>
 * </li>
 * <p/>
 * </ul>
 *
 * @author Adrian Witas
 */
public class ClassGeneratorPlugin  {

//
//    private static final Map<String, Descriptor> TEMPLATES = MapMaker.makeImmutable(Descriptor.class,
//            ClassGeneratorPlugin.class.getName(),
//            new DescriptorBuilder().setTargetPackagePostfix("impl")
//                    .setTargetPostfix("Impl")
//                    .build(),
//            ClassGeneratorPlugin.class.getName() + ".complete",
//            new DescriptorBuilder().setTargetPackagePostfix("impl")
//                    .setTargetPostfix("Impl")
//                    .addOptions("generateHashCodeMethod", "true")
//                    .addOptions("generateEqualsMethod", "true")
//                    .addOptions("generateHashCodeMethod.hashFieldAnnotation", Id.class.getName())
//                    .build()
//    );
//
//
//    public ClassGeneratorPlugin() {
//        super(Arrays.asList(new RegistryFieldExtractor(), new AccessorFieldExtractor()), Arrays.<MethodExtractor>asList(new SuperMethodExtractor()));
//    }
//
//    @Override
//    protected boolean isApplicable(JavaType sourceType) {
//        return isExtractable(sourceType) || sourceType.getFields().size() > 0;
//    }
//
//    @Override
//    protected JavaTypeBuilder generate(JavaType sourceType, JavaTypeRegistry typeRegistry, String targetTypeName, Descriptor descriptor) {
//        JavaTypeBuilder classBuilder = new JavaTypeBuilderImpl(JavaKind.CLASS, targetTypeName, sourceType, descriptor, typeRegistry);
//        addBuilderHandlers(classBuilder);
//        classBuilder.addModifiers(JavaModifier.PUBLIC);
//        if (!sourceType.getGenericTypeArguments().isEmpty()) {
//            classBuilder.addGenericTypeArguments(sourceType.getGenericTypeArguments());
//        }
//        if (JavaKind.CLASS.equals(sourceType.getKind())) {
//            classBuilder.setSuperType(new TypeNameWrapper(sourceType.getName(), sourceType.getGenericTypeArguments()));
//        } else {
//            classBuilder.addSuperInterfaces(new TypeNameWrapper(sourceType.getName(), sourceType.getGenericTypeArguments()));
//        }
//        addExtractableFields(classBuilder, sourceType);
//        addExtractableMethods(classBuilder, sourceType);
//        return classBuilder;
//    }
//
//
//    protected void addBuilderHandlers(JavaTypeBuilder javaTypeBuilder) {
//
//    }
//
//
//    protected String getTargetTypeName(JavaType sourceType, Descriptor descriptor, JavaTypeRegistry registry) {
//        String buildResultTypeName = JavaTypeUtil.matchDeclaringTypeName(sourceType);
//        String buildResultSimpleClassName = JavaTypeUtil.getSimpleClassName(buildResultTypeName, true);
//        buildResultSimpleClassName = buildResultSimpleClassName.replace(".", "");
//        return getTargetTypeName(buildResultSimpleClassName,  sourceType.getPackageName(), descriptor, registry);
//    }
//
//
//    @Override
//    public Map<String, Descriptor> getTemplates() {
//        return TEMPLATES;
//    }
}
