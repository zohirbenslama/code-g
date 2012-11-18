package org.abstractmeta.code.g.core.config.properties;

import org.abstractmeta.code.g.config.Descriptor;
import org.abstractmeta.code.g.core.config.builder.DescriptorBuilder;
import org.abstractmeta.code.g.core.util.DecoderUtil;
import org.abstractmeta.code.g.core.util.DescriptorUtil;

import java.util.Map;

/**
 * Represents DescriptorDecoder
 *
 * @author Adrian Witas
 */
public class DescriptorDecoder {

    private final Map<String, Map<String, String>> template;

    public DescriptorDecoder(Map<String, Map<String, String>> template) {
        this.template = template;
    }

    @SuppressWarnings("unchecked")
    public Descriptor decode(Map<String, String> properties) {
        DescriptorBuilder resultBuilder = new DescriptorBuilder();
        DescriptorUtil.applyTemplate(properties, template);
        resultBuilder.setPlugin(properties.get("plugin"));
        resultBuilder.setCompilationSources(DecoderUtil.readStringList(properties, "compilationSources"));
        resultBuilder.setExclusions(DecoderUtil.readStringSet(properties, "exclusion"));
        resultBuilder.setInclusion(DecoderUtil.readStringSet(properties, "inclusions"));
        resultBuilder.setInterfaces(properties.get("interfaces"));
        resultBuilder.setSourceClass(properties.get("sourceClass"));
        resultBuilder.setSourcePackage(properties.get("sourcePackage"));
        resultBuilder.setSuperType(properties.get("superType"));
        resultBuilder.setTargetPackage(properties.get("targetPackage"));
        resultBuilder.setTargetPrefix(properties.get("targetPrefix"));
        resultBuilder.setTargetPostfix(properties.get("targetPostfix"));
        Map<String, String> options = DecoderUtil.matchWithPrefix(properties, "options");
        resultBuilder.setOptions((Map)options);
        Map<String, String> immutableImplementation = DecoderUtil.matchWithPrefix(properties, "immutableImplementation");
        resultBuilder.setImmutableImplementation(immutableImplementation);
        return resultBuilder.build();
    }
}
