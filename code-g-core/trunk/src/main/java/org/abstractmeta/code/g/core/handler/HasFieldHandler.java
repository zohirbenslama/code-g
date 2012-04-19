package org.abstractmeta.code.g.core.handler;

import org.abstractmeta.code.g.code.JavaField;
import org.abstractmeta.code.g.code.JavaType;
import org.abstractmeta.code.g.core.code.builder.JavaMethodBuilder;
import org.abstractmeta.code.g.core.code.builder.JavaTypeBuilder;
import org.abstractmeta.code.g.core.util.StringUtil;
import org.abstractmeta.code.g.handler.JavaFieldHandler;
import com.google.common.base.CaseFormat;

/**
 * This handler creates has[field name] method for any field prefixed with '_'.
 * Has method only returns true if original
 * field was mutated.
 * <p>
 * <ul>For 'foo' field the following method is generated.
 * <li> has: <pre>
 * public boolean hasFoo() {
 *    return this._foo;
 * }
 * </pre></li>
 * </p>
 *
 * @author Adrian Witas
 */
public class HasFieldHandler implements JavaFieldHandler {

    private final JavaTypeBuilder ownerTypeBuilder;

    public HasFieldHandler(JavaTypeBuilder ownerTypeBuilder) {
        this.ownerTypeBuilder = ownerTypeBuilder;
    }


    @Override
    public void handle(JavaType sourceType, JavaField javaField) {
        if(! (javaField.getName().charAt(0) == '_' && javaField.getType().equals(boolean.class))) {
             return;
        }

        String fieldName = javaField.getName();
        String methodPrefix = "has";
        String methodName = StringUtil.format(CaseFormat.LOWER_CAMEL, methodPrefix, fieldName, CaseFormat.LOWER_CAMEL);
        if (! ownerTypeBuilder.containsMethod(methodName)) {
            JavaMethodBuilder methodBuilder = new JavaMethodBuilder();
            methodBuilder.setName(methodName);
            methodBuilder.setResultType(boolean.class);
            methodBuilder.addModifier("public");
            methodBuilder.addBody(String.format("return this.%s;", fieldName));
            ownerTypeBuilder.addMethod(methodBuilder.build());
        }
    }
}
