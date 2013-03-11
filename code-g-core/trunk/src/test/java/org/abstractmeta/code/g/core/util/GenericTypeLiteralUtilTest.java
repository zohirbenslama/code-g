package org.abstractmeta.code.g.core.util;


import org.abstractmeta.code.g.core.type.GenericTypeLiteral;
import org.abstractmeta.code.g.core.type.GenericTypeLiteralParser;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Represents GenericTypeLiteralUtilTest
 *
 * @author Adrian Witas
 */
@Test
public class GenericTypeLiteralUtilTest {

    public void testGenericTypeLiteralUtil() {

        GenericTypeLiteral literal = GenericTypeLiteralParser.parse("Map<String, Integer>");
        Assert.assertEquals(literal.getTypeName(), "Map");
        Assert.assertEquals(literal.getArgumentTypes().get(0).getTypeName(), "String");
        Assert.assertEquals(literal.getArgumentTypes().get(1).getTypeName(), "Integer");

    }
}
