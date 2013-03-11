package org.abstractmeta.code.g.code;

import org.abstractmeta.code.g.code.handler.ConstructorHandler;
import org.abstractmeta.code.g.code.handler.FieldHandler;
import org.abstractmeta.code.g.code.handler.MethodHandler;
import org.abstractmeta.code.g.code.handler.TypeHandler;
import org.abstractmeta.code.g.config.Descriptor;
import org.abstractmeta.code.g.generator.Context;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * <p>Represents a java type builder.</p>
 *
 * This abstraction provides comprehensive way of building java type.
 * Any boilerplate code generation can be delegated to specialised handlers.
 *
 * <pre>
 *    JavaTypeBuilder javaTypeBuilder = ...;
 *    javaTypeBuilder.setName("com.test.Foo");
 *    javaTypeBuilder.addFieldHandler(new SetterAndGetterHandler());
 *    javaTypeBuilder.addMethodHandler(new CommandPatternHandler());
 *
 *    javaTypeBuilder.addFields(javaField); //generates also getter and setter for this field
 *    // and for each setter method command for a field change
 *
 *    javaTypeBuilder.setContext(context);// building context
 *    JavaType javaType = javaTypeBuilder.build();
 *
 *
 *    JavaFieldRenderer renderer = ...
 *    render.render(javaType,  javaTypeBuilder.getImporter(), 4);
 *    //renders java source code for a defined type
 *
 * </pre>
 *
 * @author Adrian Witas
 */
public interface JavaTypeBuilder extends JavaType {

    JavaType getSourceType();

    JavaTypeImporter getImporter();

    JavaTypeBuilder addFieldHandlers(FieldHandler ... fieldHandler);

    JavaTypeBuilder addTypeHandlers(TypeHandler ... typeHandler);

    JavaTypeBuilder addMethodHandlers(MethodHandler ... methodHandler);

    JavaTypeBuilder addConstructorHandlers(ConstructorHandler ... constructorHandler);

    boolean containsField(String fieldName);

    JavaField getFiled(String fieldName);

    /**
     * Adds a field to this builder, all specified field handlers are called.
     * @param field java field
     * @return this builder
     */
    JavaTypeBuilder addField(JavaField field);

    JavaTypeBuilder addFields(JavaField ... fields);

    JavaTypeBuilder addFields(Collection<JavaField> fields);

    boolean containsMethod(String methodName, JavaParameter ... parameters);

    JavaMethod getMethod(String methodName, JavaParameter ... parameters);

    /**
     * Adds a method to this builder, all specified method handlers are called.
     * @param method java method
     * @return this builder
     */
    JavaTypeBuilder addMethod(JavaMethod method);

    List<JavaMethod> getMethods(String methodName);

    JavaTypeBuilder addMethods(JavaMethod ... methods);

    JavaTypeBuilder addMethods(Collection<JavaMethod> methods);

    /**
     * Adds a field to this builder, all specified constructor handlers are called.
     * @param constructor java constructor
     * @return this builder
     */
    JavaTypeBuilder addConstructor(JavaConstructor constructor);

    JavaTypeBuilder addConstructors(JavaConstructor ... constructors);

    JavaTypeBuilder addConstructors(Collection<JavaConstructor> constructors);

    JavaTypeBuilder addSuperInterfaces(Type ... superInterfaces);

    JavaTypeBuilder addSuperInterfaces(Collection<Type> superInterfaces);

    JavaTypeBuilder addImportTypes(Type ... importTypes);

    JavaTypeBuilder addImportTypes(Collection<Type> importTypes);

    JavaTypeBuilder addBodyLines(String ... bodyLines);

    JavaTypeBuilder addBodyLines(Collection<String> bodyLines);

    JavaTypeBuilder setSuperType(Type superType);

    JavaTypeBuilder addNestedJavaTypes(JavaType ... classTypes);

    JavaTypeBuilder addNestedJavaTypes(Collection<JavaType> classTypes);

    JavaTypeBuilder addModifiers(Collection<JavaModifier> modifiers);

    JavaTypeBuilder addModifiers(JavaModifier... modifiers);

    JavaTypeBuilder addAnnotations(Annotation ... annotations);

    JavaTypeBuilder addAnnotations(Collection<Annotation> annotations);

    JavaTypeBuilder addDocumentations(String ... documentations);

    JavaTypeBuilder addDocumentation(Collection<String> documentation);

    JavaTypeBuilder setNested(boolean nested);

    JavaTypeBuilder addGenericTypeArguments(Type... genericTypeArguments);

    JavaTypeBuilder addGenericTypeArguments(Collection<Type> genericTypeArguments);

    JavaTypeBuilder addGenericTypeVariables(Map<String, Type> genericTypeVariables);

    JavaTypeBuilder addGenericTypeVariable(String key, Type value);

    Context getContext();
    /**
     * Create a new instance for all defined parameters, all specified type handlers are called.
     * @return java builder
     */
    JavaType build();

    JavaTypeBuilder merge(JavaType instance);


}
