package org.abstractmeta.code.g.core.config.properties;

import org.abstractmeta.code.g.config.UnitDescriptor;
import org.abstractmeta.code.g.core.util.DecoderUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Represents PrUnitDescriptorDecoder
 *
 * @author Adrian Witas
 */
public class UnitDescriptorsDecoder {


    public static String UNIT_DESCRIPTOR_KEY = "unit";
    private final UnitDescriptorDecoder unitDescriptorDecoder;

    public UnitDescriptorsDecoder() {
        this(new UnitDescriptorDecoder());
    }

    public UnitDescriptorsDecoder(UnitDescriptorDecoder unitDescriptorDecoder) {
        this.unitDescriptorDecoder = unitDescriptorDecoder;
    }

    @SuppressWarnings("unchecked")
    public List<UnitDescriptor> decode(Map properties) {
        List<UnitDescriptor> result = new ArrayList<UnitDescriptor>();
        for(int i =0;;i++) {
            Map<String, String> unitDescriptorProperties = DecoderUtil.matchWithPrefix(properties, UNIT_DESCRIPTOR_KEY + "_" + i);
            if(unitDescriptorProperties.isEmpty()) {
                if(i > 1) break;
                continue;
            }
            UnitDescriptor unitDescriptor = unitDescriptorDecoder.decode(unitDescriptorProperties);
            result.add(unitDescriptor);
        }

        return result;
    }


}
