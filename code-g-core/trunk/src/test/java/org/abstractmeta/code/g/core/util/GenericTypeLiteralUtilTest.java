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
