package org.abstractmeta.code.g.core.renderer;

import org.abstractmeta.code.g.code.JavaConstructor;
import org.abstractmeta.code.g.code.JavaTypeImporter;
import org.abstractmeta.code.g.core.util.StringUtil;
import org.abstractmeta.code.g.renderer.JavaConstructorRenderer;
import com.google.common.base.Joiner;


public class ConstructorRenderer extends AbstractRenderer<JavaConstructor> implements JavaConstructorRenderer {

    public static final String TEMPLATE = String.format("${%s}${%s}${%s}(${%s}) {\n" +
        "${%s}\n}\n",
        DOCUMENTATION_PARAMETER,
        MODIFIER_PARAMETER,
        NAME_PARAMETER,
        ARGUMENTS_PARAMETER,
        BODY_PARAMETER
    );

    public ConstructorRenderer() {
        super(TEMPLATE, 4);
    }

    @Override
    void setParameters(JavaConstructor instance, JavaTypeImporter importer, Template template, int indentSize) {

        template.set(DOCUMENTATION_PARAMETER, getDocumentation(instance.getDocumentation()));
        template.set(MODIFIER_PARAMETER, getModifiers(instance.getModifiers()));
        template.set(NAME_PARAMETER, instance.getName());
        template.set(ARGUMENTS_PARAMETER, getMethodArguments(importer, instance.getParameterTypes(), instance.getParameterNames()));
        template.set(BODY_PARAMETER, StringUtil.indent(Joiner.on("\n").join(instance.getBody()), indentSize + 4));
    }

}
