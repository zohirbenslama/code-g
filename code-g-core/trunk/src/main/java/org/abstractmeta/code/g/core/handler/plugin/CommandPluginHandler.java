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

import com.google.common.base.CaseFormat;
import com.google.common.base.Joiner;
import org.abstractmeta.code.g.code.*;
import org.abstractmeta.code.g.config.Descriptor;
import org.abstractmeta.code.g.core.builder.SimpleClassBuilder;
import org.abstractmeta.code.g.core.code.JavaTypeImporterImpl;
import org.abstractmeta.code.g.core.code.builder.JavaConstructorBuilder;
import org.abstractmeta.code.g.core.code.builder.JavaFieldBuilder;
import org.abstractmeta.code.g.core.code.builder.JavaMethodBuilder;
import org.abstractmeta.code.g.core.code.builder.JavaTypeBuilder;
import org.abstractmeta.code.g.core.config.builder.DescriptorBuilder;
import org.abstractmeta.code.g.core.config.command.CommandHandlerConfiguration;
import org.abstractmeta.code.g.core.config.command.CommandHandlerConfigurationDecoder;
import org.abstractmeta.code.g.core.config.command.CommandConfiguration;
import org.abstractmeta.code.g.core.internal.TypeNameWrapper;
import org.abstractmeta.code.g.core.util.*;
import org.abstractmeta.code.g.handler.JavaPluginHandler;

import java.lang.reflect.Type;
import java.util.*;

/**
 * Represents  CommandPluginHandler.
 * This plugin uses simple class builder to implement command pattern on target instance.
 *
 * See configuration option {@link CommandConfiguration}
 *
 * TODO update documentation
 *
 * TODO add method command support
 * TODO add constructor command support
 *
 */
public class CommandPluginHandler implements JavaPluginHandler {

    private final JavaTypeImporter javaTypeImporter;
    private final CommandHandlerConfigurationDecoder decoderHandler;

    public CommandPluginHandler() {
        this(new CommandHandlerConfigurationDecoder());
    }

    public CommandPluginHandler(CommandHandlerConfigurationDecoder decoderHandler) {
        this.decoderHandler = decoderHandler;
        this.javaTypeImporter = new JavaTypeImporterImpl("");
    }


    @Override
    public void handle(Descriptor descriptor, JavaType targetType) {
        CommandHandlerConfiguration commandHandlerConfiguration = getConfiguration(descriptor);
        targetType.getImportTypes().add(Arrays.class);
        Type commandFieldType = new TypeNameWrapper(commandHandlerConfiguration.getFieldType());
        String fieldName = commandHandlerConfiguration.getFieldName();
        targetType.getImportTypes().add(commandFieldType);
        targetType.getFields().add(new JavaFieldBuilder().addModifiers("private", "transient")
                .setType(commandFieldType)
                .setName(fieldName).build());

        String getterMethod = StringUtil.format(CaseFormat.LOWER_CAMEL, "get", fieldName, CaseFormat.LOWER_CAMEL);

        targetType.getMethods().add(new JavaMethodBuilder()
                .addModifier("public")
                .setName(getterMethod)
                .setResultType(commandFieldType)
                .addBody(String.format("return this.%s;", fieldName)).build()

        );

        Type commandInterface = new TypeNameWrapper(commandHandlerConfiguration.getInterface());
        targetType.getSuperInterfaces().add(commandInterface);
    }


    @Override
    public void handle(Descriptor descriptor, JavaType targetType, JavaField targetField, JavaMethod fieldMethod) {
        if (targetField.getModifiers().contains("transient")) {
            return;
        }

        if (fieldMethod.getName().startsWith("set")) {
            handleFieldCommand(descriptor, targetType, targetField, fieldMethod);
        }


    }


    public void handleFieldCommand(Descriptor descriptor, JavaType targetType, JavaField targetField, JavaMethod fieldMethod) {
        Type fieldType = targetField.getType();
        if (ReflectUtil.isPrimitiveType(fieldType)) {
            fieldType = ReflectUtil.getPrimitiveCounterType(Class.class.cast(fieldType));
        }
        String argument = fieldMethod.getParameterNames().get(0);
        CommandHandlerConfiguration handlerConfiguration = getConfiguration(descriptor);
        CommandConfiguration commandConfiguration = handlerConfiguration.getFieldCommand();
        Map<String, Type> typeParameters = MapUtil.createMap(Type.class,
                "fieldType", fieldType,
                "superType", new TypeNameWrapper(commandConfiguration.getSuperTypeName()),
                "fieldName", String.class,
                "previousValue", fieldType,
                "value", fieldType,
                "isId", Boolean.class,
                "ownerType", new TypeNameWrapper(targetType.getName())
        );


        Map<String, String> namedParameters = MapUtil.createMap(String.class,
                "fieldName", "\"" + targetField.getName() + "\"",
                "fieldType", javaTypeImporter.getSimpleTypeName(fieldType),
                "argument", targetField.getName(),
                "ownerType", targetType.getSimpleName(),
                "previousValue", "this." + targetField.getName(),
                "isId", (JavaTypeUtil.containsAnnotation(targetField.getAnnotations(), commandConfiguration.getIdAnnotation())
                ? "true" : "false"),
                "value", argument
        );
        String superTypeName = JavaTypeUtil.getSimpleClassName(commandConfiguration.getSuperTypeName());
        String commandTypeNameForThisField = StringUtil.format(CaseFormat.UPPER_CAMEL, targetField.getName(), superTypeName, CaseFormat.LOWER_CAMEL);
        JavaTypeBuilder commandInnerClassBuilder = createCommandInnerClass(handlerConfiguration.getImplementationKind(), commandTypeNameForThisField, targetType, commandConfiguration, typeParameters);

        commandInnerClassBuilder.setNested(true);

        commandInnerClassBuilder.getDocumentation().clear();
        if (CommandHandlerConfiguration.ImplementationKind.METHOD.equals(handlerConfiguration.getImplementationKind())) {
            JavaMethodBuilder javaMethodBuilder = createExecuteMethod(targetType, fieldMethod);
            commandInnerClassBuilder.addMethod(javaMethodBuilder.build());

            fieldMethod.getNestedJavaTypes().add(commandInnerClassBuilder.build());
        } else {
            JavaMethodBuilder javaMethodBuilder = createExecuteFieldMethod(commandConfiguration, targetType, fieldMethod);
            commandInnerClassBuilder.addMethod(javaMethodBuilder.build());
            commandInnerClassBuilder.addModifiers("public", "static");
            targetType.getNestedJavaTypes().add(commandInnerClassBuilder.build());
        }

        Collection<String> initBody = addCommand(handlerConfiguration, commandConfiguration, commandInnerClassBuilder.getSimpleName(), targetField.getType(), namedParameters);
        fieldMethod.getBody().addAll(initBody);
    }

    @Override
    public void handle(Descriptor descriptor, JavaType targetType, JavaConstructor javaConstructor) {
        CommandHandlerConfiguration commandHandlerConfiguration = getConfiguration(descriptor);
        Type commandFieldImplementationType = new TypeNameWrapper(commandHandlerConfiguration.getFieldType());
        targetType.getImportTypes().add(commandFieldImplementationType);
        String implementationSimpleTypeName = JavaTypeUtil.getSimpleClassName(commandHandlerConfiguration.getFieldType());
        javaConstructor.getBody().add(String.format("this.%s = new %s();", commandHandlerConfiguration.getFieldName(), implementationSimpleTypeName));
    }


    private Collection<String> addCommand(CommandHandlerConfiguration handlerConfiguration, CommandConfiguration commandConfiguration, String innerTypeName, Type fieldType, Map<String, String> namedParameters) {
        Collection<String> result = new ArrayList<String>();
        String ownerCommandFiledName = handlerConfiguration.getFieldName();

        List<String> newCommandArguments = new ArrayList<String>();

        for (String argumentName : commandConfiguration.getSuperTypeConstructorArguments()) {
            if (!namedParameters.containsKey(argumentName)) {
                throw new UnsupportedOperationException("unsupported argument " + argumentName + ", out of " + namedParameters.keySet());
            }
            newCommandArguments.add(namedParameters.get(argumentName));

        }
        String previousValue = namedParameters.get("previousValue");
        String value = namedParameters.get("value");
        String modificationCriteria;
        if (ReflectUtil.isPrimitiveType(fieldType)) {
            modificationCriteria = String.format("if(%s != %s)", previousValue, value);

        } else if (ReflectUtil.isArrayType(fieldType)) {
            modificationCriteria = String.format("if(! Arrays.equals(%s, %s))", previousValue, value);


        } else {
            modificationCriteria = String.format("if((%s != null && ! %s.equals(%s)) || %s != null)", previousValue, previousValue, value, value);

        }
        result.add(String.format("%s {\n    this.%s.add(new %s(%s));\n}", modificationCriteria, ownerCommandFiledName, innerTypeName, Joiner.on(", ").join(newCommandArguments)));
        return result;
    }


    protected JavaMethodBuilder createExecuteMethod(JavaType targetType, JavaMethod javaMethod) {
        JavaMethodBuilder result = new JavaMethodBuilder();
        result.addModifier("public").setName("execute").setResultType(void.class).addParameter("instance", new TypeNameWrapper(targetType.getName()));
        result.addBody(String.format("instance.%s(%s);", javaMethod.getName(), Joiner.on(",").join(javaMethod.getParameterNames())));
        return result;
    }

    protected JavaMethodBuilder createExecuteFieldMethod(CommandConfiguration commandConfiguration, JavaType targetType, JavaMethod javaMethod) {
        JavaMethodBuilder result = new JavaMethodBuilder();
        result.addModifier("public").setName("execute").setResultType(void.class).addParameter("instance", new TypeNameWrapper(targetType.getName()));
        String getValueFieldName = commandConfiguration.getValueFieldName();
        String getterMethod = StringUtil.format(CaseFormat.LOWER_CAMEL, "get", getValueFieldName, CaseFormat.LOWER_CAMEL);
        result.addBody(String.format("instance.%s(%s());", javaMethod.getName(), getterMethod));
        return result;
    }


    private JavaTypeBuilder createCommandInnerClass(CommandHandlerConfiguration.ImplementationKind implementationKind, String commandTypeName, JavaType targetType, CommandConfiguration commandConfiguration, Map<String, Type> parameters) {
        SimpleClassBuilder result = new SimpleClassBuilder(targetType, new DescriptorBuilder().build());
        result.setName(commandTypeName);
        result.setPackageName("");
        Type superType = getCommandSuperType(commandConfiguration, parameters);
        result.setSuperType(superType);
        JavaConstructorBuilder constructorBuilder = new JavaConstructorBuilder();
        constructorBuilder.setName(result.getSimpleName());
        if (CommandHandlerConfiguration.ImplementationKind.CLASS.equals(implementationKind)) {
            constructorBuilder.addModifier("public");
        }
        for (String argumentName : commandConfiguration.getSuperTypeConstructorArguments()) {
            if (!parameters.containsKey(argumentName)) {
                throw new UnsupportedOperationException("unsupported generic source type " + argumentName + ", out of " + parameters.keySet());
            }
            constructorBuilder.addParameter(argumentName, parameters.get(argumentName));
        }
        String superCallArguments = Joiner.on(", ").join(commandConfiguration.getSuperTypeConstructorArguments());
        constructorBuilder.addBody("super(" + superCallArguments + ");");
        result.addConstructor(constructorBuilder.build());
        return result;
    }


    protected Type getCommandSuperType(CommandConfiguration configuration, Map<String, Type> scopedTypes) {
        List<String> genericTypeNames = configuration.getSuperTypeGenericTypeSources();
        if (genericTypeNames.size() == 0) {
            return new TypeNameWrapper(configuration.getSuperTypeName());
        }
        List<Type> genericTypes = new ArrayList<Type>();
        for (String genericType : genericTypeNames) {
            if (!scopedTypes.containsKey(genericType)) {
                throw new UnsupportedOperationException("unsupported generic source type " + genericType + ", out of " + scopedTypes.keySet());
            }
            genericTypes.add(scopedTypes.get(genericType));
        }
        return new TypeNameWrapper(configuration.getSuperTypeName(), genericTypes.toArray(new Type[]{}));
    }


    @SuppressWarnings("unchecked")
    public CommandHandlerConfiguration getConfiguration(Descriptor descriptor) {
        Map options = DecoderUtil.matchWithPrefix(descriptor.getOptions(), "commandHandler");
        if (options.containsKey(CommandHandlerConfiguration.class.getName())) {
            return CommandHandlerConfiguration.class.cast(options.get(CommandHandlerConfiguration.class.getName()));
        }
        CommandHandlerConfiguration handlerConfiguration = decoderHandler.decode(options);
        options.put(CommandHandlerConfiguration.class.getName(), handlerConfiguration);
        return handlerConfiguration;
    }


}

        
