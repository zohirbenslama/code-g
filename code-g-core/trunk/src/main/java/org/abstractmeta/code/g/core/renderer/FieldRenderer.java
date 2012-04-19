package org.abstractmeta.code.g.core.renderer;


import org.abstractmeta.code.g.code.JavaField;
import org.abstractmeta.code.g.code.JavaTypeImporter;
import org.abstractmeta.code.g.renderer.JavaFieldRenderer;

public class FieldRenderer extends AbstractRenderer<JavaField> implements JavaFieldRenderer {

    public static final String INIT_BLOCK_PARAMETER = "initBlock";
    public static final String IMMUTABLE_PARAMETER = "immutable";

    public static final String TEMPLATE = String.format("${%s}${%s}${%s}${%s}${%s} ${%s}${%s};\n",
            DOCUMENTATION_PARAMETER,
            ANNOTATIONS_PARAMETER,
            MODIFIER_PARAMETER,
            IMMUTABLE_PARAMETER,
            TYPE_PARAMETER,
            NAME_PARAMETER,
            INIT_BLOCK_PARAMETER);

    public FieldRenderer() {
        super(TEMPLATE, 4);
    }

    @Override
    void setParameters(JavaField instance, JavaTypeImporter importer, Template template, int indentSize) {
        template.set(DOCUMENTATION_PARAMETER, getDocumentation(instance.getDocumentation()));
        template.set(IMMUTABLE_PARAMETER, instance.isImmutable() ? "final ": "");
        template.set(ANNOTATIONS_PARAMETER, getAnnotations(importer, instance.getAnnotations()));
        template.set(MODIFIER_PARAMETER, getModifiers(instance.getModifiers()));
        template.set(TYPE_PARAMETER, importer.getSimpleTypeName(instance.getType()));
        template.set(NAME_PARAMETER, instance.getName());
        template.set(INIT_BLOCK_PARAMETER, getValue(instance.getInitBody()));
    }


}
