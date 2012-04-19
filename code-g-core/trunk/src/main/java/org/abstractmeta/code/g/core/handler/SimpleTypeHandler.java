package org.abstractmeta.code.g.core.handler;

import org.abstractmeta.code.g.code.JavaConstructor;
import org.abstractmeta.code.g.code.JavaField;
import org.abstractmeta.code.g.code.JavaType;
import org.abstractmeta.code.g.core.code.builder.JavaConstructorBuilder;
import org.abstractmeta.code.g.core.code.builder.JavaTypeBuilder;
import org.abstractmeta.code.g.core.collection.predicates.ConstructorArgumentPredicate;
import org.abstractmeta.code.g.core.util.JavaTypeUtil;
import org.abstractmeta.code.g.core.util.ReflectUtil;
import org.abstractmeta.code.g.handler.JavaTypeHandler;
import com.google.common.collect.Iterables;

import java.lang.reflect.Type;

/**
 * This handler creates constructor for all fields defined on the owner type.
 * In case where all fields are mutable the empty constructor is added.
 * If builder was not able to generated all methods from source class class is marked as abstract.
 *
 * @author Adrian Witas
 */
public class SimpleTypeHandler implements JavaTypeHandler {

    private final JavaTypeBuilder ownerTypeBuilder;

    public SimpleTypeHandler(JavaTypeBuilder ownerTypeBuilder) {
        this.ownerTypeBuilder = ownerTypeBuilder;
    }


    @Override
    public void handle(JavaType sourceType) {
        if (ownerTypeBuilder.getConstructors().size() > 0) {
            return;
        }
        JavaConstructorBuilder constructorBuilder = new JavaConstructorBuilder();
        addSuperCall(sourceType, constructorBuilder);
        addConstructorParameters(sourceType,  constructorBuilder);
        constructorBuilder.setName(ownerTypeBuilder.getSimpleName());
        constructorBuilder.addModifier("public");
        if (!JavaTypeUtil.isMethodCompatible(sourceType, ownerTypeBuilder)) {
            ownerTypeBuilder.addModifier("abstract");
        }
        ownerTypeBuilder.addConstructor(constructorBuilder.build());
    }

    protected void addConstructorParameters(JavaType sourceType, JavaConstructorBuilder constructorBuilder) {
            Iterable<JavaField> constructorArguments = Iterables.filter(ownerTypeBuilder.getFields(),new ConstructorArgumentPredicate(sourceType));
        for(JavaField field: constructorArguments) {
                 String fieldName = field.getName();
            constructorBuilder.addArgumentName(field.getName());
            constructorBuilder.addArgumentType(field.getType());
            constructorBuilder.addBody(String.format("this.%s = %s;", fieldName, fieldName));
        }
    }


    protected void addSuperCall(JavaType sourceType, JavaConstructorBuilder constructorBuilder) {
        if (sourceType.getConstructors().size() == 0) {
            return;
        }
        JavaConstructor constructorCandidate = null;
        for (JavaConstructor constructor : sourceType.getConstructors()) {
            if (constructor.getParameterNames().size() == 0) {
                return;
            }
            if (constructorCandidate == null) {
                constructorCandidate = constructor;
            }
            if (constructorCandidate.getParameterNames().size() > constructor.getParameterNames().size()) {
                constructorCandidate = constructor;
            }
        }

        StringBuilder result = new StringBuilder();
        for (Type parameterType : constructorCandidate.getParameterTypes()) {
            Class parameterRawClass = ReflectUtil.getRawClass(parameterType);
            if (result.length() > 0) {
                result.append(", ");
            }
            if (parameterRawClass.isPrimitive()) {
                result.append("0");
            } else {
                result.append("null");
            }
        }
        constructorBuilder.addBody("super(" + result.toString() + ");");
    }

}
