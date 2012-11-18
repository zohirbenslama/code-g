package org.abstractmeta.code.g.core.config.properties;

import org.abstractmeta.code.g.config.UnitDescriptor;
import org.abstractmeta.code.g.core.util.DecoderUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents PrUnitDescriptorDecoder
 *
 * @author Adrian Witas
 */
public class UnitDescriptorsDecoder {


    public static String UNIT_DESCRIPTOR_KEY = "unit";
    public static String TEMPLATE_DESCRIPTOR_KEY = "template";


    @SuppressWarnings("unchecked")
    public List<UnitDescriptor> decode(Map properties) {
        UnitDescriptorDecoder unitDescriptorDecoder = new UnitDescriptorDecoder(extractTemplates(properties));

        List<UnitDescriptor> result = new ArrayList<UnitDescriptor>();
        for (int i = 0; ; i++) {
            Map<String, String> unitDescriptorProperties = DecoderUtil.matchWithPrefix(properties, UNIT_DESCRIPTOR_KEY + "_" + i);
            if (unitDescriptorProperties.isEmpty()) {
                if (i > 1) break;
                continue;
            }
            UnitDescriptor unitDescriptor = unitDescriptorDecoder.decode(unitDescriptorProperties);
            result.add(unitDescriptor);
        }

        return result;
    }

    private Map<String, Map<String, String>> extractTemplates(Map<String, String> properties) {
        Map<String, Map<String, String>> result = new HashMap<String, Map<String, String>>();
        for (int i = 0; ; i++) {
            Map<String, String> template = DecoderUtil.matchWithPrefix(properties, TEMPLATE_DESCRIPTOR_KEY + "_" + i);
            if (template.isEmpty()) {
                if (i > 1) break;
                continue;
            }
            result.put(template.get("name"), template);
        }
        return result;
    }


}
