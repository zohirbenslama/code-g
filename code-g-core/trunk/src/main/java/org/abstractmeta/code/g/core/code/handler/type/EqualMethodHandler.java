package org.abstractmeta.code.g.core.code.handler.type;

import org.abstractmeta.code.g.code.*;
import org.abstractmeta.code.g.code.handler.TypeHandler;
import org.abstractmeta.code.g.core.code.builder.JavaMethodBuilder;
import org.abstractmeta.code.g.core.util.ReflectUtil;
import org.abstractmeta.code.g.generator.Context;

import java.lang.reflect.Type;

/**
 * Represents  HashCodeMethodHandler.
 *
 * @author awitas
 * @version 0.01 14/11/2012
 */
public class EqualMethodHandler implements TypeHandler {

    public static final String METHOD_NAME = "equals";

    @Override
    public void handle(JavaTypeBuilder owner, Context context) {
        Config config = context.getOptional(Config.class);
        if(config == null || ! config.isGenerateEqualMethod()) return;
        JavaMethodBuilder resultBuilder = new JavaMethodBuilder();
        if(owner.containsMethod(METHOD_NAME)) return;


        resultBuilder.setName(METHOD_NAME)
                .addModifier(JavaModifier.PUBLIC)
                .setResultType(boolean.class)
                .addParameter("objectCandidate", Object.class);

        resultBuilder.addBodyLines("if(objectCandidate == null) return false;");
        resultBuilder.addBodyLines("if(!(objectCandidate instanceof " + owner.getSimpleName() + ")) return false;");
        resultBuilder.addBodyLines(owner.getSimpleName() + " candidate = " + owner.getSimpleName() + ".class.cast(objectCandidate);");

        for (JavaField field : owner.getFields()) {
            if (field.getModifiers().contains(JavaModifier.TRANSIENT)) continue;
            addEqualFieldCriteria(resultBuilder, field);

        }
        JavaType sourceType = owner.getSourceType();
        if(sourceType != null && JavaKind.CLASS.equals(sourceType.getKind())) {
            resultBuilder.addBodyLines("return super.equals(candidate);");
        } else {
            resultBuilder.addBodyLines("return true;");
        }
        owner.addMethod(resultBuilder.build());
    }


    protected void addEqualFieldCriteria(JavaMethodBuilder resultBuilder, JavaField field) {
        Type filedType = field.getType();
        String fieldName = "this." + field.getName();
        String candidateFieldName = "candidate." + field.getName();
        if (ReflectUtil.isPrimitiveType(filedType)) {
            resultBuilder.addBodyLines("if(" + fieldName + " != " + candidateFieldName + ") return false;");
        } else if (ReflectUtil.isArrayType(filedType)) {
            resultBuilder.addBodyLines("if(! Arrays.equals(" + fieldName + "," + candidateFieldName + ")) return false;");
        } else {
            resultBuilder.addBodyLines("if(" + fieldName + "  != null &&  ! " + fieldName + ".equals(" + candidateFieldName + ")) return false;");
            resultBuilder.addBodyLines("else if(" + candidateFieldName + "  != null) return false;");
        }
    }


    public static interface Config {
        boolean isGenerateEqualMethod();
    }

}

        
