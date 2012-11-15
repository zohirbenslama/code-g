package org.abstractmeta.code.g.core.util;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents  DecoderUtilTest.
 */

@Test
public class DecoderUtilTest {

    public void test() {
        Map<String, String> map = new HashMap<String, String>() {{
            put("k_1.a", "1");
            put("k_1.b", "2");
        }};
        Map<String, String> result = DecoderUtil.matchWithPrefix(map, "k_1");
        Assert.assertEquals(result.get("a"), "1");
        Assert.assertEquals(result.get("b"), "2");

    }

}

        
