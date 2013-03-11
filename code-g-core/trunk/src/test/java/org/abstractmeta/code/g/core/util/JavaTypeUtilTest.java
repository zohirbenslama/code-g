package org.abstractmeta.code.g.core.util;


import org.testng.Assert;
import org.testng.annotations.Test;

@Test
public class JavaTypeUtilTest {

    public void testGetSimpleClassName() {
        Assert.assertEquals(JavaTypeUtil.getSimpleClassName("com.foo.Bar"), "Bar");
        Assert.assertEquals(JavaTypeUtil.getSimpleClassName("com.foo.Bar$Foo"), "Bar.Foo");
        Assert.assertEquals(JavaTypeUtil.getSimpleClassName("com.foo.Bar$Foo", false), "Foo");
        Assert.assertEquals(JavaTypeUtil.getSimpleClassName("com.foo.Bar$Foo;"), "Bar.Foo[]");
    }
}
