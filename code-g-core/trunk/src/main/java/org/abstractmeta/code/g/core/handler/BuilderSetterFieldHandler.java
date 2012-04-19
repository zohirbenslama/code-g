package org.abstractmeta.code.g.core.handler;

import org.abstractmeta.code.g.code.JavaField;
import org.abstractmeta.code.g.code.JavaType;
import org.abstractmeta.code.g.core.internal.TypeNameWrapper;
import org.abstractmeta.code.g.core.code.builder.JavaMethodBuilder;
import org.abstractmeta.code.g.core.code.builder.JavaTypeBuilder;
import org.abstractmeta.code.g.core.util.StringUtil;
import org.abstractmeta.code.g.handler.JavaFieldHandler;
import com.google.common.base.CaseFormat;

import java.lang.reflect.Type;

/**
 * This handler creates set method for any java field.
 * <p>
 * Take the following <code>field: Collection&lt;String> foos;</code> as example.
 * For this case the following method will be added to the owner builder type.
 * <ul>
 * <li> setter: <pre>
 * public &lt;T> setFoos(Collection&lt;String> foos) {
 *    this.foos = foos;
 *    return this;
 * }</pre></li>
 * <p/>
 * Where &lt;T>  is the field owner type.
 * </p>
 *
 * @author Adrian Witas
 */
public class BuilderSetterFieldHandler implements JavaFieldHandler {

    private final JavaTypeBuilder ownerTypeBuilder;

    public BuilderSetterFieldHandler(JavaTypeBuilder ownerTypeBuilder) {
        this.ownerTypeBuilder = ownerTypeBuilder;
    }

    @Override
    public void handle(JavaType sourceType, JavaField javaField) {
        addSetterMethod(ownerTypeBuilder, javaField.getName(), javaField.getType());
    }

    protected void addSetterMethod(JavaTypeBuilder typeBuilder, String fieldName, Type fieldType) {
        JavaMethodBuilder methodBuilder = new JavaMethodBuilder();
        String methodName = StringUtil.format(CaseFormat.LOWER_CAMEL, "set", fieldName, CaseFormat.LOWER_CAMEL);
        if (! ownerTypeBuilder.containsMethod(methodName)) {
            methodBuilder.setName(methodName);
            methodBuilder.setResultType(new TypeNameWrapper(typeBuilder.getName()));
            methodBuilder.addParameter(fieldName, fieldType);
            methodBuilder.addModifier("public");
            methodBuilder.addBody(String.format("this.%s = %s;", fieldName, fieldName));
            methodBuilder.addBody(String.format("this._%s = true;", fieldName));
            methodBuilder.addBody("return this;");
            typeBuilder.addMethod(methodBuilder.build());
        }
    }
}
