/**
 * Copyright 2011 Adrian Witas
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.abstractmeta.code.g.core.renderer;

import org.abstractmeta.code.g.code.*;
import org.abstractmeta.code.g.core.internal.TypeNameWrapper;
import org.abstractmeta.code.g.core.util.StringUtil;
import org.abstractmeta.code.g.renderer.JavaConstructorRenderer;
import org.abstractmeta.code.g.renderer.JavaFieldRenderer;
import org.abstractmeta.code.g.renderer.JavaMethodRenderer;
import org.abstractmeta.code.g.renderer.JavaTypeRenderer;

import java.util.List;


public class TypeRenderer extends AbstractRenderer<JavaType> implements JavaTypeRenderer {


    public static final String PACKAGE_PARAMETER = "package";
    public static final String IMPORT_PARAMETER = "import";
    public static final String IMPLEMENTS_PARAMETER = "implements";
    public static final String EXTENDS_PARAMETER = "extends";
    public static final String KIND_PARAMETER = "kind";


    public static String TEMPLATE = String.format("${%s}" +
            "${%s}${%s}${%s}\n" +
            "${%s}${%s} ${%s}${%s}${%s} {\n" +
            "${%s}" +
            "}",
            PACKAGE_PARAMETER,
            IMPORT_PARAMETER,
            DOCUMENTATION_PARAMETER,
            ANNOTATIONS_PARAMETER,
            MODIFIER_PARAMETER,
            KIND_PARAMETER,
            NAME_PARAMETER,
            EXTENDS_PARAMETER,
            IMPLEMENTS_PARAMETER,
            BODY_PARAMETER);


    private final JavaFieldRenderer fieldRenderer;
    private final JavaMethodRenderer methodRenderer;
    private final JavaConstructorRenderer constructorRenderer;


    public TypeRenderer() {
        super(TEMPLATE, 0);
        this.fieldRenderer = new FieldRenderer();
        this.constructorRenderer = new ConstructorRenderer();
        this.methodRenderer = new MethodRenderer(this);
    }

    @Override
    void setParameters(JavaType instance, JavaTypeImporter importer, SimpleTemplate template, int indentSize) {
        template.set(PACKAGE_PARAMETER, instance.isNested() ? "" : mergeFragment("package ", instance.getPackageName(), ";\n\n"));
        template.set(DOCUMENTATION_PARAMETER, getDocumentation(instance.getDocumentation()));
        template.set(MODIFIER_PARAMETER, getModifiers(instance.getModifiers()));
        template.set(KIND_PARAMETER, getValue(instance.getKind(), "class"));
        template.set(ANNOTATIONS_PARAMETER, getAnnotations(importer, instance.getAnnotations()));
        String typeName = importer.getTypeName(new TypeNameWrapper(instance.getName()), instance.getGenericTypeArguments());

        template.set(NAME_PARAMETER, importer.getSimpleTypeName(typeName));
        String extendsFragment = "";
        if (instance.getSuperType() != null) {
            extendsFragment = importer.getSimpleTypeName(instance.getSuperType());
        }
        template.set(EXTENDS_PARAMETER, mergeFragment(" extends ", extendsFragment, ""));
        template.set(IMPLEMENTS_PARAMETER, mergeFragment(" implements ", getTypes(importer, instance.getSuperInterfaces()), ""));
        String body = buildBody(importer, instance, indentSize);
        template.set(BODY_PARAMETER, body);
        String imports = instance.isNested() ? "" : StringUtil.join(importer.getTypeNames(), "import ", ";\n", true);
        template.set(IMPORT_PARAMETER, imports);
    }


    private String buildBody(JavaTypeImporter importer, JavaType instance, int indentSize) {
        String fieldsFragment = buildFieldsFragment(importer, instance.getFields(), indentSize);
        String constructorFragment = buildConstructorsFragment(importer, instance.getConstructors(), indentSize);
        String methodsFragment = buildMethodsFragment(importer, instance.getMethods(), indentSize);
        String javaInnerTypes = getJavaTypes(this, importer, instance.getNestedJavaTypes(), 4);
        return String.format("%s\n%s\n%s%s", fieldsFragment, constructorFragment, methodsFragment, ! javaInnerTypes.isEmpty() ? javaInnerTypes + "\n" :"" );
    }


    private String buildFieldsFragment(JavaTypeImporter importer, List<JavaField> fields, int indentSize) {
        StringBuilder result = new StringBuilder();
        for (JavaField field : fields) {
            result.append(fieldRenderer.render(field, importer, indentSize));
        }
        return result.toString();
    }

    private String buildConstructorsFragment(JavaTypeImporter importer, List<JavaConstructor> constructors, int indentSize) {
        StringBuilder result = new StringBuilder();
        for (JavaConstructor constructor : constructors) {
            result.append(constructorRenderer.render(constructor, importer, indentSize));
        }
        return result.toString();
    }

    private String buildMethodsFragment(JavaTypeImporter importer, List<JavaMethod> methods, int indentSize) {
        StringBuilder result = new StringBuilder();
        for (JavaMethod method : methods) {
            result.append(methodRenderer.render(method, importer, indentSize));
        }
        return result.toString();
    }


}
