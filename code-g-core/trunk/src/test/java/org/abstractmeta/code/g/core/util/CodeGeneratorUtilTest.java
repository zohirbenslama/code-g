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

import junit.framework.Assert;
import org.testng.annotations.Test;

/**
 * Utility test
 */
@Test
public class CodeGeneratorUtilTest {


    public void testGenericTypeLiteralUtil() {
        Assert.assertTrue(CodeGeneratorUtil.isSetterMethod("setI"));
        Assert.assertFalse(CodeGeneratorUtil.isSetterMethod("seti"));
        Assert.assertFalse(CodeGeneratorUtil.isSetterMethod("seZi"));
        Assert.assertFalse(CodeGeneratorUtil.isSetterMethod("set"));
    }

}
