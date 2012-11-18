package org.abstractmeta.code.g.core.config.properties;

import org.abstractmeta.code.g.config.Descriptor;
import org.abstractmeta.code.g.config.UnitDescriptor;
import org.abstractmeta.code.g.core.config.builder.UnitDescriptorBuilder;
import org.abstractmeta.code.g.core.util.DecoderUtil;
import org.abstractmeta.code.g.core.util.DescriptorUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * Represents UnitDescriptorDecoder
 *
 * @author Adrian Witas
 */
public class UnitDescriptorDecoder {

    public static String DESCRIPTOR_KEY = "descriptor";
    public final DescriptorDecoder descriptorDecoder;


    private final Map<String, Map<String, String>> template;

    public UnitDescriptorDecoder(Map<String, Map<String, String>> template) {
        this(new DescriptorDecoder(template), template);
    }

    public UnitDescriptorDecoder(DescriptorDecoder descriptorDecoder, Map<String, Map<String, String>> template) {
        this.descriptorDecoder = descriptorDecoder;
        this.template = template;
    }

    public UnitDescriptor decode(Map<String, String> properties) {
        DescriptorUtil.applyTemplate(properties, template);
        UnitDescriptorBuilder resultBuilder = new UnitDescriptorBuilder();
        resultBuilder.setSourcePackage(properties.get("sourcePackage"));
        resultBuilder.setSourceDirectory(properties.get("sourceDirectory"));
        resultBuilder.setTargetDirectory(properties.get("targetDirectory"));
        resultBuilder.addClassPathEntries(DecoderUtil.readStringList(properties, " classPathEntries"));
        Map<String, String> postDescriptor = DecoderUtil.matchWithPrefix(properties, "postDescriptor");
        if (postDescriptor.size() > 0) {
            resultBuilder.setPostDescriptor(descriptorDecoder.decode(postDescriptor));
        }
        Collection<Descriptor> descriptors = new ArrayList<Descriptor>();

        for (int i = 0; ; i++) {
            Map<String, String> descriptorProperties = DecoderUtil.matchWithPrefix(properties, DESCRIPTOR_KEY + "_" + i);
            if (descriptorProperties.isEmpty()) {
                if (i > 1) break;
                continue;
            }
            Descriptor descriptor = descriptorDecoder.decode(descriptorProperties);
            descriptors.add(descriptor);
        }
        resultBuilder.setDescriptors(descriptors);
        return resultBuilder.build();
    }

}
