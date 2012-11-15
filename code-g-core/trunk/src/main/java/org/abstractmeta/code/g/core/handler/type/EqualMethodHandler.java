package org.abstractmeta.code.g.core.handler.type;

import org.abstractmeta.code.g.code.JavaField;
import org.abstractmeta.code.g.code.JavaType;
import org.abstractmeta.code.g.config.Descriptor;
import org.abstractmeta.code.g.core.code.builder.JavaMethodBuilder;
import org.abstractmeta.code.g.core.code.builder.JavaTypeBuilder;
import org.abstractmeta.code.g.core.util.ReflectUtil;
import org.abstractmeta.code.g.handler.JavaTypeHandler;

import java.lang.reflect.Type;
import java.util.Arrays;

/**
 * Represents  HashCodeMethodHandler.
 *
 * @author awitas
 * @version 0.01 14/11/2012
 */
public class EqualMethodHandler implements JavaTypeHandler {

    public static final String GENERATE_HASH_CODE = "generateEqualsMethod";
    public static int HASH_MULTIPLIER = 29;

    private final Descriptor descriptor;
    private final JavaTypeBuilder ownerTypeBuilder;

    public EqualMethodHandler(JavaTypeBuilder ownerTypeBuilder, Descriptor descriptor) {
        this.descriptor = descriptor;
        this.ownerTypeBuilder = ownerTypeBuilder;
    }


    protected boolean generateHashCode() {
        String skipHashMethod = descriptor.getOptions().get(GENERATE_HASH_CODE);
        return ("true".equalsIgnoreCase(skipHashMethod));
    }


    @Override
    public void handle(JavaType sourceType) {
        if (!generateHashCode()) return;

        JavaMethodBuilder resultBuilder = new JavaMethodBuilder();
        resultBuilder.setName("equals").addModifier("public").setResultType(boolean.class).addParameter("objectCandidate", Object.class);
        resultBuilder.addBody("if(objectCandidate == null) return false;");
        resultBuilder.addBody("if(!(objectCandidate instanceof " + ownerTypeBuilder.getSimpleName() + ")) return false;");
        resultBuilder.addBody(ownerTypeBuilder.getSimpleName() + " candidate = " + ownerTypeBuilder.getSimpleName() + ".class.cast(objectCandidate);");

        for (JavaField field : ownerTypeBuilder.getFields()) {
            if (field.getModifiers().contains("transient")) continue;
            addEqualFieldCriteria(resultBuilder, field);

        }
        resultBuilder.addBody("return true;");
        ownerTypeBuilder.addMethod(resultBuilder.build());
    }

    private void addEqualFieldCriteria(JavaMethodBuilder resultBuilder, JavaField field) {
        Type filedType = field.getType();
        String fieldName = "this." + field.getName();
        String candidateFieldName = "candidate." + field.getName();
        if (ReflectUtil.isPrimitiveType(filedType)) {
            resultBuilder.addBody("if(" + fieldName + " != " + candidateFieldName + ") return false;");
        } else if (ReflectUtil.isArrayType(filedType)) {
            resultBuilder.addBody("if(! Arrays.equals(" + fieldName + "," + candidateFieldName + ")) return false;");
        } else {
            resultBuilder.addBody("if(" + fieldName + "  != null &&  ! " + fieldName + ".equals(" + candidateFieldName + ")) return false;");
            resultBuilder.addBody("else if(" + candidateFieldName + "  != null) return false;");
        }
    }

}

        
