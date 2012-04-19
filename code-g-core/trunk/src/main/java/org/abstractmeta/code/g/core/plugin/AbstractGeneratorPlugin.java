package org.abstractmeta.code.g.core.plugin;

import org.abstractmeta.code.g.code.JavaType;
import org.abstractmeta.code.g.code.JavaTypeRegistry;
import org.abstractmeta.code.g.core.code.builder.JavaTypeBuilder;
import org.abstractmeta.code.g.core.internal.TypeNameWrapper;
import org.abstractmeta.code.g.config.Descriptor;
import org.abstractmeta.code.g.extractor.FieldExtractor;
import org.abstractmeta.code.g.extractor.MethodExtractor;
import com.google.common.base.Preconditions;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Abstract generator plugin.
 * <p>Convenience class that provides default implementation for most common cases.</p>
 * <h3>Plugin configuration</h3>
 * The following naming convention is used to configure a plugin: <b>
 * PLUGIN_NAME.CONFIGURATION_OPTION_NAME</b>
 * <p/>
 * <ul>
 * The following option use default value from descriptor
 * <li>targetPackage</li>
 * <li>source</li>
 * <li>targetPostfix</li>
 * <li>superType</li>
 * </ul>
 * <h3>Plugin usage</h3>
 * Each sub class should implement the following methods
 * <ul>
 * <li>isApplicable - filters source classes by checking if plugin generator can applied</li>
 * <li>generateType - generates new type based on a given source type</li>
 * <p/>
 * </ul>
 *
 * @author Adrian Witas
 */
public abstract class AbstractGeneratorPlugin {

    public static final String TARGET_PACKAGE_KEY = "targetPackage";
    public static final String SOURCE_KEY = "source";
    public static final String TARGET_POSTFIX_KEY = "targetPostfix";
    public static final String SUPPER_TYPE_KEY = "superType";
    public static final String INTERFACES_KEY = "interfaces";


    private final List<FieldExtractor> fieldExtractors;
    private final List<MethodExtractor> methodExtractors;

    protected AbstractGeneratorPlugin() {
        this(Collections.<FieldExtractor>emptyList(), Collections.<MethodExtractor>emptyList());
    }

    protected AbstractGeneratorPlugin(List<FieldExtractor> fieldExtractors, List<MethodExtractor> methodExtractors) {
        this.fieldExtractors = fieldExtractors;
        this.methodExtractors = methodExtractors;
    }

    public String getRequiredOption(Descriptor descriptor, String name) {
        return getOption(descriptor, name, true);
    }

    public String getOption(Descriptor descriptor, String name) {
        return getOption(descriptor, name, false);
    }

    public String getOption(Descriptor descriptor, String name, boolean required) {
        String actualOption = this.getClass().getSimpleName() + "." + name;
        String result = descriptor.getOptions().get(actualOption);
        if (result == null) {
            if (TARGET_PACKAGE_KEY.equals(name)) {
                result = descriptor.getTargetPackage();
            } else if (TARGET_POSTFIX_KEY.equals(name)) {
                result = descriptor.getTargetPostfix();
            } else if (SOURCE_KEY.equals(name)) {
                result = descriptor.getSource();
            } else if (SUPPER_TYPE_KEY.equals(name)) {
                result = descriptor.getSuperType();
            } else if (INTERFACES_KEY.equals(name)) {
                result = descriptor.getInterfaces();
            }
        }
        if (!required && result == null) {
            return null;
        }
        Preconditions.checkNotNull(result, actualOption + " configuration option was null");
        result = result.replace("\n", "").trim();
        return result;
    }

    public List<String> generate(List<String> sourceTypeNames, JavaTypeRegistry registry, Descriptor descriptor) {
        List<String> generatedTypeNames = new ArrayList<String>();
        String targetPackage = getRequiredOption(descriptor, TARGET_PACKAGE_KEY);
        String targetPostfix = getRequiredOption(descriptor, TARGET_POSTFIX_KEY);
        for (String sourceTypeName : sourceTypeNames) {
            JavaType sourceType = registry.get(sourceTypeName);
            if (!isApplicable(sourceType)) {
                continue;
            }
            String targetTypeName = getTargetTypeName(sourceType, targetPackage, targetPostfix);
            JavaTypeBuilder typeBuilder = generateType(sourceType, targetTypeName, descriptor);
            if (typeBuilder == null) {
                continue;
            }
            buildSuperType(descriptor, typeBuilder);
            buildInterfaces(descriptor, typeBuilder);
            registry.register(typeBuilder.build());
            generatedTypeNames.add(targetTypeName);
        }

        return generatedTypeNames;
    }


    protected void buildSuperType(Descriptor descriptor, JavaTypeBuilder typeBuilder) {
        String superTypeName = getOption(descriptor, SUPPER_TYPE_KEY);
        if (superTypeName == null) return;
        Type superType = new TypeNameWrapper(superTypeName);
        typeBuilder.addImportType(superType);
        typeBuilder.setSuperType(superType);
    }

    protected void buildInterfaces(Descriptor descriptor, JavaTypeBuilder typeBuilder) {
        String interfaces = getOption(descriptor, INTERFACES_KEY);
        if (interfaces == null || interfaces.isEmpty()) return;
        String[] interfaceArray = interfaces.split(",");
        for (String interfaceType : interfaceArray) {
            Type type = new TypeNameWrapper(interfaceType);
            typeBuilder.addImportType(type);
            typeBuilder.addSuperInterface(type);
        }
    }

    protected String getTargetTypeName(JavaType sourceType, String targetPackage, String targetPostfix) {
        return getTargetTypeName(sourceType.getSimpleName(), targetPackage, targetPostfix);
    }

    protected String getTargetTypeName(String name, String targetPackage, String targetPostfix) {
        targetPackage = targetPackage.replace(".*", "");
        return targetPackage + "." + name + targetPostfix;
    }


    protected boolean isExtractable(JavaType sourceType) {
        if (fieldExtractors.size() == 0 && methodExtractors.size() == 0) return true;
        for (FieldExtractor extractor : fieldExtractors) {
            if (extractor.extract(sourceType).size() > 0) {
                return true;
            }
        }
        for (MethodExtractor extractor : methodExtractors) {
            if (extractor.extract(sourceType).size() > 0) {
                return true;
            }
        }
        return false;
    }

    protected void addExtractableFields(JavaTypeBuilder builder, JavaType sourceType) {
        for (FieldExtractor extractor : fieldExtractors) {
            builder.addFields(extractor.extract(sourceType));
        }
    }


    protected void addExtractableMethods(JavaTypeBuilder builder, JavaType sourceType) {
        for (MethodExtractor extractor : methodExtractors) {
            builder.addMethods(extractor.extract(sourceType));
        }
    }


    /**
     * Is a given java source type applicable to this class generator plugin.
     *
     * @param sourceType java source type
     * @return true if applicable
     */
    protected abstract boolean isApplicable(JavaType sourceType);


    /**
     * Generates new type for a given source type and a given target type name.
     *
     * @param sourceType     source java type
     * @param targetTypeName target type name.
     * @param descriptor
     * @return
     */
    protected abstract JavaTypeBuilder generateType(JavaType sourceType, String targetTypeName, Descriptor descriptor);


}
