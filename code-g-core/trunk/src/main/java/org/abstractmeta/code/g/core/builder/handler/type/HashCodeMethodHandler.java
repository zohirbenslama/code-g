package org.abstractmeta.code.g.core.builder.handler.type;


import org.abstractmeta.code.g.code.*;
import org.abstractmeta.code.g.code.handler.TypeHandler;
import org.abstractmeta.code.g.core.code.builder.JavaMethodBuilder;
import org.abstractmeta.code.g.core.util.JavaTypeUtil;
import org.abstractmeta.code.g.core.util.ReflectUtil;
import org.abstractmeta.code.g.generator.Context;

import java.lang.reflect.Type;

/**
 * Represents  HashCodeMethodHandler.
 * <p/>
 * Descriptor options
 * <ul>
 * <li>generateHashCodeMethod - is set to true flag used to generate hashCode method</li>
 * <li>generateHashCodeMethod.hashFieldAnnotation - if specified only field with this annotation will be used in hashCode calculation</li>
 * </ul>
 */
public class HashCodeMethodHandler implements TypeHandler {

    public static final String METHOD_NAME = "hashCode";

    public static int HASH_MULTIPLIER = 32;

    @Override
    public void handle(JavaTypeBuilder owner, Context context) {
        Config config = context.getOptional(Config.class);
        if (config == null || !config.isGenerateHashMethod()) return;
        if (owner.containsMethod(METHOD_NAME)) return;
        JavaMethodBuilder resultBuilder = new JavaMethodBuilder();
        resultBuilder.setName(METHOD_NAME).addModifiers(JavaModifier.PUBLIC).setResultType(int.class);
        resultBuilder.addBodyLines("int result = 0;");


        String hashAnnotation = config.getIncludeInHashAnnotation();
        for (JavaField field : owner.getFields()) {
            if (field.getModifiers().contains(JavaModifier.TRANSIENT)) continue;
            if (hashAnnotation != null) {
                if (!JavaTypeUtil.containsAnnotation(field.getAnnotations(), hashAnnotation)) {
                    continue;
                }
            }
            addHashFieldCalculation(resultBuilder, field, config);
        }
        JavaType sourceType = owner.getSourceType();
        if (sourceType != null && JavaKind.CLASS.equals(sourceType.getKind())) {
            resultBuilder.addBodyLines("result += super.hashCode();");
        }
        resultBuilder.addBodyLines("return result;");
        owner.addMethod(resultBuilder.build());
    }


    private void addHashFieldCalculation(JavaMethodBuilder resultBuilder, JavaField field, Config config) {
        Type filedType = field.getType();
        int hashMultiplier = getHashMultiplier(config);
        String fieldName = "this." + field.getName();
        if (ReflectUtil.isPrimitiveType(filedType)) {
            resultBuilder.addBodyLines("result += " + hashMultiplier + " * " + fieldName + ";");
        } else if (ReflectUtil.isArrayType(filedType)) {
            resultBuilder.addBodyLines("result += " + hashMultiplier + " * (" + fieldName + " != null ? Arrays.hashCode(" + fieldName + ") : 0);");
        } else {
            resultBuilder.addBodyLines("result += " + hashMultiplier + " * (" + fieldName + " != null ? " + fieldName + ".hashCode() : 0);");
        }
    }


    protected int getHashMultiplier(Config config) {
        if (config.getHashMultiplier() > 0) return config.getHashMultiplier();
        return HASH_MULTIPLIER;
    }


    public static interface Config {

        /**
         * Flag to generate hash method
         * @return
         */
        boolean isGenerateHashMethod();

        /**
         * Hash multiplier. Default 32
         * @return
         */
        int getHashMultiplier();

        /**
         * Annotation to indicate which field include in hash calculation
         *
         * @return annotation class name
         */
        String getIncludeInHashAnnotation();

    }

}

        
