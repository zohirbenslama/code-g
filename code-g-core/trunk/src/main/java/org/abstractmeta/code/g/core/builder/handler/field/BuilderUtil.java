package org.abstractmeta.code.g.core.builder.handler.field;

import org.abstractmeta.code.g.code.JavaField;
import org.abstractmeta.code.g.code.JavaMethod;
import org.abstractmeta.code.g.code.JavaType;
import org.abstractmeta.code.g.code.JavaTypeBuilder;
import org.abstractmeta.code.g.core.code.builder.JavaMethodBuilder;
import org.abstractmeta.code.g.core.internal.TypeNameWrapper;
import org.abstractmeta.code.g.core.util.CodeGeneratorUtil;
import org.abstractmeta.code.g.core.util.JavaTypeUtil;

/**
 * Builder utilities
 *
 * @author Adrian Witas
 */

public class BuilderUtil {


    public static void addIsPresentFlag(JavaTypeBuilder owner, String fieldName, JavaMethodBuilder methodBuilder) {
        JavaField isPresentField = JavaTypeUtil.getField(owner.getFields(), CodeGeneratorUtil.getPresentFieldName(fieldName));
        if (isPresentField != null) {
            methodBuilder.addBodyLines(String.format("this.%s = true;", isPresentField.getName()));
        }
    }


    public static void addSetterResultType(JavaTypeBuilder owner, String methodName, JavaMethodBuilder methodBuilder) {
        JavaType sourceType = owner.getSourceType();
        JavaMethod setMethod = JavaTypeUtil.getMethod(sourceType.getMethods(), methodName);
        if (setMethod != null && void.class.equals(setMethod.getResultType())) {
            methodBuilder.setResultType(setMethod.getResultType());
        } else {
            methodBuilder.setResultType(new TypeNameWrapper(owner.getName(), owner.getGenericTypeArguments()));
            methodBuilder.addBodyLines("return this;");
        }
    }
}