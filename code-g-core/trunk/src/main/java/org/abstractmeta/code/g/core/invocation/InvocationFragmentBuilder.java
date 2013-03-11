package org.abstractmeta.code.g.core.invocation;

import com.google.common.base.Joiner;
import org.abstractmeta.code.g.core.code.JavaTypeImporterImpl;
import org.abstractmeta.code.g.core.util.JavaTypeUtil;

import java.lang.reflect.Type;

/**
 * Represents invocation fragment builder.
 *
 * @author Adrian Witas
 */
public class InvocationFragmentBuilder {

    public String build(InvocationMeta invocationMeta) {
        if (InvocationMeta.InvocationType.CONSTRUCTOR.equals(invocationMeta.getInvocationType())) {
            String genericArgumentLiteral = "";
            if (!invocationMeta.getParameterizedTypeArguments().isEmpty()) {
                JavaTypeImporterImpl typeImporter = new JavaTypeImporterImpl("");
                StringBuilder genericArgumentBuilder = new StringBuilder();
                for (Type type : invocationMeta.getParameterizedTypeArguments()) {
                    String simpleTypeName = typeImporter.getSimpleTypeName(type);
                    if (genericArgumentBuilder.length() > 0) genericArgumentBuilder.append(",");
                    genericArgumentBuilder.append(simpleTypeName);
                }
                genericArgumentLiteral = "<" + genericArgumentBuilder.toString() + ">";
            }
            return String.format(" new %s%s(%s)", invocationMeta.getConstructorName(), genericArgumentLiteral, Joiner.on(", ").join(invocationMeta.getParameters()));
        } else if (InvocationMeta.InvocationType.TYPE.equals(invocationMeta.getInvocationType())) {
            String simpleClassName = JavaTypeUtil.getSimpleClassName(invocationMeta.getOwnerType());
            return String.format(" new %s()", simpleClassName);
        } else {
            return String.format("%s(%s)", invocationMeta.getMethodName(), Joiner.on(", ").join(invocationMeta.getParameters()));

        }

    }

}
