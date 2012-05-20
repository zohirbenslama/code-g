package org.abstractmeta.code.g.core.plugin;

import org.abstractmeta.code.g.code.JavaType;
import org.abstractmeta.code.g.code.JavaTypeRegistry;
import org.abstractmeta.code.g.core.code.builder.JavaTypeBuilder;
import org.abstractmeta.code.g.core.internal.TypeNameWrapper;
import org.abstractmeta.code.g.config.Descriptor;
import org.abstractmeta.code.g.extractor.FieldExtractor;
import org.abstractmeta.code.g.extractor.MethodExtractor;
import com.google.common.base.Preconditions;
import org.abstractmeta.code.g.plugin.CodeGeneratorPlugin;

import java.lang.reflect.Type;
import java.util.*;

/**
 * Abstract generator plugin.
 * <p>Convenience class that provides default implementation for most core cases.</p>
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

    private final List<FieldExtractor> fieldExtractors;
    private final List<MethodExtractor> methodExtractors;

    protected AbstractGeneratorPlugin() {
        this(Collections.<FieldExtractor>emptyList(), Collections.<MethodExtractor>emptyList());
    }

    protected AbstractGeneratorPlugin(List<FieldExtractor> fieldExtractors, List<MethodExtractor> methodExtractors) {
        this.fieldExtractors = fieldExtractors;
        this.methodExtractors = methodExtractors;
    }


    public List<String> generate(Collection<String> sourceTypeNames, JavaTypeRegistry registry, Descriptor descriptor) {
        List<String> generatedTypeNames = new ArrayList<String>();
        for (String sourceTypeName : sourceTypeNames) {
            JavaType sourceType = registry.get(sourceTypeName);
            if (! isApplicable(sourceType)) {
                continue;
            }
            String targetTypeName = getTargetTypeName(sourceType, descriptor, registry);
            if(registry.isRegistered(targetTypeName)) {
                continue;
            }
            JavaTypeBuilder typeBuilder = generateType(sourceType, registry, targetTypeName, descriptor);
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
        String superTypeName = descriptor.getSuperType();
        if (superTypeName == null) return;
        Type superType = new TypeNameWrapper(superTypeName);
        typeBuilder.addImportType(superType);
        typeBuilder.setSuperType(superType);
    }

    protected void buildInterfaces(Descriptor descriptor, JavaTypeBuilder typeBuilder) {
        String interfaces = descriptor.getInterfaces();
        if (interfaces == null || interfaces.isEmpty()) return;
        String[] interfaceArray = interfaces.split(",");
        for (String interfaceType : interfaceArray) {
            Type type = new TypeNameWrapper(interfaceType);
            typeBuilder.addImportType(type);
            typeBuilder.addSuperInterface(type);
        }
    }

    protected String getTargetTypeName(JavaType sourceType, Descriptor descriptor, JavaTypeRegistry registry) {
        return getTargetTypeName(sourceType.getSimpleName(), descriptor, registry);
    }

    protected String getTargetTypeName(String name, Descriptor descriptor, JavaTypeRegistry registry) {
        String targetPackage = descriptor.getTargetPackage().replace(".*", "");
        String targetPrefix = descriptor.getTargetPrefix();
        if (targetPrefix == null) targetPrefix = "";
        String targetPostfix = descriptor.getTargetPostfix();
        if (targetPostfix == null) targetPostfix = "";
        return targetPackage + "." + targetPrefix + name + targetPostfix;
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
     *
     * @param sourceType     source java type
     * @param registry
     *@param targetTypeName target type name.
     * @param descriptor   @return
     */
    protected abstract JavaTypeBuilder generateType(JavaType sourceType, JavaTypeRegistry registry, String targetTypeName, Descriptor descriptor);


    public Map<String, String> getOptions() {
        return Collections.emptyMap();
    }
    
}
