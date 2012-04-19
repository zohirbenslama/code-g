package org.abstractmeta.code.g.core.plugin;

import org.abstractmeta.code.g.code.JavaField;
import org.abstractmeta.code.g.code.JavaType;
import org.abstractmeta.code.g.core.builder.BuilderClassBuilder;
import org.abstractmeta.code.g.core.code.builder.JavaFieldBuilder;
import org.abstractmeta.code.g.core.code.builder.JavaTypeBuilder;
import org.abstractmeta.code.g.core.util.JavaTypeUtil;
import org.abstractmeta.code.g.config.Descriptor;
import org.abstractmeta.code.g.plugin.CodeGeneratorPlugin;

import java.util.Map;

/**
 * <p><b>Builder Generator Plugin</b></p>
 * <h2>Overview</h2>
 * <p>This plugin generates code implementation for the builder. The generation process can be break down to</p>
 * <ul>
 * <li>Fields generation, all fields are extracted from source class' fields.
 * <ul>
 * <li>all fields are mutable</li>
 * <li>for each field additional starting with '_' is added to track whether original field was mutated</li>
 * </ul>
 * </li>
 * <li>Method generation - once all fields are defined and type is being build the following
 * field handlers are fired with ability to build relevant method
 * <ul>
 * <li>{@link org.abstractmeta.code.g.core.handler.BuilderSetterFieldHandler}</li>
 * <li>{@link org.abstractmeta.code.g.core.handler.BuilderCollectionFieldHandler}</li>
 * <li>{@link org.abstractmeta.code.g.core.handler.BuilderMapFieldHandler}</li>
 * <li>{@link org.abstractmeta.code.g.core.handler.BuilderArrayFieldHandler}</li>
 * <li>{@link org.abstractmeta.code.g.core.handler.GetterFieldHandler}</li>
 * <li>{@link org.abstractmeta.code.g.core.handler.HasFieldHandler}</li>
 * <li>{@link org.abstractmeta.code.g.core.handler.BuilderTypeHandler}</li>
 * <li>{@link org.abstractmeta.code.g.core.handler.BuilderMergeHandler}</li>
 * </ul>
 * </li>
 * </ul>
 * </p>
 *
 * @author Adrian Witas
 */
public class BuilderGeneratorPlugin extends AbstractGeneratorPlugin implements CodeGeneratorPlugin {

    @Override
    protected boolean isApplicable(JavaType sourceType) {
        return !(sourceType.getModifiers().contains("abstract") || sourceType.getFields().size() == 0);
    }

    @Override
    protected JavaTypeBuilder generateType(JavaType sourceType, String targetTypeName, Descriptor descriptor) {
        BuilderClassBuilder builderClassBuilder = new BuilderClassBuilder(sourceType);
        Map<String, String> immutableImplementation = descriptor.getImmutableImplementation();
        if (immutableImplementation != null) {
            for (String key : immutableImplementation.keySet()) {
                builderClassBuilder.addImmutableImplementation(key, immutableImplementation.get(key));
            }
        }
        builderClassBuilder.setSourceType(sourceType);
        builderClassBuilder.addModifier("public").setTypeName(targetTypeName);
        for (JavaField field : sourceType.getFields()) {
            JavaFieldBuilder fieldBuilder = new JavaFieldBuilder();
            fieldBuilder.addModifier("private");
            fieldBuilder.setType(field.getType());
            fieldBuilder.setName(field.getName());
            builderClassBuilder.addField(fieldBuilder.build());

            JavaFieldBuilder trackerFieldBuilder = new JavaFieldBuilder();
            trackerFieldBuilder.addModifier("private");
            trackerFieldBuilder.setType(boolean.class);
            trackerFieldBuilder.setName("_" + field.getName());
            builderClassBuilder.addField(trackerFieldBuilder.build());
        }

        return builderClassBuilder;
    }

    protected String getTargetTypeName(JavaType sourceType, String targetPackage, String targetPostfix) {
        String superSourceType = JavaTypeUtil.getSimpleClassName(JavaTypeUtil.getSuperTypeName(sourceType));
        return targetPackage + "." + superSourceType + targetPostfix;
    }



}
