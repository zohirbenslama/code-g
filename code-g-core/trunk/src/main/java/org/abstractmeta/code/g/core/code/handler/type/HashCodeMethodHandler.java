package org.abstractmeta.code.g.core.code.handler.type;


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
public class HashCodeMethodHandler {

//    public static final String GENERATE_HASH_CODE = "generateHashCodeMethod";
//    public static final String METHOD_NAME = "hashCode";
//
//    public static int HASH_MULTIPLIER = 32;
//
//    @Override
//    public void handle(JavaTypeBuilder owner) {
//        if(owner.containsMethod(METHOD_NAME)) return;
//        Descriptor descriptor = owner.getDescriptor();
//        JavaMethodBuilder resultBuilder = new JavaMethodBuilder();
//        resultBuilder.setName("hashCode").addModifier(JavaModifier.PUBLIC).setResultType(int.class);
//        resultBuilder.addBodyLines("int result = 0;");
//        String hashAnnotation = DescriptorUtil.get(descriptor, "generateHashCodeMethod.hashFieldAnnotation");
//        for (JavaField field : owner.getFields()) {
//            if (field.getModifiers().contains(JavaModifier.TRANSIENT)) continue;
//            if (hashAnnotation != null) {
//                if (!JavaTypeUtil.containsAnnotation(field.getAnnotations(), hashAnnotation)) {
//                    continue;
//                }
//            }
//            addHashFieldCalculation(resultBuilder, field);
//        }
//        JavaType sourceType = owner.getSourceType();
//        if(sourceType != null && JavaKind.CLASS.equals(sourceType.getKind())) {
//            resultBuilder.addBodyLines("result += super.hashCode();");
//        }
//        resultBuilder.addBodyLines("return result;");
//        owner.addMethod(resultBuilder.build());
//    }
//
//
//    private void addHashFieldCalculation(JavaMethodBuilder resultBuilder, JavaField field) {
//        Type filedType = field.getType();
//        String fieldName = "this." + field.getName();
//        if (ReflectUtil.isPrimitiveType(filedType)) {
//            resultBuilder.addBodyLines("result += " + HASH_MULTIPLIER + " * " + fieldName + ";");
//        } else if (ReflectUtil.isArrayType(filedType)) {
//            resultBuilder.addBodyLines("result += " + HASH_MULTIPLIER + " * (" + fieldName + " != null ? Arrays.hashCode(" + fieldName + ") : 0);");
//        } else {
//            resultBuilder.addBodyLines("result += " + HASH_MULTIPLIER + " * (" + fieldName + " != null ? " + fieldName + ".hashCode() : 0);");
//        }
//    }


}

        
