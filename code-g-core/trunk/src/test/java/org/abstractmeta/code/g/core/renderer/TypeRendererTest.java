package org.abstractmeta.code.g.core.renderer;

import org.abstractmeta.code.g.code.JavaTypeImporter;
import org.abstractmeta.code.g.core.code.JavaTypeImporterImpl;
import org.abstractmeta.code.g.core.code.builder.JavaFieldBuilder;
import org.abstractmeta.code.g.core.code.builder.JavaMethodBuilder;
import org.abstractmeta.code.g.core.code.builder.JavaTypeBuilder;
import org.abstractmeta.code.g.core.renderer.TypeRenderer;
import org.abstractmeta.code.g.renderer.JavaTypeRenderer;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

@Test
public class TypeRendererTest {

    public void testRender() {
        JavaTypeBuilder typeBuilder = new JavaTypeBuilder();
        typeBuilder.setTypeName("com.foo.Test");
        typeBuilder.addField(new JavaFieldBuilder().setName("field1").setType(int.class).setImmutable(true).build());
        typeBuilder.addField((new JavaFieldBuilder().setName("field2").setType(String.class).build()));
        typeBuilder.addMethod(new JavaMethodBuilder().setName("test").setResultType(void.class)
                .addParameter("arg1", Map.class).build());

        JavaTypeRenderer renderer = new TypeRenderer();
        JavaTypeImporter importer = new JavaTypeImporterImpl(typeBuilder.getPackageName());
        String classFragment = renderer.render(typeBuilder.build(), importer, 0);

        Assert.assertTrue(classFragment.contains("final int field1;"));
        Assert.assertTrue(classFragment.contains("String field2;"));
    }

}
