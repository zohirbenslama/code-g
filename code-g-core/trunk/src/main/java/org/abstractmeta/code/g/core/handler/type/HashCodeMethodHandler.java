package org.abstractmeta.code.g.core.handler.type;


import org.abstractmeta.code.g.code.JavaField;
import org.abstractmeta.code.g.code.JavaType;
import org.abstractmeta.code.g.config.Descriptor;
import org.abstractmeta.code.g.core.code.builder.JavaMethodBuilder;
import org.abstractmeta.code.g.core.code.builder.JavaTypeBuilder;
import org.abstractmeta.code.g.core.util.DescriptorUtil;
import org.abstractmeta.code.g.core.util.JavaTypeUtil;
import org.abstractmeta.code.g.core.util.ReflectUtil;
import org.abstractmeta.code.g.handler.JavaTypeHandler;

import java.lang.reflect.Type;

/**
 * Represents  HashCodeMethodHandler.
 * <p/>
 * Descriptor options
 * <ul>
 * <li>generateHashCodeMethod - is set to true flag used to generate hashCode method</li>
 * <li>generateHashCodeMethod.hashFieldAnnotation - if specified only field with this annotation will be used in hashCode calculation</li>
 * </ul>
 *
 */
public class HashCodeMethodHandler implements JavaTypeHandler {

    public static final String GENERATE_HASH_CODE = "generateHashCodeMethod";
    public static int HASH_MULTIPLIER = 29;

    private final Descriptor descriptor;
    private final JavaTypeBuilder ownerTypeBuilder;

    public HashCodeMethodHandler(JavaTypeBuilder ownerTypeBuilder, Descriptor descriptor) {
        this.ownerTypeBuilder = ownerTypeBuilder;
        this.descriptor = descriptor;
    }


    protected boolean generateHashCode() {
        return DescriptorUtil.is(descriptor, GENERATE_HASH_CODE);
    }


    @Override
    public void handle(JavaType sourceType) {
        if (!generateHashCode()) return;

        JavaMethodBuilder resultBuilder = new JavaMethodBuilder();
        resultBuilder.setName("hashCode").addModifier("public").setResultType(int.class);
        resultBuilder.addBody("int result = 0;");
        String hashAnnotation = DescriptorUtil.get(descriptor, "generateHashCodeMethod.hashFieldAnnotation");
        for (JavaField field : ownerTypeBuilder.getFields()) {
            if (field.getModifiers().contains("transient")) continue;
            if (hashAnnotation != null) {
                if (!JavaTypeUtil.containsAnnotation(field.getAnnotations(), hashAnnotation)) {
                    continue;
                }
            }
            addHashFieldCalulation(resultBuilder, field);

        }
        resultBuilder.addBody("return result;");
        ownerTypeBuilder.addMethod(resultBuilder.build());
    }


    private void addHashFieldCalulation(JavaMethodBuilder resultBuilder, JavaField field) {
        Type filedType = field.getType();
        String fieldName = "this." + field.getName();
        if (ReflectUtil.isPrimitiveType(filedType)) {
            resultBuilder.addBody("result += " + HASH_MULTIPLIER + " * " + fieldName + ";");
        } else if (ReflectUtil.isArrayType(filedType)) {
            resultBuilder.addBody("result += " + HASH_MULTIPLIER + " * (" + fieldName + " != null ? Arrays.hashCode(" + fieldName + ") : 0);");
        } else {
            resultBuilder.addBody("result += " + HASH_MULTIPLIER + " * (" + fieldName + " != null ? " + fieldName + ".hashCode() : 0);");
        }
    }

}

        
