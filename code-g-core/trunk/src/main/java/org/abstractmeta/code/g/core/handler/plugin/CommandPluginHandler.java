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
package org.abstractmeta.code.g.core.handler.plugin;


/**
 * Represents  CommandPluginHandler.
 * This plugin uses simple class builder to implement command pattern on target instance.
 * <p/>
 * <p/>
 */
public class CommandPluginHandler {

//    private final JavaTypeImporter javaTypeImporter;
//    private final CommandConfigurationDecoder decoderHandler;
//    private final InvocationParser invocationParser;
//    private final InvocationFragmentBuilder invocationFragmentBuilder;
//
//    public CommandPluginHandler() {
//        this(new CommandConfigurationDecoder(), new InvocationParser(), new InvocationFragmentBuilder());
//    }
//
//    public CommandPluginHandler(CommandConfigurationDecoder decoderHandler, InvocationParser invocationParser, InvocationFragmentBuilder invocationFragmentBuilder) {
//        this.decoderHandler = decoderHandler;
//        this.invocationParser = invocationParser;
//        this.invocationFragmentBuilder = invocationFragmentBuilder;
//        this.javaTypeImporter = new JavaTypeImporterImpl("");
//    }
//
//
//
//
//    /**
//     * Handles a new target that implements a command pattern.
//     *
//     * @param descriptor descriptor
//     * @param targetType source type
//     */
//    @Override
//    public void handle(Descriptor descriptor, JavaType targetType) {
//        CommandConfiguration configuration = getConfiguration(descriptor);
//        targetType.getImportTypes().add(Arrays.class);
//        Collection<InvocationParameter> invocationParameters = getOwnerTypeParameters(targetType);
//        buildCommandField(configuration, targetType, invocationParameters);
//        setCommandInterface(configuration, targetType, invocationParameters);
//    }
//
//
//    /**
//     * Builds a command filed and its accessor to store commands
//     */
//    protected void buildCommandField(CommandConfiguration configuration, JavaType targetType, Collection<InvocationParameter> invocationParameters) {
//        CommandConfiguration.RegistryField field = configuration.getRegistryField();
//        InvocationMeta fieldMetaType = invocationParser.parse(field.getType(), invocationParameters);
//        targetType.getImportTypes().add(fieldMetaType.getType());
//        Type commandFieldType = fieldMetaType.getType();
//        String fieldName = field.getName();
//
//        targetType.getFields().add(new JavaFieldBuilder().addModifiers("private", "transient")
//                .setType(commandFieldType)
//                .setName(fieldName).build());
//        String getterMethod = StringUtil.format(CaseFormat.LOWER_CAMEL, "get", fieldName, CaseFormat.LOWER_CAMEL);
//
//        targetType.getMethods().add(new JavaMethodBuilder()
//                .addModifier("public")
//                .setName(getterMethod)
//                .setResultType(commandFieldType)
//                .addBodyLines(String.format("return this.%s;", fieldName)).build()
//        );
//    }
//
//    @Override
//    public void handle(Descriptor descriptor, JavaType targetType, JavaField targetField, JavaMethod fieldMethod) {
//        if (targetField.getModifiers().contains("transient")) {
//            return;
//        }
//        if (fieldMethod.getName().startsWith("set")) {
//            handleFieldCommand(descriptor, targetType, targetField, fieldMethod);
//        }
//    }
//
//
//    public void handleFieldCommand(Descriptor descriptor, JavaType targetType, JavaField targetField, JavaMethod fieldMethod) {
//        CommandConfiguration configuration = getConfiguration(descriptor);
//        boolean isThisFieldAnnotated =  JavaTypeUtil.containsAnnotation(targetField.getAnnotations(), configuration.getIdAnnotation());
//        Collection<InvocationParameter> invocationParameters = getTargetParameterInvocation(targetType, targetField, isThisFieldAnnotated);
//        buildCommandFieldImplementationClass(targetType, targetField, configuration, invocationParameters);
//
//
//        InvocationMeta fieldCommandInvocation = invocationParser.parse(configuration.getFieldCommandSuperType(), invocationParameters);
//        CommandConfiguration.RegistryField commandRegistry = configuration.getRegistryField();
//        InvocationMeta fieldMetaType = invocationParser.parse(commandRegistry.getType(), invocationParameters);
//
//        String superTypeSimpleName = javaTypeImporter.getSimpleTypeName(JavaTypeUtil.getSuperTypeIfDefined(targetType));
//        String commandTypeNameForThisField = StringUtil.format(CaseFormat.UPPER_CAMEL, targetField.getName(), superTypeSimpleName, CaseFormat.LOWER_CAMEL);
//        JavaTypeBuilder commandInnerClassBuilder = createCommandInnerClass(commandTypeNameForThisField, targetType, fieldCommandInvocation, invocationParameters);
//
//
//        JavaMethodBuilder javaMethodBuilder = createExecuteFieldMethod(configuration, targetType, fieldMethod);
//        commandInnerClassBuilder.addMethod(javaMethodBuilder.build());
//        targetType.getNestedJavaTypes().add(commandInnerClassBuilder.build());
//
//
//        List<JavaParameter> constructorParameters = commandInnerClassBuilder.getConstructors().get(0).getParameters();
//        List<String> commandConstructorParameters = new ArrayList<String>();
//        for (JavaParameter parameter : constructorParameters) {
//            if (targetField.getName().equals(parameter.getName())) {
//                commandConstructorParameters.add(targetField.getName());
//            } else if (parameter.getName().startsWith("previous")) {
//                String getterMethod = StringUtil.format(CaseFormat.LOWER_CAMEL, "get", targetField.getName(), CaseFormat.LOWER_CAMEL);
//                commandConstructorParameters.add(getterMethod + "()");
//            } else {
//                throw new IllegalStateException("Unknown parameter " + parameter.getName());
//            }
//        }
//       // namedParameters.put("command", String.format("new %s(%s)", commandInnerClassBuilder.getSimpleName(), Joiner.on(", ").join(commandConstructorParameters)));
//       // Collection<String> initBody = addCommand(configuration, namedParameters);
//      //  fieldMethod.getBodyLines().addAll(initBody);
//    }
//
//    protected void buildCommandFieldImplementationClass(JavaType targetType, JavaField targetField, CommandConfiguration configuration, Collection<InvocationParameter> invocationParameters) {
//        String superTypeSimpleName = javaTypeImporter.getSimpleTypeName(JavaTypeUtil.getSuperTypeIfDefined(targetType));
//
//        //To change body of created methods use File | Settings | File Templates.
//    }
//
//
//    /**
//     * Sets a command interface
//     */
//    protected void setCommandInterface(CommandConfiguration configuration, JavaType targetType, Collection<InvocationParameter> invocationParameters) {
//        String interfaceName = StringUtil.substitute(configuration.getInterface(), InvocationParameterUtil.nameToSimpleClassName(invocationParameters));
//        Type commandInterface = new TypeNameWrapper(interfaceName);
//        targetType.getSuperInterfaces().add(commandInterface);
//        targetType.getImportTypes().add(commandInterface);
//
//    }
//
//
//    @Override
//    public void handle(Descriptor descriptor, JavaType targetType, JavaMethod targetMethod) {
//        List<JavaParameter> methodParameters = targetMethod.getParameters();
//        String commandTypeNameForThisMethod = String.format("%s%sCommand", targetMethod.getName(), JavaTypeUtil.getArgumentTypesHashCode(methodParameters));
//        CommandConfiguration configuration = getConfiguration(descriptor);
//        Collection<InvocationParameter> methodInvocationParameters = getInvocationParameters(targetType, targetMethod.getParameters());
//        CommandConfiguration.RegistryField field = configuration.getRegistryField();
//        InvocationMeta fieldMetaType = invocationParser.parse(field.getType(), methodInvocationParameters);
//        targetType.getImportTypes().add(fieldMetaType.getType());
//
//        InvocationMeta fieldInitMeta = invocationParser.parse(field.getInitValue(), methodInvocationParameters);
//        targetType.getImportTypes().add(fieldInitMeta.getType());
//        targetType.getImportTypes().add(new TypeNameWrapper(fieldInitMeta.getOwnerType()));
//        String initValue = invocationFragmentBuilder.build(fieldInitMeta);
//        targetMethod.getBodyLines().add(String.format("this.%s = %s;", field.getName(), initValue));
//
//        InvocationMeta constructorCommandMeta = invocationParser.parse(configuration.getConstructorCommandSuperType(), methodInvocationParameters);
//        JavaTypeBuilder commandInnerClassBuilder = createCommandInnerClass(commandTypeNameForThisMethod, targetType, constructorCommandMeta, methodInvocationParameters);
//        commandInnerClassBuilder.setNested(true);
//        commandInnerClassBuilder.addModifiers("public", "static");
//        commandInnerClassBuilder.getDocumentation().clear();
//        JavaMethodBuilder javaMethodBuilder = createMethodExecuteFieldMethod(configuration, targetType, targetMethod, commandInnerClassBuilder);
//        commandInnerClassBuilder.addMethod(javaMethodBuilder.build());
//        targetType.getNestedJavaTypes().add(commandInnerClassBuilder.build());
//    }
//
//    private JavaMethodBuilder createMethodExecuteFieldMethod(CommandConfiguration configuration, JavaType targetType, JavaMethod method, JavaTypeBuilder commandInnerClassBuilder) {
//        JavaMethodBuilder result = new JavaMethodBuilder();
//        result.addModifier("public").setName("execute");
//        result.addAnnotation(new SuppressWarningsBuilder().addValue("unchecked").build());
//        result.addParameters(new JavaParameterBuilder().setName("instance")
//                .setType(JavaTypeUtil.getSuperTypeIfDefined(targetType))
//                .build());
//        return result;
//    }
//
//
//
//    @Override
//    public void handle(Descriptor descriptor, JavaType targetType, JavaConstructor javaConstructor) {
//
////        List<JavaParameter> constructorParameters = javaConstructor.getParameters();
////        int constructorHashCode = JavaTypeUtil.getArgumentTypesHashCode(constructorParameters);
////        CommandConfiguration configuration = getConfiguration(descriptor);
////        String superTypeSimpleName = javaTypeImporter.getSimpleTypeName(JavaTypeUtil.getSuperTypeIfDefined(targetType));
////        StringBuilder typeClassBuilder = new StringBuilder();
////        for (Type type : JavaTypeUtil.getParameterTypes(constructorParameters)) {
////            if (typeClassBuilder.length() > 0) typeClassBuilder.append(",");
////            typeClassBuilder.append(javaTypeImporter.getSimpleTypeName(ReflectUtil.getRawClass(type))).append(".class");
////        }
////        Map<String, String> namedParameters = MapMaker.make(String.class,
////                "ownerType", targetType.getSimpleName() + ".class",
////                "ownerSuperType", superTypeSimpleName + ".class",
////                "values", "values",
////                "types", "new Class[]{" + typeClassBuilder.toString() + "}"
////        );
////        Map<String, Type> typeParameters = MapMaker.make(Type.class,
////                "ownerType", new TypeNameWrapper(targetType.getName()),
////                "ownerSuperType", JavaTypeUtil.getSuperTypeIfDefined(targetType),
////                "values", Object[].class,
////                "types", Class[].class
////        );
////        String commandTypeNameForThisConstructor = "Constructor" + constructorHashCode + "Command";
////        StringBuilder constructorArguments = new StringBuilder();
////        for (JavaParameter parameter : constructorParameters) {
////            if (constructorArguments.length() > 0) constructorArguments.append(", ");
////            constructorArguments.append(parameter.getName());
////        }
////        namedParameters.put("constructorCommand", String.format("new %s(new Object[]{%s})", commandTypeNameForThisConstructor, constructorArguments.toString()));
////
////
////        CommandConfiguration.RegistryField field = configuration.getRegistryField();
////        InvocationMeta fieldMetaType = invocationParser.parse(field.getType(), namedParameters, typeParameters);
////        targetType.getImportTypes().add(fieldMetaType.getType());
////        InvocationMeta fieldInitMeta = invocationParser.parse(field.getInitValue(), namedParameters, typeParameters);
////        targetType.getImportTypes().add(fieldInitMeta.getType());
////        targetType.getImportTypes().add(new TypeNameWrapper(fieldInitMeta.getOwnerType()));
////
////        String initValue = invocationFragmentBuilder.build(fieldInitMeta);
////        javaConstructor.getBodyLines().add(String.format("this.%s = %s;", field.getName(), initValue));
////
////
////        InvocationMeta constructorCommandMeta = invocationParser.parse(configuration.getConstructorCommandSuperType(), namedParameters, typeParameters);
////        JavaTypeBuilderImpl commandInnerClassBuilder = createCommandInnerClass(commandTypeNameForThisConstructor, targetType, configuration, constructorCommandMeta, typeParameters);
////        commandInnerClassBuilder.setNested(true);
////        commandInnerClassBuilder.addModifiers("public", "static");
////        commandInnerClassBuilder.getDocumentation().clear();
////        JavaMethodBuilder javaMethodBuilder = createConstructorExecuteFieldMethod(configuration, targetType, javaConstructor, commandInnerClassBuilder);
////        commandInnerClassBuilder.addMethod(javaMethodBuilder.build());
////        targetType.getNestedJavaTypes().add(commandInnerClassBuilder.build());
//
//
//
//    }
//
//
////    private Collection<String> addCommand(CommandConfiguration
////                                                  configuration, Map<String, String> namedParameters) {
////        InvocationMeta addCommandMeta = invocationParser.parse(configuration.getAddCommandMethod(), namedParameters);
////        String result = invocationFragmentBuilder.build(addCommandMeta);
////
////        if (!result.endsWith(";")) result = result + ";";
////        return Arrays.asList(result);
////    }
//
//    protected JavaMethodBuilder createExecuteFieldMethod(CommandConfiguration configuration, JavaType targetType, JavaMethod javaMethod) {
//        JavaMethodBuilder result = new JavaMethodBuilder();
//        result.addModifier("public").setName("execute").setResultType(void.class).addParameter("instance", JavaTypeUtil.getSuperTypeIfDefined(targetType));
//        result.addBodyLines(String.format("instance.%s(%s);", javaMethod.getName(), configuration.getValueAccessor()));
//        return result;
//    }
//
//
//    protected JavaMethodBuilder createConstructorExecuteFieldMethod(CommandConfiguration configuration, JavaType targetType, JavaConstructor javaConstructor, JavaTypeBuilder commandInnerClassBuilder) {
//        JavaMethodBuilder result = new JavaMethodBuilder();
//        result.addModifier("public").setName("execute");
//        result.addAnnotation(new SuppressWarningsBuilder().addValue("unchecked").build());
//        result.setResultType(JavaTypeUtil.getSuperTypeIfDefined(targetType));
//        StringBuilder constructorParameterBuilder = new StringBuilder();
//        String valuesAccessor = configuration.getValuesAccessor();
//        List<JavaParameter> javaParameters = javaConstructor.getParameters();
//        for (int i = 0; i < javaParameters.size(); i++) {
//            JavaParameter javaParameter = javaParameters.get(i);
//            Type argumentType = javaParameter.getType();
//            String simpleArgumentTypeName = javaTypeImporter.getSimpleTypeName(ReflectUtil.getObjectType(argumentType));
//            if (constructorParameterBuilder.length() > 0) constructorParameterBuilder.append(", ");
//            constructorParameterBuilder.append(String.format("(%s)%s[%s]", simpleArgumentTypeName, valuesAccessor, i));
//        }
//        result.addBodyLines(String.format("return new %s(%s);", targetType.getSimpleName(), constructorParameterBuilder.toString()));
//        return result;
//    }
//
//    protected JavaTypeBuilder createCommandInnerClass(String commandTypeName, JavaType targetType, InvocationMeta commandMeta, Collection<InvocationParameter> invocationParameters) {
//        SimpleClassBuilderImpl result = new SimpleClassBuilderImpl(targetType, new DescriptorBuilder().build(), new JavaTypeRegistryImpl());
//        result.setName(commandTypeName);
//        result.setPackageName("");
//        result.setNested(true);
//        result.getDocumentation().clear();
//        result.addModifiers("public", "static");
//        Type superType = commandMeta.getType();
//        result.setSuperType(superType);
//        targetType.getImportTypes().add(superType);
//        JavaConstructorBuilder constructorBuilder = new JavaConstructorBuilder();
//        constructorBuilder.setName(result.getSimpleName());
//        constructorBuilder.addModifier("public");
//        Map<String, InvocationParameter> namedParameters = Maps.uniqueIndex(invocationParameters, new InvocationParameterName());
//        for (int i = 0; i < commandMeta.getRawParameters().size(); i++) {
//            String rawArgumentName = commandMeta.getRawParameters().get(i);
//            String argumentName = commandMeta.getParameters().get(i);
//            InvocationParameter parameter = namedParameters.get(rawArgumentName);
//            if (parameter == null) {
//                throw new UnsupportedOperationException("unsupported generic source type '" + rawArgumentName + "' out of " + namedParameters.keySet());
//            }
//            if (!parameter.isDynamic()) {
//                continue;
//            }
//            constructorBuilder.addParameter(argumentName, parameter.getType());
//        }
//        constructorBuilder.addBodyLines(String.format("super(%s);", Joiner.on(", ").join(commandMeta.getParameters())));
//        result.addConstructor(constructorBuilder.build());
//        return result;
//    }
//
//
//    @SuppressWarnings("unchecked")
//    public CommandConfiguration getConfiguration(Descriptor descriptor) {
//        Map options = DecoderUtil.readPrefixedStringToStringMap(descriptor.getOptions(), "commandHandler");
//        if (options.containsPath(CommandConfiguration.class.getName())) {
//            return CommandConfiguration.class.cast(options.get(CommandConfiguration.class.getName()));
//        }
//        CommandConfiguration handlerConfiguration = decoderHandler.decode(options);
//        options.put(CommandConfiguration.class.getName(), handlerConfiguration);
//        return handlerConfiguration;
//    }
//
//
//
//
//    protected boolean isWrappedType(CommandConfiguration configuration, Type type) {
//        Class rawType = ReflectUtil.getRawClass(type);
//        Map<String, String> wrappedValues = configuration.getWrappedValues();
//        return wrappedValues.containsPath(rawType.getName());
//    }
//
//
//    protected String getWrappedType(CommandConfiguration configuration, Type type) {
//        Class rawType = ReflectUtil.getRawClass(type);
//        Map<String, String> wrappedValues = configuration.getWrappedValues();
//        return wrappedValues.get(rawType.getName());
//    }
//
//
//    protected String getWrappedBody(JavaType ownerType, CommandConfiguration configuration, JavaField targetField) {
//        Class rawType = ReflectUtil.getRawClass(targetField.getType());
//        Type[] typeArguments = ReflectUtil.getGenericActualTypeArguments(targetField.getType());
//        Collection<InvocationParameter> invocationParameters = getTargetParameterInvocation(ownerType, targetField, false);
//        if (typeArguments != null && typeArguments.length > 0) {
//            int componentIndex = 0;
//            if (Map.class.isAssignableFrom(rawType)) {
//                componentIndex = 1;
//                Type keyType = typeArguments[0];
//                invocationParameters.add(new InvocationParameterBuilder()
//                        .setName("K")
//                        .setValue(javaTypeImporter.getSimpleTypeName(keyType) + ".class")
//                        .setDynamic(false)
//                        .setClassName(javaTypeImporter.getSimpleTypeName(keyType))
//                        .setType(keyType)
//                        .build());
//
//            }
//            Type componentType = typeArguments[componentIndex];
//            invocationParameters.add(new InvocationParameterBuilder()
//                    .setName("V")
//                    .setValue(javaTypeImporter.getSimpleTypeName(componentType) + ".class")
//                    .setDynamic(false)
//                    .setClassName(javaTypeImporter.getSimpleTypeName(componentType))
//                    .setType(componentType)
//                    .build());
//
//        }
//        InvocationMeta wrappedValueInvocation = invocationParser.parse(getWrappedType(configuration, targetField.getType()), invocationParameters);
//        return invocationFragmentBuilder.build(wrappedValueInvocation);
//    }
//
//
//
//
//
//    /**
//     * Returns collection of owner and owner super type invocation parameters:
//     * <ul>
//     *     <li>ownerType</li>
//     *     <li>ownerSuperType</li>
//     * </ul>
//     * @param ownerType java owner type
//     * @return invocation parameter collection
//     */
//    protected Collection<InvocationParameter> getOwnerTypeParameters(JavaType ownerType) {
//        Collection<InvocationParameter> result = new ArrayList<InvocationParameter>();
//        result.add(new InvocationParameterBuilder()
//                .setName("ownerType")
//                .setValue(ownerType.getSimpleName())
//                .setClassName(ownerType.getName())
//                .setType(new TypeNameWrapper(ownerType.getName())).build());
//
//        result.add(new InvocationParameterBuilder()
//                .setName("ownerSuperType")
//                .setValue(javaTypeImporter.getSimpleTypeName(JavaTypeUtil.getSuperTypeIfDefined(ownerType)))
//                .setClassName(javaTypeImporter.getTypeName(JavaTypeUtil.getSuperTypeIfDefined(ownerType)))
//                .setType(JavaTypeUtil.getSuperTypeIfDefined(ownerType)).build());
//
//        return result;
//    }
//
//    /**
//     * Returns collection of java parameters and a owner type
//     * <ul>
//     *     <li>values</li>
//     *     <li>types</li>
//     * </ul>
//     *
//     * See owner type parameters {@link CommandPluginHandler#getOwnerTypeParameters}
//     * @param targetType
//     * @param targetParameters
//     * @return
//     */
//    protected Collection<InvocationParameter> getInvocationParameters(JavaType targetType, List<JavaParameter> targetParameters) {
//        Collection<InvocationParameter> result = getOwnerTypeParameters(targetType);
//        result.add(
//                new InvocationParameterBuilder()
//                        .setName("values")
//                        .setValue("values")
//                        .setDynamic(true)
//                        .setClassName(targetType.getName())
//                        .setType(Object[].class).build()
//        );
//
//        List<String> simpleTypeNames = new ArrayList<String>();
//        for(JavaParameter parameter: targetParameters) {
//            simpleTypeNames.add(javaTypeImporter.getSimpleTypeName(ReflectUtil.getObjectType(parameter.getType())));
//        }
//        result.add(
//                new InvocationParameterBuilder()
//                        .setName("types")
//                        .setValue(Joiner.on(",").join(simpleTypeNames))
//                        .setDynamic(false)
//                        .setClassName(targetType.getName())
//                        .setType(Object[].class).build()
//        );
//        return result;
//    }
//
//
//    /**
//     * Returns a field and type owner invocation parameters:
//     * <ul>
//     *     <li>value - new field value</li>
//     *     <li>previousValue -  previous field value</li>
//     *     <li>type - field type</li>
//     *     <li>isIdAnnotated - boolean value</li>
//     * </ul>
//     * @param targetType
//     * @param targetField
//     * @param isIdAnnotated
//     * @return
//     */
//    protected Collection<InvocationParameter> getTargetParameterInvocation(JavaType targetType, JavaField targetField, boolean isIdAnnotated) {
//        Collection<InvocationParameter> result = getOwnerTypeParameters(targetType);
//        result.add(
//                new InvocationParameterBuilder()
//                        .setName("fieldName")
//                        .setValue("\"" + targetField.getName() + "\"")
//                        .setDynamic(false)
//                        .setClassName(String.class.getName())
//                        .setType(String.class)
//                        .setType(targetField.getType()).build()
//        );
//        result.add(
//                new InvocationParameterBuilder()
//                        .setName("value")
//                        .setValue(targetField.getName())
//                        .setDynamic(true)
//                        .setClassName(javaTypeImporter.getTypeName(targetField.getType()))
//                        .setType(targetField.getType()).build()
//        );
//
//        result.add(
//                new InvocationParameterBuilder()
//                        .setName("previousValue")
//                        .setValue("previous" + CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, targetField.getName()))
//                        .setDynamic(true)
//                        .setClassName(javaTypeImporter.getTypeName(targetField.getType()))
//                        .setType(targetField.getType()).build()
//        );
//
//        result.add(
//                new InvocationParameterBuilder()
//                        .setName("isIdAnnotated")
//                        .setValue(isIdAnnotated ? "true" : "false")
//                        .setDynamic(false)
//                        .setClassName(boolean.class.getName())
//                        .setType(boolean.class).build()
//        );
//
//        Type fieldType = targetField.getType();
//        if (ReflectUtil.isPrimitiveType(fieldType)) {
//            fieldType = ReflectUtil.getPrimitiveCounterType(Class.class.cast(fieldType));
//        }
//        result.add(
//                new InvocationParameterBuilder()
//                        .setName("type")
//                        .setValue(javaTypeImporter.getSimpleTypeName(targetField.getName()) + ".class")
//                        .setDynamic(false)
//                        .setClassName(javaTypeImporter.getTypeName(targetField.getType()))
//                        .setType(fieldType).build()
//        );
//        return result;
//    }
//


}

        
